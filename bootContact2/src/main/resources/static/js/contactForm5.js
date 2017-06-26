//グローバル変数
var curRow;      // テーブル一覧の現在の選択行
var editMode=0;  // 編集モード（表示:0, 編集:1, 追加:2）

jQuery(function($) {
  //$$$$$$$$$$ 必要な関数を定義 $$$$$$$$$$
  //## Spring SecurityのCSRF対策によるトークンをajax通信で設定する
  $.ajaxPrefilter(function(options, originalOptions, jqXHR) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    jqXHR.setRequestHeader(header, token);
  });
  
  //$$$ フォームの Input を contact(JSON形式)に変換（1）
  function getContactFromForm(formId) {
    var seibetu = $('[name="seibetu"]:checked').val();
    var syumiChecked = "";
    $('[name="syumiList"]:checked').each(function() {
      syumiChecked += $(this).val() + ",";
    });
    var contact = {
        "id" : $("#id").val(),
        "version" : $("#version").val(),
        "simei" : $("#simei").val(),
        "yomi" : $("#yomi").val(),
        "keisyo" : $("#keisyo").val(),
        "seibetu" : seibetu,
        "seinenGappi" : $("#seinenGappi").val(),
        "memo" : $("#memo").val(),
        "syumi" : $("#syumi").val(),
        "kaisya" : $("#kaisya").val(),
        "syozoku" : $("#syozoku").val(),
        "yakusyoku" : $("#yakusyoku").val(),
        "syokusyu" : $("#syokusyu").val(),
        "syubetu" : $("#syubetu").val(),
        "syumi" : syumiChecked,
      };
     alert("contact=" + JSON.stringify(contact))
     return contact;
  }
  
  //$$$ フォームの Input を JSON に変換 (この関数はページ構造に依存)
  var formToJson = function() {
    // フォームの内容を取得: serializeArrayの戻り値は[{key: 'キーの名前', value: '値'}]という形式
    var aData = $('#contactForm input').serializeArray();
    alert(JSON.stringify(aData));
    var json = {};
    $.each(aData, function(i, v) {
      json[v.name] = v.value || "";
    });
    // 不要な要素を削除する
    delete json["_csrf"];
    delete json["syumiList"];
    
    // 複数チェックボックスは別ルーチンで取得
    json["syumi"] = getCheckBox("syumi");
    
    // user_id から User を作成することも必要
    json["user"] = { "id" : Number($('#user_id').val()) };
    return json;
  }

  //$$$ 複数チェックボックスからカンマ区切り文字列を取得
  function getCheckBox(boxName) {
    return $('#'+boxName).find('input:checkbox:checked').map(function() {
      return $(this).val();
    }).get().join(",");
  }
  
  //$$$ 編集モードの設定 表示:0, 編集:1, 追加:2
  function setEditMode(mode) {
    //alert("setEditMode: mode=" + mode);
    editMode = mode;
    if (editMode > 0) {// 編集または追加
      // 保存ボタンを有効にする
      $('#btnSave').prop("disabled", false);
      $('#tabNaviEdit').tab('show');
    } else {
      // 保存ボタンを無効にする
      //$('#btnSave').prop("disabled", true);
      $('#tabNaviDisp').tab('show');
    }
  }
  
  //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
  // ボタン
  //&&& 保存ボタン：FORM編集データの保存
  $("#btnSave").click(function() {    
    // 多重送信を防ぐため通信完了までボタンをdisableにする
    var button = $(this);
    button.attr("disabled", true);
    $('#contactForm').find('span').remove();
    
    // リクエストのURLを組み立てる
    var url = window.location.protocol + "//" + window.location.host
    + "/bootContact/contact/save";
    
    // フォームから保存する連絡先データを取得
    var contact = formToJson();
    alert("url="+url + "\n" + JSON.stringify(contact));
    
    // 通信実行
    var saveContact = $.ajax({
      async : false,                    // 別ウインドウで開くため
      type : "post",
      url : url,
      data : JSON.stringify(contact),   // JSONデータ本体
      contentType : 'application/json', // リクエストの Content-Type
      dataType : "json",                // レスポンスをJSONとしてパースする
      
    }).done(function(cmd) {
      //>>>>> 通信成功
      alert(cmd.message);
      if (cmd.statas==11 || cmd.status==12) {
        if (cmd.status==11) {    //## 追加成功時
          // テーブル行に行を追加
        } else if (cmd.status=12) {  //## 更新成功時
          // 該当行のデータ変更
        }
        $("#version").val(cmd.version);
        oContact = cmd.contact;
        // oContact --> 表示エリア
        setDispContact(oContact)
        setEditMode(0);
      }
      
    }).fail(function(XMLHttpRequest, textStatus, errorThrown) {
      //>>>>> 通信失敗
      // とりあえずエラー状態を表示
      alert("XMLHttpRequest : " + XMLHttpRequest.status
          + "\ntextStatus : " + textStatus + "\nerrorThrown : "
          + errorThrown.message);
      
    }).always(function() {
      //>>>>> 成功・失敗に関わらず通信が終了した際の処理
      $btn.attr("disabled", false); // ボタンを再び enableにする
    });
  });
  
  //&&& 削除ボタン：連絡先データの削除
  $("#btnDel").click(function() {
    alert("XXX");
    var url = window.location.protocol + "//" + window.location.host
    + "/bootContact/contact/delete";
    $.get(url);
    alert(url);
    return;
    $('#errMsg').html('');
    // ajax で カレント行のデータを削除して成功時はメッセージを表示
    // 一覧テーブルのカレント行の連絡先データ.id を取得
    curRow = getCurRow();
    var id = curRow.find('td:first').text();
    var url = window.location.protocol + "//" + window.location.host
      + "/bootContact/contact/delete/" + id;
    //alert("url="+url);
    $.getJSON(url, {}, function(cmd) {
      alert(cmd.message);
      // $('#errMsg').html(msg);
      //var index = curRow.index();
      //curRow.remove();
      //curRow = $('#contact-table tr').eq(index + 1);
      //curRow.addClass('active');
      //selectRow(curRow.find('td:first').text());
    });
  });

  //&&& 追加ボタン
  $("#btnAdd").click(function() {
    alert("sss");
    // フォームに空の連絡先を設定
    setFormContact( createContact(
        // id, ver, simei, yomi, keisyo, seibetu, seinenGappi,
        null, 0,   "新規追加",    "しんきついか",   "様",     "女",      "1980/08/01",
        // memo, syumi, kaisya, syozoku, yakusyoku, syokusyu, syubetu
        "",     "",    "",     "",      "",        "",       "家族・親戚") );
    // 編集モードを追加に設定
    setEditMode(2);
  });
  
  //&&& 編集ボタン
  $("#btnEdit").click(function() {
    // 現連絡先データをフォームにセットして編集モードにする
    //alert("btnEdit: id=" + oContact.id + ",simei=" + oContact.simei + ",yomi=" + oContact.yomi
    //    + ",seibetu=" + oContact.seibetu + ",seinenGappi=" + oContact.seinenGappi + "version="+oContact.version);
    setFormContact(oContact);
    setEditMode(1);
  });
  
  //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
  // DOM：データの設定と取得
  //%%% フォームのフィールドに、渡された連絡先の値を設定する
  function setFormContact(ct) {
    //alert("setForm: simei=" + ct.simei);
    $("#id").val(ct.id);
    $("#version").val(ct.version);
    $("#simei").val(ct.simei);
    $("#yomi").val(ct.yomi);
    $("#keisyo").val(ct.keisyo);
    $("#seinenGappi").val(ct.seinenGappi);
    $("#memo").val(ct.memo);
    $("#syumi").val(ct.syumi);
    $("#kaisya").val(ct.kaisya);
    $("#syozoku").val(ct.syozoku);
    $("#yakusyoku").val(ct.yakusyoku);
    $("#syokusyu").val(ct.syokusyu);
    $("#syubetu").val(ct.syubetu);
    // 性別
    $('input[name="seibetu"]').val([ct.seibetu]);
    // 趣味
    // Contact の syumi(カンマ区切りもじれ列) をカンマで分割
    var syumiList = ct.syumi.split(",");
    // form 内の input で name属性が"seibetu" のものについて
    $('[name="syumiList"]').each(function() {
      for (syumi in syumiList) {
        if ($(this).val() == syumiList[syumi]) {
          $(this).prop('checked',true);
        };
      }
    });
  }
  
  //%%% 指定した値を設定した連絡先オブジェクトを生成
  function createContact(id, ver, simei, yomi, keisyo, seibetu, seinenGappi,
      memo, syumi, kaisya, syozoku, yakusyoku, syokusyu, syubetu) {
    //alert("createContact");
    var c = {
        "id" : id,
        "version" : ver,
        "simei" : simei,
        "yomi" : yomi,
        "keisyo" : keisyo,
        "seibetu" : seibetu,
        "seinenGappi" : seinenGappi,
        "memo" : memo,
        "syumi" : syumi,
        "kaisya" : kaisya,
        "syozoku" : syozoku,
        "yakusyoku" : yakusyoku,
        "syokusyu" : syokusyu,
        "syubetu" : syubetu
      };
    return c;
  }

  //%%% 詳細グリッドの表示タブペイン表示エリアに連絡先情報を設定
  function setDispContact(ct) {
    //alert("setDisp : simei=" + ct.simei);
    $("#_id").text(ct.id);
    $("#_simei").text(ct.simei);
    $("#_yomi").text(ct.yomi);
    $("#_keisyo").text(ct.keisyo);
    $("#_seibetu").text(ct.seibetu);
    $("#_seinenGappi").text(ct.seinenGappi);
    $("#_memo").text(ct.memo);
    $("#_syumi").text(ct.syumi);
    $("#_kaisya").text(ct.kaisya);
    $("#_syozoku").text(ct.syozoku);
    $("#_yakusyoku").text(ct.yakusyoku);
    $("#_syokusyu").text(ct.syokusyu);
  }

  //%%% 性別ラジオボタンから性別データ取得
  function getSeibetu() {
    var sex;
    $('#seibetu').find('input:radio:checked').map(function() {
      sex = $(this).val();
    });
    return sex;
  }
  
  //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
  // テーブル
  //&&& テーブル行クリックイベント
  $('#contact-table tbody').on('click', 'tr', function() {
    setEditMode(0);
    $('#tabNaviDisp').tab('show');
    getCurRow().toggleClass('active'); //カレント行のアクティブを反転して、
    $(this).addClass('active');       //選択行をアクティブに設定
    curRow = $(this);                 //選択行をカレントとして設定
    var id = curRow.find('td:first').text(); //カレント行のidを取得
    selectRow(id);                          //取得idの連絡先をコントローラから取得
  });
  
  //&&& 連絡先一覧テーブルの現選択行を返す
  //まだ選択行が設定されてない場合は先頭行を返す
  getCurRow = function() {
    return (curRow == undefined) ? $('#contact-table tr').eq(1) : curRow;
  }

  //&&& 指定した IDの連絡先をコントローラから取得
  selectRow = function(id) {
    var url = window.location.protocol + "//" + window.location.host
    + "/bootContact/contact/select/" + id;
    alert("url="+url);
    $.getJSON(url, {}, function(contact) {
      alert('contact:'+contact.id);
      if (id == contact.id) {
        //取得した連絡先のid が指定idに等しい場合
        alert('id='+id+",version="+contact.version);
        setDispContact(contact);  //詳細表示エリアに取得連絡先を表示
        oContact = contact;       //グローバル変数にも取得連絡先を保持
      }
    });
  }  
});
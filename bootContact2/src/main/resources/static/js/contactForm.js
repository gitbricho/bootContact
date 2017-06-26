//グローバル変数
var curRow;      // テーブル一覧の現在の選択行
var editMode=0;  // 編集モード（表示:0, 編集:1, 追加:2）

jQuery(function($) {
  //&&& 追加ボタン
  $("#btnAdd").click(function() {
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
    setFormContact(oContact);
    setEditMode(1);
  });
  
  $("#btnSave").click(function() {
    var button = $(this);
    button.attr("disabled", true);
    $('#contactForm').submit();
  });
    
  
  //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
  // テーブル
  //&&& テーブル行クリックイベント
  $('#contact-table tbody').on('click', 'tr', function() {
    setEditMode(0);
    $('#tabNaviDisp').tab('show');
    getCurRow().toggleClass('success'); //カレント行のアクティブを反転して、
    $(this).addClass('success');       //選択行をアクティブに設定
    curRow = $(this);                 //選択行をカレントとして設定
    var id = curRow.find('td:first').text(); //カレント行のidを取得
    var rowNo = $(this)[0].rowIndex - 1;
    selectRow(rowNo, id);
  });
  
  //&&& 連絡先一覧テーブルの現選択行を返す
  //まだ選択行が設定されてない場合は先頭行を返す
  getCurRow = function() {
    if (curRow == undefined) {
      curRow = $('#contact-table tr').eq(1);
      $('#contact-table tbody tr').each(function(){
        var id = $(this).find('td:first').text();
        if(id == oContact.id){
          curRow = $(this);
        }
      });
    }
    return curRow;
  }

  //&&& 指定した IDの連絡先をコントローラから取得
  selectRow = function(rowNo, id) {
    var url = window.location.protocol + "//" + window.location.host
    + "/bootContact/contact/select/" + rowNo;
    $.getJSON(url, {}, function(contact) {
      if (id == contact.id) {
        //取得した連絡先のid が指定idに等しい場合
        setDispContact(contact);  //詳細表示エリアに取得連絡先を表示
        oContact = contact;       //グローバル変数にも取得連絡先を保持
      }
    });
  }  

  //$$$ 編集モードの設定 表示:0, 編集:1, 追加:2
  function setEditMode(mode) {
    editMode = mode;
    if (editMode > 0) {// 編集または追加
      // 保存ボタンを有効にする
      $('#btnSave').prop("disabled", false);
      $('#tabNaviEdit').tab('show');
    } else {
      // 保存ボタンを無効にする
      $('#btnSave').prop("disabled", true);
      $('#tabNaviDisp').tab('show');
    }
  }

  //## Spring SecurityのCSRF対策によるトークンをajax通信で設定する
  $.ajaxPrefilter(function(options, originalOptions, jqXHR) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    jqXHR.setRequestHeader(header, token);
  });
  
  //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
  //%%% 詳細グリッドの表示タブペイン表示エリアに連絡先情報を設定
  function setDispContact(ct) {
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
  //%%% フォームのフィールドに、渡された連絡先の値を設定する
  function setFormContact(ct) {
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
});
jQuery(function($) {
  
  //$$$$$$$$$$ 必要な関数を定義 $$$$$$$$$$
  //## Spring SecurityのCSRF対策によるトークンをajax通信で設定する
  $.ajaxPrefilter(function(options, originalOptions, jqXHR) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    jqXHR.setRequestHeader(header, token);
  });

  //## フォームの Input を JSON に変換 (この関数はページ構造に依存します)
  var formToJson = function() {
    // フォームの内容を取得:
    // serializeArrayの戻り値は[{key: 'キーの名前', value: '値'}]という形式
    var aData = $('#contactForm input').serializeArray();
    //alert(JSON.stringify(aData));
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

  //## 複数チェックボックスからカンマ区切り文字列を取得
  function getCheckBox(boxName) {
    return $('#'+boxName).find('input:checkbox:checked').map(function() {
      return $(this).val();
    }).get().join(",");
  }

  // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
  // 保存ボタン：FORM編集データの保存
  // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
  $("#btnSave").click(function() {
    // 多重送信を防ぐため通信完了までボタンをdisableにする
	alert("save");
    var button = $(this);
    button.attr("disabled", true);
    //$('#contactForm').find('span').remove();
    
    // リクエストのURLを組み立てる
    var url = window.location.protocol + "//" + window.location.host
    + "/bootContact/contact/ajaxsave";
    
    // フォームから保存する連絡先データを取得
    var contact = formToJson();
    alert(JSON.stringify(contact));
    
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
      if (cmd.status==11) {    //## 追加成功時
        // テーブル行に行を追加
        $("#version").val(cmd.version);
      } else if (cmd.status=12) {  //## 更新成功時
        // 該当行のデータ変更
        $("#version").val(cmd.version);
      }
    }).fail(function(XMLHttpRequest, textStatus, errorThrown) {
      //>>>>> 通信失敗
      // とりあえずエラー状態を表示
      alert("XMLHttpRequest : " + XMLHttpRequest.status
          + "\ntextStatus : " + textStatus + "\nerrorThrown : "
          + errorThrown.message);
      
    }).always(function() {
      //>>>>> 成功・失敗に関わらず通信が終了した際の処理
      $('#btnSave').prop("disabled", false); // ボタンを再び enableにする
    });
  });
});
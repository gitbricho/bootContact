package bootcontact;

public class AppConst {
    
    private AppConst() {}
    
    //URL Contact
    public static final String URL_ROOT = "/";
    public static final String URL_HOME = "/home";    
    public static final String URL_LOGIN = "/login";
    public static final String URL_ERROR = "/error";
    public static final String URL_ADMIN = "/admin";

    public static final String URL_CONTACT = "/contact";        //連絡先管理
    public static final String URL_SYUBETU = "/syubetu/{syu}";  //種別変更
    public static final String URL_THEME = "/theme/{theme}";    //テーマ変更
    public static final String URL_NEXT = "/nextPage";
    public static final String URL_PREV = "/prevPage";
    public static final String URL_SEARCH = "/search";
    //URL Contact CRUD
    public static final String URL_SELECT_ID = "/select/{id}";     //連絡先選択
    public static final String URL_SELECT_ROW = "/select/{rowNo}"; ///連絡先選択
    public static final String URL_DELETE = "/delete";          //連絡先削除
    public static final String URL_SAVE = "/save";              //連絡先保存
    public static final String URL_LIST = "/list";
    //URL User
    public static final String URL_USER_LIST= "/user_list";     //ユーザー一覧

    //View Name    
    public static final String VIEW_HOME = "views/home";            //ホーム
    public static final String VIEW_LOGIN = "views/login";          //ログイン
    public static final String VIEW_ERROR = "error";
    public static final String VIEW_USER_LIST = "views/user_list";  //ユーザー一覧
    public static final String VIEW_CONTACT = "views/contact";      //連絡先
        
    //属性名
    public static final String ATTR_CUR_SYUBETU = "curSyubetu";     //現連絡先種別
    public static final String ATTR_CUR_THEME = "curTheme";         //現テーマ
    public static final String ATTR_CONTACT_LIST = "contactList";   //連絡先リスト
    public static final String ATTR_CONTACT = "contact";            //現連絡先
    public static final String ATTR_CONTACT_ID = "contact.id";
    public static final String ATTR_SYUMI_ITEMS = "syumiItems"; 
    public static final String ATTR_CONTACT_SYUBETU_ITEMS = "contactSyubetuItems"; 
    public static final String ATTR_SEIBETU_ITEMS = "seibetuItems"; 
    public static final String ATTR_LOGIN_USER = "loginUser";
    public static final String ATTR_USER_LIST = "userList";
    
    public static final String ATTR_CUR_PAGE_NO = "curPageNo";
    public static final String ATTR_CUR_ROW_NO = "curRowNo";
    public static final String ATTR_PAGE_COUNT = "pageCount";
    
    //CRUD ステータス
    public static final int STS_CRE_OK = 11;
    public static final int STS_UPD_OK = 12;
    public static final int STS_DEL_OK = 13;
    public static final int STS_VALID_ERR = 90;
    public static final int STS_SAVE_ERR = 92;
    public static final int STS_DEL_ERR = 93;
    //CRUD メッセージプロパティ
    public static final String MSG_SAVE_OK = "contact.save.ok";
    public static final String MSG_CRE_OK = "contact.cre.ok";
    public static final String MSG_UPD_OK = "contact.upd.ok";
    public static final String MSG_DEL_OK = "contact.del.ok";
    public static final String MSG_VALID_ERR = "contact.valid.err";

}

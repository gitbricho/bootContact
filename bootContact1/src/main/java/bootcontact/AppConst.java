package bootcontact;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 定数定義.
 */
public class AppConst {
    
    private AppConst() {}
    
    //#### URL ########################
    /** アプリケーションのルートURL */
    public static final String URL_ROOT = "/";
    /** ホームビューURL */
    public static final String URL_HOME = "/home";    
    /** ユーザー一覧ビューURL */
    public static final String URL_USER_LIST = "/user_list";
    /** ログインビューURL */
    public static final String URL_LOGIN = "/login";
    /** エラーURL */
    public static final String URL_ERROR = "/error";
    /** 管理者ロールがアクセス可能なURL */
    public static final String URL_ADMIN = "/admin";
    
    public static final String URL_CONTACT = "/contact";        //連絡先親パス
    public static final String URL_THEME = "/theme/{theme}";    //テーマ変更
    public static final String URL_LIST = "/list";              //連絡先一覧
    public static final String URL_FORM = "/form";              //フォーム
    public static final String URL_SELECT = "/select";          //選択
    public static final String URL_SAVE = "/save";              //保存
    public static final String URL_DELETE = "/delete";          //削除
    public static final String URL_ADD_PHONE = "/addPhone";     //電話追加
    public static final String URL_ADD_MAIL = "/addMail";       //メール追加
    public static final String URL_ADD_ADDRESS = "/addAddress"; //住所追加
    public static final String URL_DEL_PHONE = "/delPhone";     //電話削除
    public static final String URL_DEL_MAIL = "/delMail";       //メール削除
    public static final String URL_DEL_ADDRESS = "/delAddress"; //住所削除
    
    //#### VIEW ########################
    /** ホームビュー名 */    
    public static final String VIEW_HOME = "views/home"; 
    /** ログインビュー名 */    
    public static final String VIEW_LOGIN = "views/login"; 
    /** ユーザー一覧ビュー名 */    
    public static final String VIEW_USER_LIST = "views/user_list";
    /** エラービュー名 */    
    public static final String VIEW_ERROR = "error";
    
    public static final String VIEW_CONTACT_LIST = "views/contact_list"; 
    public static final String VIEW_CONTACT_DETAIL = "views/contact_detail"; 
    public static final String VIEW_CONTACT_FORM = "views/contact_form"; 
    public static final String VIEW_CONTACT_FORM_AJAX = "views/contact_form_ajax"; 
        
    //#### 属性名 ######################
    /** カレントの連絡先種別(String)を登録する属性名 */
    public static final String ATTR_CUR_SYUBETU = "curSyubetu"; 
    /** カレントのテーマ(String)を登録する属性名 */
    public static final String ATTR_CUR_THEME = "curTheme";
    /** ユーザー一覧(List<User>)を登録する属性名 */
    public static final String ATTR_USER_LIST = "userList";
    /** ログインユーザー(User)を登録する属性名 */
    public static final String ATTR_LOGIN_USER = "loginUser";
    public static final String ATTR_CONTACT_LIST = "contactList"; 
    public static final String ATTR_CONTACT = "contact"; 
    public static final String ATTR_CONTACT_ID = "contact.id"; 
    public static final String ATTR_SYUMI_ITEMS = "syumiItems"; 
    public static final String ATTR_CONTACT_SYUBETU_ITEMS = "contactSyubetuItems"; 
    public static final String ATTR_SEIBETU_ITEMS = "seibetuItems"; 
    public static final String ATTR_PHONE_SYUBETU_ITEMS = "phoneSyubetuItems"; 
    public static final String ATTR_MAIL_SYUBETU_ITEMS = "mailSyubetuItems"; 
    public static final String ATTR_JUSYO_SYUBETU_ITEMS = "jusyoSyubetuItems"; 
    
    public static final String ATTR_THEME_NAME = "themeName";
    public static final String ATTR_KEKKA = "kekka";
    
    //#### CRUD ステータス #################
    public static final int STS_CRE_OK = 11;
    public static final int STS_UPD_OK = 12;
    public static final int STS_DEL_OK = 13;
    public static final int STS_VALID_ERR = 90;
    public static final int STS_SAVE_ERR = 92;
    public static final int STS_DEL_ERR = 93;
    //#### CRUD メッセージプロパティ ##############
    public static final String MSG_SAVE_OK = "contact.save.ok";
    public static final String MSG_CRE_OK = "contact.cre.ok";
    public static final String MSG_UPD_OK = "contact.upd.ok";
    public static final String MSG_VALID_ERR = "contact.valid.err";

    //#### テストデータ ###################
    // 更新データ(連絡先)
    public static final String C_UPD_SIMEI_SEI = "相田XX";
    public static final String C_UPD_YOMI_SEI = "あいだXX";
    public static final String C_UPD_SEINEN_GAPPI = "1990/09/09";
    // 新規データ(連絡先)
    public static final String C_ADD_ID = "900100";
    public static final String C_ADD_VERSION = "0"; 
    public static final String C_ADD_SIMEI_SEI = "新規";
    public static final String C_ADD_SIMEI_MEI = "追加";
    public static final String C_ADD_YOMI_SEI = "しんき";
    public static final String C_ADD_YOMI_MEI = "ついか";
    public static final String C_ADD_SEIBETU = "女";
    public static final String C_ADD_SEINEN_GAPPI = "1980/01/01";
    public static final String C_ADD_SYUBETU = "家族親戚";
    public static final String C_ADD_USER_ID = "1";
}

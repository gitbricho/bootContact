package bootapp;

/**
 * 定数定義.
 */
public class AppConst {
    
    private AppConst() {}
    
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
    
    /** ホームビュー名 */    
    public static final String VIEW_HOME = "views/home"; 
    /** ログインビュー名 */    
    public static final String VIEW_LOGIN = "views/login"; 
    /** ユーザー一覧ビュー名 */    
    public static final String VIEW_USER_LIST = "views/user_list";
    /** エラービュー名 */    
    public static final String VIEW_ERROR = "error";
    
    /** カレントの連絡先種別(String)を登録する属性名 */
    public static final String ATTR_CUR_SYUBETU = "curSyubetu"; 
    /** カレントのテーマ(String)を登録する属性名 */
    public static final String ATTR_CUR_THEME = "curTheme";
    /** ユーザー一覧(List<User>)を登録する属性名 */
    public static final String ATTR_USER_LIST = "userList";
    /** ログインユーザー(User)を登録する属性名 */
    public static final String ATTR_LOGIN_USER = "loginUser";
}

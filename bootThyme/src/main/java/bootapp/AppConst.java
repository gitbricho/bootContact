package bootapp;

public class AppConst {
    
    private AppConst() {}
    
    //URL
    public static final String URL_ROOT = "/";
    public static final String URL_HOME = "/home";    
    public static final String URL_USER_LIST = "/user_list";
    public static final String URL_LOGIN = "/login";
    public static final String URL_ERROR = "/error";
    public static final String URL_ADMIN = "/admin";
    
    public static final String URL_THYME = "/thyme";
    
    public static final String URL_FRAG_NAVI01 = "/fragNavi01";
    public static final String URL_FRAG_FIELD01 = "/fragField01";
    public static final String URL_TH_ITERATION = "/thIteration";
    public static final String URL_TH_CONDITION = "/thCondition";
    public static final String URL_TH_INLINE = "/thInline";
    public static final String URL_TH_EXPRESSIONS = "/thExpressions";
    public static final String URL_TH_LITERAL = "/thLiteral";
    
    //ビュー名    
    public static final String VIEW_HOME = "views/home"; 
    public static final String VIEW_LOGIN = "views/login"; 
    public static final String VIEW_USER_LIST = "views/user_list";
    public static final String VIEW_ERROR = "error";
    
    public static final String VIEW_FRAG_FIELD01 = "views/thyme/fragField01";
    public static final String VIEW_TH_ITERATION = "views/thyme/Iteration";
    public static final String VIEW_TH_CONDITION = "views/thyme/Condition";
    public static final String VIEW_TH_INLINE = "views/thyme/Inline";
    public static final String VIEW_TH_EXPRESSIONS = "views/thyme/Expressions";
    public static final String VIEW_TH_LITERAL = "views/thyme/LiteralAndOperation";
    public static final String VIEW_THYME_KEKKA = "views/thyme/kekka";

    
    //属性名
    public static final String ATTR_CUR_SYUBETU = "curSyubetu"; 
    public static final String ATTR_CUR_THEME = "curTheme";
    public static final String ATTR_USER_LIST = "userList";
    public static final String ATTR_LOGIN_USER = "loginUser";
    
    public static final String ATTR_CONTACT = "contact"; 
    public static final String ATTR_KEKKA = "kekka";

    public static final String ATTR_SYUMI_ITEMS = "syumiItems"; 
    public static final String ATTR_CONTACT_SYUBETU_ITEMS = "contactSyubetuItems"; 
    public static final String ATTR_SEIBETU_ITEMS = "seibetuItems"; 
    
    //CRUD メッセージプロパティ
    public static final String MSG_SAVE_OK = "contact.save.ok";

}

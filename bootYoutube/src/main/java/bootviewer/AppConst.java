package bootviewer;

public class AppConst {

  private AppConst() {
  }

  // URL
  public static final String URL_ROOT = "/";
  public static final String URL_HOME = "/home";
  public static final String URL_USER_LIST = "/user_list";
  public static final String URL_VIDEO_LIST = "/video_list";
  public static final String URL_VIDEO_PLAYER = "/video_player";
  public static final String URL_LOGIN = "/login";
  public static final String URL_ERROR = "/error";
  public static final String URL_ADMIN = "/admin";

  public static final String URL_YOUTUBE_SEARCH_LIST = "/youtube_list";

  // ビュー名
  public static final String VIEW_HOME = "views/home";
  public static final String VIEW_LOGIN = "views/login";
  public static final String VIEW_USER_LIST = "views/user_list";
  public static final String VIEW_VIDEO_LIST = "views/video_list";
  public static final String VIEW_VIDEO_PLAYER = "views/video_player";

  public static final String VIEW_YOUTUBE_LIST = "views/youtube_list";

  public static final String VIEW_ERROR = "error";

  // 属性名
  public static final String ATTR_CUR_SYUBETU = "curSyubetu";
  public static final String ATTR_CUR_THEME = "curTheme";
  public static final String ATTR_USER_LIST = "userList";
  public static final String ATTR_LOGIN_USER = "loginUser";
}

package bootviewer.controller;

/**
 * サーチコマンド.
 * <p>
 * Yotyube ビデオ一覧ビューの form 情報を管理するバッキング・ビーン
 * </p>
 */
public class SearchCmd {
  /** テキスト入力欄の入力値（検索文字列）を保持. */
  private String searchStr = "";
  /** 選択ボックスで選択した検索文字列を保持. */
  private String selectStr = "";
  /** 結果一覧の表示順の設定値を保持. */
  private String order = "viewCount"; // date, rating, title
  /** キャプションの有無 */
  private boolean videoCaption = false; // closedCaption
  /** 高解像｜標準解像 */
  private boolean videoHeighDef = true;
  private String videoDuration = ""; // long, medium, short

  public SearchCmd() {
  }

  public SearchCmd(String str, String select) {
    this.searchStr = str;
    this.selectStr = select;
  }

  public String getSearchStr() {
    return searchStr;
  }

  public void setSearchStr(String searchStr) {
    this.searchStr = searchStr;
  }

  public String getSelectStr() {
    return selectStr;
  }

  public void setSelectStr(String selectStr) {
    this.selectStr = selectStr;
  }

  public String getOrder() {
    return order;
  }

  public void setOrder(String order) {
    this.order = order;
  }

  public boolean isVideoCaption() {
    return videoCaption;
  }

  public void setVideoCaption(boolean videoCaption) {
    this.videoCaption = videoCaption;
  }

  public boolean isVideoHeighDef() {
    return videoHeighDef;
  }

  public void setVideoHeighDef(boolean videoHeighDef) {
    this.videoHeighDef = videoHeighDef;
  }

  public String getVideoDuration() {
    return videoDuration;
  }

  public void setVideoDuration(String videoDuration) {
    this.videoDuration = videoDuration;
  }
}

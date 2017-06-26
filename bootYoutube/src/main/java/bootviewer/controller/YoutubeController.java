package bootviewer.controller;

import static bootviewer.AppConst.URL_ROOT;
import static bootviewer.AppConst.URL_YOUTUBE_SEARCH_LIST;
import static bootviewer.AppConst.VIEW_YOUTUBE_LIST;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bootviewer.model.Videos;

/**
 * Youtube ビデオ検索コントローラ.
 * <p>
 * Youtyube Data Api V3 を使用してビデオ検索を行う.
 * </p>
 */
@Controller
@Scope("session")
public class YoutubeController {

  final static private Logger log = LoggerFactory.getLogger(YoutubeController.class);

  // Youtube Data API V3 URL
  private static final String YOUTUBE_BASE_URL = "https://www.googleapis.com/youtube/v3/search";
  private static final String API_KEY = "あなたのアプリキー";

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private RestTemplate restTemplate;

  // form バッキング・ビーン
  private SearchCmd zCurSearchCmd;

  /**
   * コンストラクタ.
   */
  public YoutubeController() {
    // セッションで使用するサーチコマンドオブジェクトを作成
    this.zCurSearchCmd = new SearchCmd();
  }

  /**
   * 検索文字列のリストを作成.
   * 
   * @return 検索文字列のリスト
   */
  @ModelAttribute("selItems")
  public List<String> loadSelItems() {
    // 現時点ではハードコードした文字列の配列を返す.
    // TODO: DBで管理するように変更する.
    return Arrays.asList(new String[] { "Adele", "Alicia+Keys", "Alizee", "Avril", "Beyonce",
        "Britney+Spears", "Clarkson", "Coltrane", "Norah+Jones" });
  }

  /**
   * 起動時または videoList GETリクエストに応答.
   * 
   * @return レスポンスビュー (/views/youtube_list.html)
   */
  @GetMapping(value = { URL_ROOT, URL_YOUTUBE_SEARCH_LIST })
  public ModelAndView videoList() throws JsonParseException, JsonMappingException, IOException {
    ModelAndView mav = new ModelAndView(VIEW_YOUTUBE_LIST);
    // サーチコマンドの検索文字列で検索して、結果（ビデオ一覧）を取得.
    Videos videos = getVideos("", zCurSearchCmd.getSearchStr());
    // 取得したビデオ一覧をモデルに登録.
    mav.addObject("searchItems", videos.getItems());
    // 結果の次のページを表示するためのトークンをモデルに登録.
    // 1ページに表示される件数は制限されるので.
    mav.addObject("nextToken", videos.getNextPageToken());
    mav.addObject("searchCmd", zCurSearchCmd);
    return mav;
  }

  // 検索結果のビデオ一覧を取得.
  private Videos getVideos(String pageToken, String qry) {
    // リクエストの生成
    StringBuilder sb = new StringBuilder(YOUTUBE_BASE_URL);
    // 必須パラメータ: part: API レスポンスに含める内容を指定.
    // snippet と id を指定できる.
    // snippet を指定して、タイトルや説明を取得する.
    sb.append("?part=snippet");
    // eventType: 検索対象をブロードキャスト イベントに制限.
    // completed – 完了したブロードキャストのみを含める.
    // live – アクティブなブロードキャストのみを含める.
    // upcoming – 今後配信予定のブロードキャストのみを含める.

    // maxResults: 結果セットとして返されるアイテムの最大数を指定.
    // 0 以上 50 以下の値を指定できる. デフォルト値は 5.
    sb.append("&maxResults=10");
    // order: リソースの並べ替え方法を指定。
    // date – 作成日の新しい順。
    // rating – 評価の高い順。
    // relevance – 検索クエリの関連性が高い順。デフォルト値。
    // title – タイトルのアルファベット順。
    // viewCount – 再生回数の多い順。
    sb.append("&order=").append(zCurSearchCmd.getOrder());
    // pageToken: 返される結果セットに含める特定のページを指定。
    // nextPageToken | prevPageToken プロパティは取得可能な他のページを表す。

    // publishedAfter: 指定した日時より後に作成されたリソースのみ指定。
    // 例: 1970-01-01T00:00:00Z

    // type: 検索クエリの対象を特定のタイプのリソースのみに制限。
    // 値はカンマで区切られたリソースのタイプのリスト。
    // デフォルト値は video,channel,playlist です。
    sb.append("&type=video");
    // videoCaption: (closedCaption – 字幕がある | none – 字幕がない)
    if (zCurSearchCmd.isVideoCaption()) {
      sb.append("&videoCapttion=closedCaption");
    }
    // videoDefinition: (high – HD 動画のみ | standard – SD 動画のみ)
    if (zCurSearchCmd.isVideoHeighDef()) {
      sb.append("&videoDefinition=high");
    }
    // videoDuration: (long – 20 分以上 | medium – 4 分以上 | short – 4 分未満)
    if (zCurSearchCmd.getVideoDuration().length() > 0) {
      sb.append("&videoDuration=").append(zCurSearchCmd.getVideoDuration());
    }
    // videoType: (any | episode – 番組のエピソードのみ | movie – 動画のみ)

    // 検索クエリー
    sb.append("&q=").append(qry);
    // APIキー
    sb.append("&key=").append(API_KEY);

    // ページトークン
    if (pageToken.length() > 0) {
      sb.append("&pageToken=").append(pageToken);
    }

    // リクエストを実行
    String req = sb.toString();
    log.debug("req=" + req);
    String result = restTemplate.getForObject(req, String.class);
    // log.debug("get:result="+result);
    // 結果の JSON を Java オブジェクトに変換
    Videos videos = new Videos();
    try {
      videos = mapper.readValue(result, Videos.class);
      log.debug("videos:" + videos.toString());
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return videos;
  }

  /**
   * トークンを使って検索結果の次のページを表示する.
   * 
   * @param token
   * @return
   */
  @GetMapping("/youtube_next/{token}")
  public ModelAndView nextPage(@PathVariable("token") String token) {
    ModelAndView mav = new ModelAndView(VIEW_YOUTUBE_LIST);
    String nextToken = "";
    if (token != null) {
      nextToken = token;
    }
    log.debug(">>>> next:{}", nextToken);
    String sstr = zCurSearchCmd.getSelectStr().length() > 0 ? zCurSearchCmd.getSelectStr()
        : zCurSearchCmd.getSearchStr();
    Videos videos = getVideos(nextToken, sstr);
    mav.addObject("searchItems", videos.getItems());
    mav.addObject("nextToken", videos.getNextPageToken());
    mav.addObject("searchCmd", zCurSearchCmd);
    return mav;
  }

  /**
   * Yotyube ビデオ一覧ビューで検索ボタンが押されたときに呼ばれる.
   * 
   * @param cmd
   *          検索情報を保持するサーチコマンドオブジェクト.
   * @return
   */
  @PostMapping("/youtube_search")
  public ModelAndView search(@Valid SearchCmd cmd) {
    ModelAndView mav = new ModelAndView(VIEW_YOUTUBE_LIST);
    if (cmd != null) {
      zCurSearchCmd = cmd;
    }
    log.debug(">>>> search:search={}, select={}", zCurSearchCmd.getSearchStr(),
        zCurSearchCmd.getSelectStr());
    // select(検索文字列リスト)の選択項目を取得.
    String sstr = zCurSearchCmd.getSelectStr();

    if (zCurSearchCmd.getSearchStr().length() > 0) {
      // 検索文字列が入力されている場合は select 選択項目をクリア
      zCurSearchCmd.setSelectStr("");
      // 入力された検索文字列を使用.
      sstr = zCurSearchCmd.getSearchStr();
    }
    // 選択された検索文字列で検索して結果のビデオ一覧を取得,
    Videos videos = getVideos("", sstr);
    mav.addObject("searchItems", videos.getItems());
    mav.addObject("nextToken", videos.getNextPageToken());
    mav.addObject("searchCmd", zCurSearchCmd);
    return mav;
  }

}

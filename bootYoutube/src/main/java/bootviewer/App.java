package bootviewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Youtube ビデオ検索アプリケーション.
 * <p>
 * Spring boot 雛形アプリケーション ( bootAppS ) を ベースとして作成したサンプルアプリケーション.
 * </p>
 * <p>
 * このアプリでは主に以下のトピックを扱う.
 * </p>
 * <ul>
 * <li>RestTemplate の使い方.</li>
 * <li>Youtube Data API v3 の使い方</li>
 * </ul>
 */
@SpringBootApplication
@ComponentScan("bootviewer")
// 明示的にプロパティファイルの位置を指定する場合は以下のコメントをはずす
// @PropertySource("classpath:/app.properties") //ファイルが存在しなければエラー
public class App {
  public static void main(String[] args) {
    SpringApplication.run(App.class);
  }

  // @Bean
  // public ApplicationListener<ApplicationEvent> init() {
  // return event -> System.err.println(event.getClass());
  // }
}
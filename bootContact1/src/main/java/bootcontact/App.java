package bootcontact;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

// MARK: クラス
/**
 * アプリケーション起動クラス.
 * <p>
 * Spring boot サンプルアプリケーションの起動クラス.</p>
 * <p>明示的にプロパティファイルの位置を指定する場合は以下のようにする
 * (ファイルが存在しなければエラーとなる).</p>
 * @PropertySource("classpath:/app.properties")
 */
@SpringBootApplication
@ComponentScan("bootcontact")
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }

    /**
     * カスタマイズビーンを生成する.
     */
    @Bean
    public CustomizeBean customizeBean() {
        CustomizeBean bean = new CustomizeBean();
        return bean;
    }
}
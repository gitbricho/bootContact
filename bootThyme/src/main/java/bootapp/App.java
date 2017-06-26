package bootapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("bootapp")
// 明示的にプロパティファイルの位置を指定する場合は以下のコメントをはずす
// @PropertySource("classpath:/app.properties") //ファイルが存在しなければエラー
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }
}
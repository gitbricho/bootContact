package bootapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * アプリケーション起動クラス.
 * <p>
 * Spring boot 雛形アプリケーションの起動クラス.</p>
 */
@SpringBootApplication
@ComponentScan("bootapp")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }
}
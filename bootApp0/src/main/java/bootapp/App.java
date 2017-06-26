package bootapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 簡単な Spring Boot サンプルアプリケーションの起動/構成クラス.
 * <p>このサンプルアプリはこのコントローラと以下のものしか作成しない.</p>
 * <p>ステップ1: SPRING INITIALIZR で作成した build.gradle</p>
 * <p>ステップ2: ポートとコンテキストパスを変更するための application.properties,
 * 自前のログ設定を示すための logback-spring.xml</p>
 */
@Controller
@EnableAutoConfiguration
public class App {

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(App.class, args);
    }
}
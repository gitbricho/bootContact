package bootapp.controller;

import static bootapp.AppConst.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import bootapp.AppData;

/**
 * ホームコントローラ.
 * <p>アプリケーションのホームビュー、ログインビューの表示を行う.
 */
@Controller
@Scope("session")
public class HomeController {
    
    /**
     * サンプルアプリのホームページを表示.
     * <p>ページを表示する前にテスト用のデータを作成する.</p>
     * <p>リクエストのマッピングの注釈.
     * <ul>
     * <li>ver4.3: @GetMapping が使用できる.
     * <li>ver4.2:
     * @RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
     * </ul></p>
     * @param model org.springframework.ui.Model インスタンス.
     * @return ビュー名
     */
    @GetMapping({ URL_ROOT, URL_HOME })
    public String home(Model model) {
        return VIEW_HOME;
    }

    /**
     * ログイン画面を表示.
     * @return ビュー名
     */
    @GetMapping(URL_LOGIN)
    public String login() {
        return VIEW_LOGIN;
    }
}

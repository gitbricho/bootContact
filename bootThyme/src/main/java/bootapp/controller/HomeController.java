package bootapp.controller;

import static bootapp.AppConst.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import bootapp.AppData;

@Controller
@Scope("session")
public class HomeController {
    
    
    //=====(MAP-METHOD)=====
    
    /**
     * サンプルアプリのホームページを表示.
     * @param model
     * @return ビュー名
     */
    // @RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET) //ver4.2
    @GetMapping({ URL_ROOT, URL_HOME }) // ver 4.3
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

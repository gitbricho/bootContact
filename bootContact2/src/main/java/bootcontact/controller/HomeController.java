package bootcontact.controller;

import static bootcontact.AppConst.*;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import bootcontact.TestData;

@Controller
@Scope("session")
public class HomeController {

    @Autowired
    private TestData data;
    @Autowired
    private HttpSession httpSession;    
    
    /**
     * サンプルアプリのホームページを表示.
     * 
     * @param model
     * @param rq
     * @return
     */
    // @RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
    // //ver4.2
    @GetMapping({ URL_ROOT, URL_HOME }) // ver 4.3
    public String home(Model model, WebRequest rq, Locale locale) {
        data.create();
        String theme = (String)httpSession.getAttribute(ATTR_CUR_THEME);
        httpSession.setAttribute(ATTR_CUR_THEME, theme);
        model.addAttribute(ATTR_CUR_THEME, theme);
        return VIEW_HOME;
    }
    /**
     * ログイン画面を表示.
     * 
     * @param model
     * @param rq
     * @return
     */
    @RequestMapping(URL_LOGIN)
    public String login(Model model) {
        model.addAttribute(ATTR_CUR_THEME, (String)httpSession.getAttribute(ATTR_CUR_THEME));
        return VIEW_LOGIN;
    }
}

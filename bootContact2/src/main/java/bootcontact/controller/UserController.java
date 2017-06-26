package bootcontact.controller;

import static bootcontact.AppConst.ATTR_CUR_THEME;
import static bootcontact.AppConst.ATTR_USER_LIST;
import static bootcontact.AppConst.VIEW_USER_LIST;
import static bootcontact.AppConst.URL_USER_LIST;


import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import bootcontact.model.User;
import bootcontact.repo.UserRepository;

@Controller
public class UserController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    UserRepository userRepo;
    @Autowired
    private HttpSession httpSession;    

    /**
     * ユーザー一覧の表示.
     */
    @GetMapping(URL_USER_LIST)
    public String showUserList(Model model) {
        // 登録ユーザーの全件を取得.
        model.addAttribute(ATTR_CUR_THEME, (String)httpSession.getAttribute(ATTR_CUR_THEME));
        List<User> users = userRepo.findAll();
        users.forEach((user) -> log.debug(user.toString()));
        model.addAttribute(ATTR_USER_LIST, users);
        return VIEW_USER_LIST;
    }
}

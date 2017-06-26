package bootcontact.controller;

import static bootcontact.AppConst.*;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import bootcontact.model.User;
import bootcontact.repo.UserRepository;

// MARK: クラス
/**
 * ユーザーコントローラ.
 */
@Controller
public class UserController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    UserRepository userRepo;
    
    // MARK: マップメソッド
    /**
     * ユーザー一覧の表示.
     */
    @GetMapping(URL_USER_LIST)
    public String showUserList(Model model) {
        // 登録ユーザーの全件を取得.
        List<User> users = userRepo.findAll();
        users.forEach((user) -> log.debug(user.toString()));
        model.addAttribute(ATTR_USER_LIST, users);
        return VIEW_USER_LIST;
    }
}

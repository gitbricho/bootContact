package bootcontact.service;


import static bootcontact.AppConst.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import bootcontact.model.User;
import bootcontact.repo.UserRepository;

/**
 * ユーザーサービスの実装.
 */
@Service
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRepository repo;
    @Autowired
    private HttpSession httpSession;

    public UserServiceImpl() {}

    @Override
    public UserDetails loadUserByUsername(String username) 
            throws UsernameNotFoundException {
        { // 製品版でのログイン認証
            List<User> users = repo.findByName(username);
            if (users == null || users.size() == 0) {
                throw new UsernameNotFoundException("");
            } else {
                User loginUser = users.get(0);
                log.debug("loginUser %%%%%%%%% " + loginUser.toString());
                org.springframework.security.core.userdetails.User user 
                    = new org.springframework.security.core.userdetails.User(
                        loginUser.getName(), loginUser.getPass(), true, true, true,
                        true, getGrantedAuthorities(getRoles(loginUser.getRoles())));
                log.debug("httpSession=" + httpSession);
                httpSession.setAttribute(ATTR_LOGIN_USER, loginUser);
                httpSession.setAttribute(ATTR_CUR_SYUBETU, "家族親戚");
                httpSession.setAttribute(ATTR_CUR_THEME, "");
                return user;
            }
        }
    }

    private List<String> getRoles(String roles) {
        if (roles != null) {
            return Arrays.asList(roles.split(","));
        } else {
            return new ArrayList<String>();
        }
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
        return authorities;
    }
}

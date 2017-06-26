package bootapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bootapp.model.User;
import bootapp.repo.UserRepository;

/**
 * データ作成クラス.
 */
@Component
public class AppData {
    @Autowired
    private UserRepository userRepo;
    
    private int userCount;
    
    private AppData() {}
    
    /**
     * ユーザーデータの作成.
     * <p>ログイン・ユーザーを２件登録する.</p>
     */
    public void createUsers() {
        if (getUserCount() == 0) {
            userRepo.save(new User("tosi", "tosi", "ADMIN"));
            userRepo.save(new User("yumi", "yumi", "USER"));
        }
        userCount = userRepo.findAll().size();
    }
    
    /**
     * 登録ユーザーの件数を返す.
     * @return
     */
    public long getUserCount() {
        return userRepo.count();
    }
}

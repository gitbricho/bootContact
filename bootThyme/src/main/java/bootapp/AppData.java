package bootapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bootapp.model.User;
import bootapp.repo.UserRepository;

@Component
public class AppData {
    @Autowired
    private UserRepository userRepo;
    
    private int userCount;
    
    private AppData() {}
    
    public void createUsers() {
        // ユーザー2件登録 （ユーザー：作成テスト）
        userCount = userRepo.findAll().size();
        if (userCount == 0) {
            userRepo.save(new User("tosi", "tosi", "ADMIN"));
            userRepo.save(new User("yumi", "yumi", "USER"));
        }
        userCount = userRepo.findAll().size();
    }
    
    public int getUserCount() {
        return userCount;
    }
}

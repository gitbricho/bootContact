package bootapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bootapp.model.User;
import bootapp.repo.JutyuRepo;
import bootapp.repo.KokyakuRepo;
import bootapp.repo.UserRepository;

import bootapp.model.Jutyu;
import bootapp.model.Kokyaku;
import bootapp.model.Meisai;

@Component
public class AppData {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private JutyuRepo jRepo;
    @Autowired
    private KokyakuRepo kRepo;
    
    private int userCount;
    private int jutyuCount;
    private int kokyakuCount;
    
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
        
    public void createJutyuData() {
        if (jutyuCount==0) {
            Jutyu jutyu = new Jutyu(100L, "2015/09/01", "納品先01");
            jutyu.getMeisaiList().add(new Meisai("商品001", 100, 2, jutyu));
            jutyu.getMeisaiList().add(new Meisai("商品002--0001", 200, 2, jutyu));
            jutyu = jRepo.save(jutyu);
            jutyu = new Jutyu(200L, "2015/09/01", "納品先02");
            jutyu.getMeisaiList().add(new Meisai(201L, "商品001", 100, 5, jutyu));
            jutyu.getMeisaiList().add(new Meisai(202L, "商品002--0001", 200, 3, jutyu));
            jutyu = jRepo.save(jutyu);
            jutyu = new Jutyu(300L, "2015/09/02", "納品先03XXX-YYY-ZZ");
            jutyu.getMeisaiList().add(new Meisai(301L, "商品001", 100, 5, jutyu));
            jutyu.getMeisaiList().add(new Meisai(302L, "商品002--ABCDE", 200, 3, jutyu));
            jutyu.getMeisaiList().add(new Meisai(303L, "商品003", 300, 8, jutyu));
            jutyu = jRepo.save(jutyu);
            jutyu = new Jutyu(400L, "2015/09/03", "納品先02");
            jutyu.getMeisaiList().add(new Meisai(401L, "商品004", 400, 2, jutyu));
            jutyu.getMeisaiList().add(new Meisai(402L, "商品005", 500, 3, jutyu));
            jutyu = jRepo.save(jutyu);
        }
        jutyuCount = jRepo.findAll().size();
    }
    
    public void createKokyakuData() {
        if (kokyakuCount==0) {
            kRepo.save(new Kokyaku(10L, "納品先01", "k01@xxx.com", 0));
            kRepo.save(new Kokyaku(20L, "納品先02", "k02@xxx.com", 0));
            kRepo.save(new Kokyaku(30L, "納品先03XXX-YYY-ZZ", "k03@xxx.com", 0));
            kRepo.save(new Kokyaku(40L, "納品先04", "k04@xyz.com", 0));
        }
        kokyakuCount = kRepo.findAll().size();        
    }
    
    public int getUserCount() {
        return userCount;
    }

    public int getJutyuCount() {
        return jutyuCount;
    }

    public int getKokyakuCount() {
        return kokyakuCount;
    }
}

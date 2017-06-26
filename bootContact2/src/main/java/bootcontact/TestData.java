package bootcontact;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bootcontact.model.Address;
import bootcontact.model.Contact;
import bootcontact.model.Mail;
import bootcontact.model.Phone;
import bootcontact.model.User;
import bootcontact.repo.ContactRepository;
import bootcontact.repo.UserRepository;

//MARK: クラス
@Component
public class TestData {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ContactRepository contactRepo;
 
    private static final String[] SIMEI_PREFS = { "家族", "親戚" },
            YOMI_PREFS = { "かぞく", "しんせき" };
    private static final int TOSI_KAZOKU_SINSEKI_EXTRA_KENSU = 6; // 作成データの日付はこの値に依存する

    private Long murataContactId;
    private int tosiKensu;
    private int tosiKazokuSinsekiKensu;
    private int tosiYujinTijinKensu;
    private int yumiKensu;
    private int yumiKokyakuKensu;
    private int yumiYujinTijinKensu;
    private int userKensu;
    private boolean create = false;

    public TestData() {
    }

    public void create() {
    	if (create) return;
        createUsers();
        // 連絡先全件削除 (連絡先：削除テスト）
        contactRepo.deleteAll();
        // assertThat(contactRepo.findAll().size()).isEqualTo(0);
        createTosiContacts();
        createYumiContacts();
        create = true;
    }

    public User createTosiContacts() {
        // 連絡先のテストデータ作成
        User user = getUser("tosi");
        // tosi: 家族・親戚（3件）
        tosiKazokuSinsekiKensu = 3;
        String syu = "家族親戚";
        Contact c = new Contact(user, "山田花子", "やまだはなこ", "女", "1980/01/01", syu);
        c.getAddresses().put("自宅", new Address(c, "1234444", "東京都", "千代田区", "", "", "", "自宅"));
        c.getAddresses().put("会社", new Address(c, "1235555", "東京都", "港区", "", "", "", "勤務先"));
        c.getMails().put("自宅", new Mail(c, "h.yamada@x男.com", "自宅"));
        c.getPhones().put("自宅", new Phone(c, "08011112222", "自宅"));
        contactSave(c);
        contactSave(new Contact(user, "鈴木一郎", "すずきいちろう", "男", "1980/01/02", syu));
        contactSave(new Contact(user, "花山隆", "はなやまたかし", "男", "1980/01/03", syu));

        // さらに TOSI_KAZOKU_SINSEKI_EXTRA_KENSU件追加
        tosiKazokuSinsekiKensu += TOSI_KAZOKU_SINSEKI_EXTRA_KENSU * SIMEI_PREFS.length;
        if (user.getName().equals("tosi")) {
            for (int i = 0; i < TOSI_KAZOKU_SINSEKI_EXTRA_KENSU; i++) {
                for (int k = 0; k < SIMEI_PREFS.length; k++) {
                    String simei = String.format("%s%04d", SIMEI_PREFS[k], i).toString();
                    String yomi = String.format("%s%04d", YOMI_PREFS[k], i).toString();
                    String seibetu = i % 2 == 0 ? "男" : "女";
                    contactSave(new Contact(user, simei, yomi, seibetu, "1997/03/01", syu));
                }
            }
        }
        // tosi: 友人・知人（2件）
        tosiYujinTijinKensu = 2;
        syu = "友人知人";
        contactSave(new Contact(user, "森祐一", "もりゆういち", "男", "1981/02/01", syu));
        contactSave(new Contact(user, "村田健一", "むらたけんいち", "男", "1981/02/02", syu));
        tosiKensu = tosiKazokuSinsekiKensu + tosiYujinTijinKensu;

        return user;
    }

    public User createYumiContacts() {
        User user = getUser("yumi");
        // yumi: 友人・知人（2件）
        yumiYujinTijinKensu = 2;
        String syu = "友人知人";
        contactSave(new Contact(user, "岡本太郎", "おかもとたろう", "男", "1982/03/01", syu));
        contactSave(new Contact(user, "沢田由美子", "さわだゆみこ", "女", "1982/03/02", syu));

        // yumi: 顧客（2件）
        yumiKokyakuKensu = 2;
        syu = "顧客";
        contactSave(new Contact(user, "吉田優子", "よしだゆうこ", "女", "1983/04/01", syu));
        contactSave(new Contact(user, "島田悦子", "しまだえつこ", "女", "1983/04/02", syu));
        yumiKensu = yumiKokyakuKensu + yumiYujinTijinKensu;
        return user;
    }

    public int getTosiKensu() {
        return tosiKensu;
    }

    public int getTosiKazokuSinsekiKensu() {
        return tosiKazokuSinsekiKensu;
    }

    public int getTosiYujinTijinKensu() {
        return tosiYujinTijinKensu;
    }

    public int getYumiKensu() {
        return yumiKensu;
    }

    public int getYumiKokyakuKensu() {
        return yumiKokyakuKensu;
    }

    public int getYumiYujinTijinKensu() {
        return yumiYujinTijinKensu;
    }

    public void createUsers() {
        // ユーザー2件登録 （ユーザー：作成テスト）
        if (userRepo.findAll().size() == 0) {
            userRepo.save(new User("tosi", "tosi", "ADMIN"));
            userRepo.save(new User("yumi", "yumi", "USER"));
        }
        userKensu = userRepo.findAll().size();
    }

    public void contactSave(Contact c) {
        c = contactRepo.save(c);
        if (c.getSimei().equals("村田健一")) {
            murataContactId = c.getId();
        }
    }

    public User getUser(String name) {
        // name はユニークなので １件のみ
        List<User> users = userRepo.findByName(name);
        if (users.size() == 1) {
            return users.get(0);
        } else {
            return null;
        }
    }

    public Long getMurataContactId() {
        return murataContactId;
    }

    public String getKensuString() {
        StringBuilder sb = new StringBuilder("tosi:");
        sb.append("(").append(tosiKensu).append("件),");
        sb.append("家族・親戚(").append(tosiKazokuSinsekiKensu).append("件),");
        sb.append("友人・知人(").append(tosiYujinTijinKensu).append("件)作成/ ");
        sb.append("yumi:");
        sb.append("(").append(yumiKensu).append("件),");
        sb.append("顧客(").append(yumiKokyakuKensu).append("件),");
        sb.append("友人・知人(").append(yumiYujinTijinKensu).append("件)作成");
        return sb.toString();
    }

    public String getLogString(String s, String... strs) {
        StringBuilder sb = new StringBuilder(s);
        for (String str : strs) {
            sb.append(str);
        }
        return sb.toString();
    }
    
    public int getUserKensu() {
    	return this.userKensu;
    }
}

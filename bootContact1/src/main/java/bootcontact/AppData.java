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
public class AppData {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ContactRepository contactRepo;
 
    private int tosiKensu;
    private int tosiKazokuSinsekiKensu;
    private int tosiYujinTijinKensu;
    private int yumiKensu;
    private int yumiKokyakuKensu;
    private int yumiYujinTijinKensu;
    private int userKensu;

    public AppData() {
    }

    public void create() {
        createUsers();
        // 連絡先全件削除 (連絡先：削除テスト）
        contactRepo.deleteAll();
        // assertThat(contactRepo.findAll().size()).isEqualTo(0);
        createTosiContacts();
        createYumiContacts();
    }
    
    public User createTosiContacts() {
        // 連絡先のテストデータ作成
        User user = getUser("tosi");
        // tosi: 家族・親戚
        Contact c = null;
        Long id = 100100L;
        tosiKazokuSinsekiKensu = 0;
        String syu = "家族親戚";
        c = new Contact(id, user, "相田", "陽子", "あいだ", "ようこ", "女", "1980/01/01", syu);
        c.getAddresses().add(new Address(id+1, "1234444", "東京都", "千代田区", "", "", "", "自宅"));
        c.getAddresses().add(new Address(id+2, "1235555", "東京都", "港区", "", "", "", "会社"));
        c.getMails().add(new Mail(id+11, "y.aida@xxx.com", "自宅"));
        c.getPhones().add(new Phone(id+21, "08011112222", "自宅"));
        contactSave(c);
        tosiKazokuSinsekiKensu++;
        id +=100;

        c = new Contact(id, user, "井上", "京子", "いのうえ", "きょうこ", "女", "1982/02/01", syu);
        c.getMails().add(new Mail(id+11, "k.inoue@xxx.com", "自宅"));
        c.getPhones().add(new Phone(id+21, "08011113333", "自宅"));
        contactSave(c);
        tosiKazokuSinsekiKensu++;
        id +=100;

        c = new Contact(id, user, "加藤", "正雄", "かとう", "まさお", "男", "1984/03/01", syu);
        c.getMails().add(new Mail(id+11, "m.kato@yyy.com", "自宅"));
        c.getPhones().add(new Phone(id+21, "08011114444", "自宅"));
        contactSave(c);
        tosiKazokuSinsekiKensu++;
        id +=100;

        c = new Contact(id, user, "佐々木", "一郎", "ささき", "いちろう", "男", "1986/01/01", syu);
        c.getMails().add(new Mail(id+11, "i.sasaki@zzz.com", "自宅"));
        c.getPhones().add(new Phone(id+21, "08011115555", "自宅"));
        contactSave(c);
        tosiKazokuSinsekiKensu++;
        id +=100;

        c = new Contact(id, user, "清水", "昌美", "しみず", "まさみ", "女", "1988/05/05", syu);
        c.getMails().add(new Mail(id+11, "m.simizu@xxx.com", "自宅"));
        c.getPhones().add(new Phone(id+21, "08011116666", "自宅"));
        contactSave(c);
        tosiKazokuSinsekiKensu++;
        id +=100;

        c = new Contact(id, user, "鈴木", "伸二", "すずき", "しんじ", "男", "1971/10/12", syu);
        c.getMails().add(new Mail(id+11, "s.suzuki@yyy.com", "自宅"));
        c.getPhones().add(new Phone(id+21, "08011117777", "自宅"));
        contactSave(c);
        tosiKazokuSinsekiKensu++;
        id +=100;

        c = new Contact(id, user, "田村", "義和", "たむら", "よしかず", "男", "1973/12/22", syu);
        c.getMails().add(new Mail(id+11, "y.tamura@yyy.com", "自宅"));
        c.getPhones().add(new Phone(id+21, "08011118888", "自宅"));
        contactSave(c);
        tosiKazokuSinsekiKensu++;
        id +=100;

        c = new Contact(id, user, "中島", "由美子", "なかしま", "ゆみこ", "女", "1975/04/18", syu);
        c.getMails().add(new Mail(id+11, "y.nakasima@yyy.com", "自宅"));
        c.getPhones().add(new Phone(id+21, "08011112222", "自宅"));
        contactSave(c);
        tosiKazokuSinsekiKensu++;
        id +=100;

        c = new Contact(id, user, "花村", "一雄", "はなむら", "かずお", "男", "1977/06/23", syu);
        contactSave(c);
        tosiKazokuSinsekiKensu++;
        id +=100;

        c = new Contact(id, user, "花岡", "優子", "はなおか", "ゆうこ", "女", "1979/07/15", syu);
        contactSave(c);
        tosiKazokuSinsekiKensu++;
        id +=100;

        c = new Contact(id, user, "松岡", "三郎", "まつおか", "さぶろう", "男", "1980/09/03", syu);
        contactSave(c);
        tosiKazokuSinsekiKensu++;
        id +=100;

        c = new Contact(id, user, "山田", "花子", "やまだ", "はなこ", "女", "1980/09/15", syu);
        contactSave(c);
        tosiKazokuSinsekiKensu++;
        id +=100;

        // tosi: 友人・知人（2件）
        id = 110100L;
        syu = "友人知人";
        tosiYujinTijinKensu = 0;
        contactSave(new Contact(id, user, "森祐", "一", "もり", "ゆういち", "男", "1981/02/01", syu));
        tosiYujinTijinKensu++;
        id +=100;
        contactSave(new Contact(id, user, "村田", "健一", "むらた", "けんいち", "男", "1981/02/02", syu));
        tosiYujinTijinKensu++;
        
        tosiKensu = tosiKazokuSinsekiKensu + tosiYujinTijinKensu;

        return user;
    }

    public User createYumiContacts() {
        User user = getUser("yumi");
        // yumi: 友人・知人
        Long id = 200100L;
        yumiYujinTijinKensu = 0;
        String syu = "友人知人";
        contactSave(new Contact(id, user, "岡本", "太郎", "おかもと", "たろう", "男", "1982/03/01", syu));
        yumiYujinTijinKensu++;
        id += 100;
        contactSave(new Contact(id, user, "沢田", "由美子", "さわだ", "ゆみこ", "女", "1982/03/02", syu));
        yumiYujinTijinKensu++;

        // yumi: 顧客（2件）
        id = 210100L;
        syu = "顧客";
        yumiKokyakuKensu = 0;
        contactSave(new Contact(id, user, "吉田", "優子", "よしだ", "ゆうこ", "女", "1983/04/01", syu));
        yumiKokyakuKensu++;
        id += 100;
        contactSave(new Contact(id, user, "島田", "悦子", "しまだ", "えつこ", "女", "1983/04/02", syu));
        yumiKokyakuKensu++;
        
        yumiKensu = yumiYujinTijinKensu + yumiKokyakuKensu;
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

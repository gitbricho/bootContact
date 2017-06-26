package bootcontact;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import bootcontact.model.Address;
import bootcontact.model.Contact;
import bootcontact.model.Mail;
import bootcontact.model.Phone;
import bootcontact.model.User;
import bootcontact.repo.ContactRepository;
import bootcontact.repo.UserRepository;

//MARK: クラス
/**
 * 連絡先リポジトリのテスト.
 * <p>リポジトリのテストを中心に行う。</p>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ContactDBTest {
    private static final Logger log = LoggerFactory.getLogger(ContactDBTest.class);

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ContactRepository contactRepo;
    @Autowired
    private AppData data;
    
    private TestUtil util;

    @Rule
    public LogRule logRule = new LogRule();

    private Validator validator;

    @Before
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        data.create();
        util = new TestUtil(data, contactRepo);
    }

    // MARK: テストメソッド
    @Test
    public void userFindAll() {
    	logRule.testStart("US", "ユーザー全件取得");
        List<User> ulist = userRepo.findAll();
        assertThat(ulist.size()).isEqualTo(data.getUserKensu());
        ulist.forEach(u -> {
            log.info(u.toString());
        });
    }

    @Test
    public void userFindByName() {
    	logRule.testStart("US", "ユーザー(tosi)取得");
        // findByName
        List<User> ulist = this.userRepo.findByName("tosi");
        assertThat(ulist.size()).isEqualTo(1);
        assertThat(ulist.get(0).getName()).isEqualTo("tosi");
        assertThat(ulist.get(0).getRoles()).isEqualTo("ADMIN");
        ulist.forEach(u -> {
            log.info(u.toString());
        });
    }

    @Test
    public void contactFindAll() {
    	int allKensu = data.getTosiKensu() + data.getYumiKensu();
        logRule.testStart("CS", "連絡先全件取得");
        List<Contact> contacts = contactRepo.findAll();
        log.info("contacts.size={}",contacts.size());
        assertThat(contacts.size()).isEqualTo(allKensu);
        util.logAllContact(contacts, "連絡先全件取得");
    }

    @Test
    public void contactFindAllByUserAndSyubetu() {
    	logRule.testStart("CS", "連絡先検索(ユーザー＆種別)");
        User user = data.getUser("tosi");
        List<Contact> contacts = contactRepo.findAll(user, "家族親戚");
        util.logAllContact(contacts, "連絡先取得(tosi,家族親戚)");
        assertThat(contacts.size()).isEqualTo(data.getTosiKazokuSinsekiKensu());
        
        contacts = contactRepo.findAll(user, "友人知人");
        util.logAllContact(contacts, "連絡先取得(tosi,友人知人)");
        assertThat(contacts.size()).isEqualTo(data.getTosiYujinTijinKensu());

        user = data.getUser("yumi");
        contacts = contactRepo.findAll(user, "友人知人");
        util.logAllContact(contacts, "連絡先取得(yumi,友人知人)");
        assertThat(contacts.size()).isEqualTo(data.getYumiYujinTijinKensu());

        contacts = contactRepo.findAll(user, "顧客");
        util.logAllContact(contacts, "連絡先取得(yumi,顧客)");
        assertThat(contacts.size()).isEqualTo(data.getYumiKokyakuKensu());

        contacts = contactRepo.findAll(user, "家族親戚");
        util.logAllContact(contacts, "連絡先取得(yumi,家族親戚)");
        assertThat(contacts.size()).isEqualTo(0);
    }
    
    @Test
    public void contactFindBySimeiAndSyubetuAndUser() {
    	logRule.testStart("CS", "連絡先検索(氏名＆種別＆ユーザー)");
        User user = data.getUser("tosi");
        List<Contact> contacts = 
        		contactRepo.findBySimeiSeiAndSimeiMeiAndSyubetuAndUser(
        				"山田", "花子", "家族親戚", user);
        util.logAllContact(contacts, "連絡先取得(tosi,家族親戚,山田花子)");
        assertThat(contacts.size()).isEqualTo(1);
        assertThat(contacts.get(0).getSimeiSei()).isEqualTo("山田");
        assertThat(contacts.get(0).getSimeiMei()).isEqualTo("花子");
    }

    @Test
    public void contactFindAllByUserAndSyubetuLike() {
    	logRule.testStart("CS", "連絡先曖昧検索(ユーザー＆種別＆検索文字列)");
        User user = data.getUser("tosi");
        List<Contact> contacts = contactRepo.findAll(user, "家族親戚", "%花%");
        util.logAllContact(contacts, "連絡先取得(tosi,家族親戚,%花%)");
        assertThat(contacts.size()).isEqualTo(3);
    }

    @Test
    public void saveContact() {
    	logRule.testStart("CA", "yumi ユーザーに連絡先を新規追加");
        User user = data.getUser("yumi");
        Contact c = new Contact(user, "新規", "追加", "しんき", "ついか", 
        		"女", "1980/01/01", "家族親戚");
        c.getAddresses().add(
        		new Address("1234444", "東京都", "千代田区", "", "", "", "自宅"));
        c.getMails().add(new Mail("t.sinki@xmail.com", "自宅"));
        c.getMails().add(new Mail("t.sinki@xxx.xx.com", "携帯"));
        c.getPhones().add(new Phone("09011112222", "携帯"));
        c = contactRepo.save(c);
        assertThat(c.getId()).isNotEqualTo(null);
        
        // yumi, "家族・親戚" の登録件数は現在 0件 なので 1件 になる
        List<Contact> contacts = contactRepo.findAll(user, "家族親戚");
        util.logAllContact(contacts, "連絡先保存(yumi,家族親戚,新規追加");
        assertThat(contacts.size()).isEqualTo(1);
    }

    @Test
    public void updateContact01() {
    	logRule.testStart("CU", "連絡先の項目(生年月日,職種,所属)の修正");
        User user = data.getUser("tosi");
        List<Contact> contacts = 
        		contactRepo.findBySimeiSeiAndSimeiMeiAndSyubetuAndUser(
                "相田", "陽子", "家族親戚", user);
        util.logAllContact(contacts, "連絡先更新前(tosi,家族親戚,相田陽子)");
        assertThat(contacts.size()).isEqualTo(1);
        Contact c = contacts.get(0);
        assertThat(c.getSeinenGappi()).isEqualTo("1980/01/01");
        assertThat(c.getKaisya()).isEqualTo("");
        //項目を更新
        c.setSeinenGappi("1980/10/10");
        c.setKaisya("会社更新");
        Long id = c.getId();
        contactRepo.save(c);
        // 更新のチェック
        c = contactRepo.findOne(id);
        assertThat(c.getSeinenGappi()).isEqualTo("1980/10/10");
        assertThat(c.getKaisya()).isEqualTo("会社更新");
        util.logAllContact(contacts, "連絡先更新後");

    }

    @Test
    public void updateContact02() {
    	logRule.testStart("CU", "連絡先の住所(自宅)、メール(自宅)の追加");
        User user = data.getUser("yumi");
        List<Contact> contacts = 
        		contactRepo.findBySimeiSeiAndSimeiMeiAndSyubetuAndUser(
                "岡本", "太郎", "友人知人", user);
        util.logAllContact(contacts, "連絡先更新前(yumi,友人知人,岡本太郎)");
        assertThat(contacts.size()).isEqualTo(1);
        Contact c = contacts.get(0);
        assertThat(c.getAddresses().size()).isEqualTo(0);
        assertThat(c.getMails().size()).isEqualTo(0);
        // 住所とメールを追加
        c.getAddresses().add(
        		new Address("3335555", "東京都", "大田区", "", "", "", "自宅"));
        c.getMails().add(new Mail("t.sinki@ssmail.com", "自宅"));
        contactRepo.save(c);
        // 更新後のチェック
        contacts = contactRepo.findBySimeiSeiAndSimeiMeiAndSyubetuAndUser(
                "岡本", "太郎", "友人知人", user);
        assertThat(contacts.size()).isEqualTo(1);
        c = contacts.get(0);
        assertThat(c.getAddresses().size()).isEqualTo(1);
        assertThat(c.getAddresses().get(0).getYubin()).isEqualTo("3335555");
        assertThat(c.getAddresses().get(0).getTodofuken()).isEqualTo("東京都");
        assertThat(c.getMails().size()).isEqualTo(1);
        assertThat(c.getMails().get(0).getMail()).isEqualTo("t.sinki@ssmail.com");
        util.logAllContact(contacts, "連絡先更新後");
    }

    @Test
    public void updateContact03() {
    	logRule.testStart("CU", "連絡先の住所項目(郵便番号)の修正");
        User user = data.getUser("tosi");
        List<Contact> contacts = 
        		contactRepo.findBySimeiSeiAndSimeiMeiAndSyubetuAndUser(
                "相田", "陽子", "家族親戚", user);
        util.logAllContact(contacts, "連絡先更新前(tosi,家族親戚,相田陽子");
        assertThat(contacts.size()).isEqualTo(1);
        Contact c = contacts.get(0);
        assertThat(c.getAddresses().size()).isEqualTo(2);
        assertThat(c.getMails().size()).isEqualTo(1);
        assertThat(c.getPhones().size()).isEqualTo(1);
        assertThat(c.getAddresses().get(0).getYubin()).isEqualTo("1234444");
        // 住所を修正
        c.getAddresses().get(0).setYubin("1239999");
        contactRepo.save(c);
        // 更新後のチェック
        contacts = contactRepo.findBySimeiSeiAndSimeiMeiAndSyubetuAndUser(
                        "相田", "陽子", "家族親戚", user);
        assertThat(contacts.size()).isEqualTo(1);
        c = contacts.get(0);
        assertThat(c.getAddresses().size()).isEqualTo(2);
        assertThat(c.getMails().size()).isEqualTo(1);
        assertThat(c.getPhones().size()).isEqualTo(1);
        assertThat(c.getAddresses().get(0).getYubin()).isEqualTo("1239999");
        util.logAllContact(contacts, "連絡先更新");
   }

    @Test
    public void updateContact04() {
    	logRule.testStart("CU", "連絡先の住所(自宅)の削除");
        User user = data.getUser("tosi");
        List<Contact> contacts = 
        		contactRepo.findBySimeiSeiAndSimeiMeiAndSyubetuAndUser(
                "相田", "陽子", "家族親戚", user);
        util.logAllContact(contacts, "連絡先更新前(tosi,家族親戚,相田陽子");
        assertThat(contacts.size()).isEqualTo(1);
        Contact c = contacts.get(0);
        assertThat(c.getAddresses().size()).isEqualTo(2);
        assertThat(c.getMails().size()).isEqualTo(1);
        assertThat(c.getPhones().size()).isEqualTo(1);
        assertThat(c.getAddresses().get(0).getYubin()).isEqualTo("1234444");
        // 住所を削除
        c.getAddresses().remove(0);
        contactRepo.save(c);
        // 更新後のチェック
        contacts = contactRepo.findBySimeiSeiAndSimeiMeiAndSyubetuAndUser(
                "相田", "陽子", "家族親戚", user);
        assertThat(contacts.size()).isEqualTo(1);
        c = contacts.get(0);
        assertThat(c.getAddresses().size()).isEqualTo(1);
        assertThat(c.getMails().size()).isEqualTo(1);
        assertThat(c.getPhones().size()).isEqualTo(1);
        util.logAllContact(contacts, "連絡先更新後");
    }

    @Test
    public void deleteContact() {
    	logRule.testStart("CD", "連絡先(山田花子)削除");
        User user = data.getUser("tosi");
        List<Contact> contacts = contactRepo.findAll(user, "家族親戚");
        assertThat(contacts.size()).isEqualTo(data.getTosiKazokuSinsekiKensu());
        util.logAllContact(contacts, "連絡先削除前(tosi,家族親戚)");
        contacts = contactRepo.findBySimeiSeiAndSimeiMeiAndSyubetuAndUser(
                "山田", "花子", "家族親戚", user);
        util.logAllContact(contacts, "連絡先削除対象(tosi,家族親戚,山田花子)");
        assertThat(contacts.size()).isEqualTo(1);
        Contact c = contacts.get(0);
        contactRepo.delete(c);
        // 削除後のチェック
        contacts = contactRepo.findBySimeiSeiAndSimeiMeiAndSyubetuAndUser(
                "山田", "花子", "家族親戚", user);
        assertThat(contacts.size()).isEqualTo(0);
        util.logAllContact(contacts, "連絡先削除後(tosi,家族親戚,山田花子)");
   }

    @Test
    public void validMail() {
        //log.info("MV-########　メールバリデーション：OK");
        Mail m = new Mail("t.tamcho@gmail.com", "自宅");
        assertThat(validator.validate(m).size()).isEqualTo(0);
    }

    @Test
    public void validPhone() {
        //log.info("PV-######## 電話バリデーション：OK");
        Phone p = new Phone("09022223333", "自宅");
        assertThat(validator.validate(p).size()).isEqualTo(0);
    }
    
    @Test
    public void testInvalidMail_Email() {
        Mail m = new Mail("xxxxx", "自宅"); //メールアドレスが不正
        Set<ConstraintViolation<Mail>> violations = validator.validate(m);
        assertThat(violations.size()).isEqualTo(1);
        ConstraintViolation<Mail> cv = violations.iterator().next();
        assertThat(cv.getMessage()).isEqualTo("メールアドレスが不正です");
        assertThat(cv.getInvalidValue()).isEqualTo("xxxxx");
    }
    @Test
    public void testInvalidMail_MailType() {
        Mail m = new Mail("tt@gmail.com", "");	//メール種別が空
        Set<ConstraintViolation<Mail>> violations = validator.validate(m);
        assertThat(violations.size()).isEqualTo(1);
        ConstraintViolation<Mail> cv = violations.iterator().next();
        assertThat(cv.getMessage()).isEqualTo("メール種別を指定してください");
        assertThat(cv.getInvalidValue()).isEqualTo("");
    }

    @Test
    public void testInvalidPhone_TelNo() {
        // @Pattern(regexp = "\\d{2,4}\\d{2,4}\\d{4}") String telno;
        Phone p = new Phone("09011xxx", "自宅");
        Set<ConstraintViolation<Phone>> violations = validator.validate(p);
        assertThat(violations.size()).isEqualTo(1);
        ConstraintViolation<Phone> cv = violations.iterator().next();
        assertThat(cv.getMessage()).isEqualTo("電話番号が不正です");
        assertThat(cv.getMessageTemplate()).isEqualTo("{emsg.telno}");
        assertThat(cv.getInvalidValue()).isEqualTo("09011xxx");
    }
    @Test
    public void testInvalidPhone_PhoneType() {
        Phone p = new Phone("09011112222", "");	//連絡先種別が空
        Set<ConstraintViolation<Phone>> violations = validator.validate(p);
        assertThat(violations.size()).isEqualTo(1);
        ConstraintViolation<Phone> cv = violations.iterator().next();
        assertThat(cv.getMessage()).isEqualTo("電話種別を指定してください");
        assertThat(cv.getMessageTemplate()).isEqualTo("{emsg.phoneType.hissu}");
        assertThat(cv.getInvalidValue()).isEqualTo("");
    }
}
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
import org.junit.rules.ExpectedException;
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

/**
 * 連絡先リポジトリのテスト.
 * リポジトリのテストを中心に行う。
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
    private TestData data;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private Validator validator;

    @Before
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        log.debug("$$$$$$ テストの初期処理： ユーザー(2件), 連絡先(9件)を作成");
        data.create();
    }

    @Test
    public void testUserFindAll() {
        log.debug("########## ユーザー全件取得テスト");
        List<User> ulist = userRepo.findAll();
        assertThat(ulist.size()).isEqualTo(2);
        ulist.forEach(u -> {
            log.debug(u.toString());
        });
    }

    @Test
    public void testUserFindByName() {
        log.debug("########## ユーザー:FindByName");
        // findByName
        List<User> users = this.userRepo.findByName("tosi");
        assertThat(users.size()).isEqualTo(1);
        assertThat(users.get(0).getName()).isEqualTo("tosi");
        assertThat(users.get(0).getRoles()).isEqualTo("ADMIN");
    }

    @Test
    public void testContactFindAll() {
        log.debug("########## 連絡先全件取得");
        List<Contact> contacts = contactRepo.findAll();
        logAllContact(contacts);
        assertThat(contacts.size()).isEqualTo(data.getTosiKensu()+data.getYumiKensu());
    }

    @Test
    public void testContactFindAllByUserAndSyubetu() {
        log.debug("########## 連絡先取得:ユーザー＆種別");
        User user = data.getUser("tosi");
        List<Contact> contacts = contactRepo.findAll(user, "家族親戚");
        logAllContact(contacts);
        assertThat(contacts.size()).isEqualTo(data.getTosiKazokuSinsekiKensu());
        
        contacts = contactRepo.findAll(user, "友人知人");
        logAllContact(contacts);
        assertThat(contacts.size()).isEqualTo(data.getTosiYujinTijinKensu());

        user = data.getUser("yumi");
        contacts = contactRepo.findAll(user, "友人知人");
        logAllContact(contacts);
        assertThat(contacts.size()).isEqualTo(data.getYumiYujinTijinKensu());

        contacts = contactRepo.findAll(user, "顧客");
        logAllContact(contacts);
        assertThat(contacts.size()).isEqualTo(data.getYumiKokyakuKensu());

        contacts = contactRepo.findAll(user, "家族親戚");
        logAllContact(contacts);
        assertThat(contacts.size()).isEqualTo(0);
    }
    
    @Test
    public void testContactFindBySimeiAndSyubetuAndUser() {
        log.debug("########## 連絡先取得:氏名、種別、ユーザー");
        User user = data.getUser("tosi");
        List<Contact> contacts = contactRepo.findBySimeiAndSyubetuAndUser("山田花子", "家族親戚", user);
        logAllContact(contacts);
        assertThat(contacts.size()).isEqualTo(1);
        assertThat(contacts.get(0).getSimei()).isEqualTo("山田花子");
    }

    @Test
    public void testContactFindAllByUserAndSyubetuLike() {
        log.debug("########## 連絡先取得:ユーザー＆種別＆検索文字列");
        User user = data.getUser("tosi");
        List<Contact> contacts = contactRepo.findAll(user, "家族親戚", "%山%");
        logAllContact(contacts);
        assertThat(contacts.size()).isEqualTo(2);
    }

    @Test
    public void testSaveContact() {
        log.debug("########## 連絡先保存 yumi ユーザーに連絡先を新規追加");
        User user = data.getUser("yumi");
        Contact c = new Contact(user, "新規追加", "しんきついか", "女", "1980/01/01", "家族親戚");
        c.getAddresses().put("自宅",
                new Address(c, "1234444", "東京都", "千代田区", "", "", "", "自宅"));
        c.getMails().put("自宅", new Mail(c, "t.sinki@xmail.com", "自宅"));
        c.getMails().put("携帯", new Mail(c, "t.sinki@xxx.xx.com", "携帯"));
        c.getPhones().put("携帯", new Phone(c, "09011112222", "携帯"));
        c = contactRepo.save(c);
        assertThat(c.getId()).isNotEqualTo(null);
        
        // yumi, "家族・親戚" の登録件数は現在 0件 なので 1件 になる
        List<Contact> contacts = contactRepo.findAll(user, "家族親戚");
        logAllContact(contacts);
        assertThat(contacts.size()).isEqualTo(1);
    }

    @Test
    public void testUpdateContact01() {
        log.debug("########## 連絡先更新：連絡先の項目の修正");
        User user = data.getUser("tosi");
        List<Contact> contacts = contactRepo.findBySimeiAndSyubetuAndUser(
                "山田花子", "家族親戚", user);
        assertThat(contacts.size()).isEqualTo(1);
        Contact c = contacts.get(0);
        assertThat(c.getSeinenGappi()).isEqualTo("1980/01/01");
        assertThat(c.getSyokusyu()).isEqualTo("");
        assertThat(c.getSyozoku()).isEqualTo("");
        //項目を更新
        c.setSeinenGappi("1980/10/10");
        c.setSyokusyu("SE");
        c.setSyozoku("開発部");
        Long id = c.getId();
        contactRepo.save(c);
        // 更新のチェック
        c = contactRepo.findOne(id);
        assertThat(c.getSeinenGappi()).isEqualTo("1980/10/10");
        assertThat(c.getSyokusyu()).isEqualTo("SE");
        assertThat(c.getSyozoku()).isEqualTo("開発部");
    }

    @Test
    public void testUpdateContact02() {
        log.debug("########## 連絡先更新：住所(自宅)の追加、メール(自宅)の追加");
        User user = data.getUser("yumi");
        List<Contact> contacts = contactRepo.findBySimeiAndSyubetuAndUser(
                "岡本太郎", "友人知人", user);
        assertThat(contacts.size()).isEqualTo(1);
        Contact c = contacts.get(0);
        assertThat(c.getAddresses().size()).isEqualTo(0);
        assertThat(c.getMails().size()).isEqualTo(0);
        // 住所とメールを追加
        c.getAddresses().put("自宅",
                new Address(c, "3335555", "東京都", "大田区", "", "", "", "自宅"));
        c.getMails().put("自宅", new Mail(c, "t.sinki@ssmail.com", "自宅"));
        contactRepo.save(c);
        // 更新後のチェック
        contacts = contactRepo.findAll(user, "友人知人", "%岡本太郎%");
        assertThat(contacts.size()).isEqualTo(1);
        c = contacts.get(0);
        assertThat(c.getAddresses().size()).isEqualTo(1);
        assertThat(c.getAddresses().get("自宅").getYubin()).isEqualTo("3335555");
        assertThat(c.getAddresses().get("自宅").getTodofuken()).isEqualTo("東京都");
        assertThat(c.getMails().size()).isEqualTo(1);
        assertThat(c.getMails().get("自宅").getMail()).isEqualTo("t.sinki@ssmail.com");
    }

    @Test
    public void testUpdateContact03() {
        log.debug("########## 連絡先更新：住所項目の修正");
        User user = data.getUser("tosi");
        List<Contact> contacts = contactRepo.findBySimeiAndSyubetuAndUser(
                "山田花子", "家族親戚", user);
        assertThat(contacts.size()).isEqualTo(1);
        Contact c = contacts.get(0);
        assertThat(c.getAddresses().size()).isEqualTo(2);
        assertThat(c.getMails().size()).isEqualTo(1);
        assertThat(c.getPhones().size()).isEqualTo(1);
        assertThat(c.getAddresses().get("自宅").getYubin()).isEqualTo("1234444");
        // 住所を修正
        c.getAddresses().get("自宅").setYubin("1235555");
        contactRepo.save(c);
        // 更新後のチェック
        contacts = contactRepo.findAll(user, "家族親戚", "%山田花子%");
        assertThat(contacts.size()).isEqualTo(1);
        c = contacts.get(0);
        assertThat(c.getAddresses().size()).isEqualTo(2);
        assertThat(c.getMails().size()).isEqualTo(1);
        assertThat(c.getPhones().size()).isEqualTo(1);
        assertThat(c.getAddresses().get("自宅").getYubin()).isEqualTo("1235555");
    }

    @Test
    public void testUpdateContact04() {
        log.debug("########## 連絡先更新：住所(自宅)の削除");
        User user = data.getUser("tosi");
        List<Contact> contacts = contactRepo.findBySimeiAndSyubetuAndUser(
                "山田花子", "家族親戚", user);
        assertThat(contacts.size()).isEqualTo(1);
        Contact c = contacts.get(0);
        assertThat(c.getAddresses().size()).isEqualTo(2);
        assertThat(c.getMails().size()).isEqualTo(1);
        assertThat(c.getPhones().size()).isEqualTo(1);
        assertThat(c.getAddresses().get("自宅").getYubin()).isEqualTo("1234444");
        // 住所を削除
        c.getAddresses().remove("自宅");
        contactRepo.save(c);
        // 更新後のチェック
        contacts = contactRepo.findAll(user, "家族親戚", "%山田花子%");
        assertThat(contacts.size()).isEqualTo(1);
        c = contacts.get(0);
        assertThat(c.getAddresses().size()).isEqualTo(1);
        assertThat(c.getMails().size()).isEqualTo(1);
        assertThat(c.getPhones().size()).isEqualTo(1);
    }

    @Test
    public void testDeleteContact() {
        log.debug("########## 連絡先削除");
        User user = data.getUser("tosi");
        List<Contact> contacts = contactRepo.findAll(user, "家族親戚");
        assertThat(contacts.size()).isEqualTo(data.getTosiKazokuSinsekiKensu());
        logAllContact(contacts);
        contacts = contactRepo.findBySimeiAndSyubetuAndUser(
                "山田花子", "家族親戚", user);
        logAllContact(contacts);
        assertThat(contacts.size()).isEqualTo(1);
        Contact c = contacts.get(0);
        contactRepo.delete(c);
        // 削除後のチェック
        contacts = contactRepo.findBySimeiAndSyubetuAndUser(
                "山田花子", "家族親戚", user);
        assertThat(contacts.size()).isEqualTo(0);
    }

    @Test
    public void testValidMail() {
        log.debug("##########　メールバリデーション：OK");
        Mail m = new Mail(new Contact(), "t.tamcho@gmail.com", "自宅");
        assertThat(validator.validate(m).size()).isEqualTo(0);
    }

    @Test
    public void testValidPhone() {
        log.debug("########## 電話バリデーション：OK");
        Phone p = new Phone(new Contact(), "09022223333", "自宅");
        assertThat(validator.validate(p).size()).isEqualTo(0);
    }
    
    @Test
    public void testInvalidMail_ContactNull() {
        log.debug("########## メールバリデーション:連絡先がnull");
        //exception.expect(javax.validation.ConstraintViolationException.class);
        // 1つの不正な値をチェック
        // @NotNull Contact contact;
        Mail m = new Mail(null, "tt@gmail.com", "自宅");
        Set<ConstraintViolation<Mail>> violations = validator.validate(m);
        assertThat(violations.size()).isEqualTo(1);
        ConstraintViolation<Mail> cv = violations.iterator().next();
        assertThat(cv.getMessage()).isEqualTo("may not be null");
        assertThat(cv.getMessageTemplate()).isEqualTo("{javax.validation.constraints.NotNull.message}");
        assertThat(cv.getInvalidValue()).isNull();
    }
    @Test
    public void testInvalidMail_Email() {
        log.debug("########## メールバリデーション:メールアドレスの形式が不正");
        //exception.expect(javax.validation.ConstraintViolationException.class);
        // 1つの不正な値をチェック
        // @Email(message = "メールアドレスの形式が正しくありません") String mail
        Mail m = new Mail(new Contact(), "xxxxx", "自宅");
        Set<ConstraintViolation<Mail>> violations = validator.validate(m);
        assertThat(violations.size()).isEqualTo(1);
        ConstraintViolation<Mail> cv = violations.iterator().next();
        assertThat(cv.getMessage()).isEqualTo("メールアドレスの形式が正しくありません");
        assertThat(cv.getInvalidValue()).isEqualTo("xxxxx");
    }
    @Test
    public void testInvalidMail_MailType() {
        log.debug("########## メールバリデーション:メールタイプ");
        //exception.expect(javax.validation.ConstraintViolationException.class);
        // 1つの不正な値をチェック
        // @Size(min = 1, message = "メール種別の入力は必須です") String mailType;
        Mail m = new Mail(new Contact(), "tt@gmail.com", "");
        Set<ConstraintViolation<Mail>> violations = validator.validate(m);
        assertThat(violations.size()).isEqualTo(1);
        ConstraintViolation<Mail> cv = violations.iterator().next();
        assertThat(cv.getMessage()).isEqualTo("メール種別の入力は必須です");
        assertThat(cv.getInvalidValue()).isEqualTo("");
    }

    @Test
    public void testInvalidPhone_ContactNull() {
        log.debug("########## 電話バリデーション:連絡先がnull");
        // @NotNull Contact contact;
        Phone p = new Phone(null, "09011112222", "自宅");
        Set<ConstraintViolation<Phone>> violations = validator.validate(p);
        assertThat(violations.size()).isEqualTo(1);
        ConstraintViolation<Phone> cv = violations.iterator().next();
        assertThat(cv.getMessage()).isEqualTo("may not be null");
        assertThat(cv.getMessageTemplate()).isEqualTo("{javax.validation.constraints.NotNull.message}");
        assertThat(cv.getInvalidValue()).isNull();
    }
    @Test
    public void testInvalidPhone_TelNo() {
        log.debug("########## 電話バリデーション");
        // @Pattern(regexp = "\\d{2,4}\\d{2,4}\\d{4}") String telno;
        Phone p = new Phone(new Contact(), "09011xxx", "自宅");
        Set<ConstraintViolation<Phone>> violations = validator.validate(p);
        assertThat(violations.size()).isEqualTo(1);
        ConstraintViolation<Phone> cv = violations.iterator().next();
        assertThat(cv.getMessage()).isEqualTo("パターンにマッチしていません");
        assertThat(cv.getMessageTemplate()).isEqualTo("{javax.validation.constraints.Pattern.message}");
        assertThat(cv.getInvalidValue()).isEqualTo("09011xxx");
    }
    @Test
    public void testInvalidPhone_PhoneType() {
        log.debug("########## 電話バリデーション");
        // @Size(min = 1, message = "{cstm.size.msg}") String phoneType;
        Phone p = new Phone(new Contact(), "09011112222", "");
        Set<ConstraintViolation<Phone>> violations = validator.validate(p);
        assertThat(violations.size()).isEqualTo(1);
        ConstraintViolation<Phone> cv = violations.iterator().next();
        assertThat(cv.getMessage()).isEqualTo("1文字以上の文字列を入力してください");
        assertThat(cv.getMessageTemplate()).isEqualTo("{cstm.size.msg}");
        assertThat(cv.getInvalidValue()).isEqualTo("");
    }

    private void logAllContact(List<Contact> contacts) {
        log.debug("%% 取得連絡先一覧 --------------");
        contacts.forEach(c -> {
            log.debug(c.toString());
            c.getAddresses().forEach((k, m) -> {
                log.debug(">" + k + ":" + m.toString());
            });
            c.getMails().forEach((k, m) -> {
                log.debug(">" + k + ":" + m.toString());
            });
            c.getPhones().forEach((k, m) -> {
                log.debug(">" + k + ":" + m.toString());
            });
        });
        log.debug("-------------------------------");
    }

}
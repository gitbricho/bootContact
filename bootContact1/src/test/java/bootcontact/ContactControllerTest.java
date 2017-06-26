package bootcontact;

import static bootcontact.AppConst.ATTR_CONTACT;
import static bootcontact.AppConst.ATTR_CONTACT_LIST;
import static bootcontact.AppConst.ATTR_CONTACT_SYUBETU_ITEMS;
import static bootcontact.AppConst.ATTR_CUR_SYUBETU;
import static bootcontact.AppConst.ATTR_SEIBETU_ITEMS;
import static bootcontact.AppConst.ATTR_SYUMI_ITEMS;
import static bootcontact.AppConst.C_UPD_SEINEN_GAPPI;
import static bootcontact.AppConst.STS_CRE_OK;
import static bootcontact.AppConst.STS_UPD_OK;
import static bootcontact.AppConst.URL_CONTACT;
import static bootcontact.AppConst.URL_DELETE;
import static bootcontact.AppConst.URL_FORM;
import static bootcontact.AppConst.URL_LIST;
import static bootcontact.AppConst.URL_SAVE;
import static bootcontact.AppConst.URL_SELECT;
import static bootcontact.AppConst.VIEW_CONTACT_DETAIL;
import static bootcontact.AppConst.VIEW_CONTACT_FORM;
import static bootcontact.AppConst.VIEW_CONTACT_LIST;
import static bootcontact.AppConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
//get, post, request
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
//status, view, content, model, forwardedUrl, redirectedUrl, print, crsf
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bootcontact.model.Address;
import bootcontact.model.Contact;
import bootcontact.model.ContactCmd;
import bootcontact.model.Mail;
import bootcontact.model.Phone;
import bootcontact.props.ItemsProps;
import bootcontact.repo.ContactRepository;

//MARK: クラス
/**
 * 連絡先コントローラ(ContactController)のテスト. 
 * <p>コントローラの GETリクエスト、POSTリクエストを処理するメソッドをテストする.
 * セキュリティで保護されたページのテストの前準備と基本的なテスト方法のサンプル. </p>
 * <p>以下の Tips を取り上げる.
 * <ul>
 * <li>@RunWith, @SpringBootTest 注釈の使用 
 * <li>静的 import MockMvcRequestBuilders の get,post, ... 
 * <li>静的 import MockMvcResultMatchers の status, view, model, ... 
 * <li>MockMvc, MvcResult の使い方 
 * <li>ユーザー/パスワード/ロールでのアクセス
 * </ul>
 * </p>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class ContactControllerTest {
    private static final Logger log = LoggerFactory.getLogger(ContactControllerTest.class);
    private static final long CONTACT_ID = 100100; //相田陽子
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private AppData data;
    @Autowired
    private ItemsProps itemProps;
    @Autowired
    private ContactRepository contactRepo;
    @Autowired
    ObjectMapper mapper;
    
    @Autowired
    private MessageSource msc;

    @LocalServerPort
    private int port;
    
    @Rule
    public LogRule logRule = new LogRule();

    private MockMvc mockMvc;

    private MockHttpSession mockHttpSession;
    private Validator validator;
    private TestUtil util;
    
    @Before
    public void setUp() {
        data.create();
        util = new TestUtil(data, contactRepo);
        util.logSetup();

        // テストで使用する MockMvc を作成
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                // .defaultRequest(get("/").with(user("tosi").roles("ADMIN")))
                // Spring セキュリティの適用
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();

        this.mockHttpSession = new MockHttpSession(wac.getServletContext(),
                UUID.randomUUID().toString());
        mockHttpSession.setAttribute("loginUser", data.getUser("tosi"));
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * Javaオブジェクトを JSON文字列に変換
     * @param obj : Javaオブジェクト
     * @return JSON文字列
     */
    private String asJsonString(final Object obj) {
        try {
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }  

    // MARK: テストメソッド
    /** @GetMapping("/list") String showContactList(Model model, Locale locale) */
    @Test
    @WithMockUser(username = "tosi", password = "tosi", roles = "ADMIN")
    public void contactList() throws Exception {
    	logRule.testStart("", "");
        //ログインユーザー="tosi", 連絡先種別="家族親戚" での連絡先一覧
        mockMvc.perform(get(URL_CONTACT + URL_LIST).session(mockHttpSession))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name(VIEW_CONTACT_LIST))
        .andExpect(model().attribute(ATTR_CONTACT_LIST, hasSize(data.getTosiKazokuSinsekiKensu())))
        .andExpect(model().attribute(ATTR_CONTACT_LIST, hasItem(
        	allOf(
        		hasProperty("id", is(100100L)),
        		hasProperty("version", is(1)),
        		hasProperty("simeiSei", is("相田")),
        		hasProperty("simeiMei", is("陽子")),
        		hasProperty("yomiSei", is("あいだ")),
        		hasProperty("yomiMei", is("ようこ")),
        		hasProperty("seibetu", is("女")),
        		hasProperty("seinenGappi", is("1980/01/01")),
        		hasProperty("syubetu", is("家族親戚"))
        	)
        )))
        .andReturn();
    }

    /** @GetMapping("/list/{syu}") ModelAndView showContactList(@PathVariable("syu") String syu) */
    @Test
    @WithMockUser(username = "tosi", password = "tosi", roles = "ADMIN")
    public void contactListByKS() throws Exception {
    	logRule.testStart("", "");
        final String HENKO_SYUBETU = "家族親戚";
        mockMvc.perform(
        	// contacts.html で連絡先種別（家族・親戚）を選択する操作のシミュレーション
        	get(URL_CONTACT + URL_LIST + "/" + HENKO_SYUBETU)
        	.session(mockHttpSession)
        	.characterEncoding("utf8"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name(VIEW_CONTACT_LIST))
        // user='tosi', 連絡先種別='家族・親戚' の連絡先の件数
        .andExpect(model().attribute(ATTR_CONTACT_LIST, 
        		hasSize(data.getTosiKazokuSinsekiKensu())))
        .andExpect(model().attribute(ATTR_CONTACT_SYUBETU_ITEMS, 
        		itemProps.getContactSyubetu()))
        .andExpect(model().attribute(ATTR_CUR_SYUBETU, HENKO_SYUBETU)).andReturn();
    }
    
    @Test
    @WithMockUser(username = "tosi", password = "tosi", roles = "ADMIN")
    public void contactListByYC() throws Exception {
    	logRule.testStart("", "");
        final String HENKO_SYUBETU = "友人知人";
        mockMvc.perform(
        	// contacts.html で連絡先種別（友人・知人）を選択する操作のシミュレーション
            get(URL_CONTACT + URL_LIST + "/" + HENKO_SYUBETU)
            .session(mockHttpSession)
            .characterEncoding("utf8"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name(VIEW_CONTACT_LIST))
        // user='tosi', 連絡先種別='友人・知人' の連絡先は 2件
        .andExpect(model().attribute(ATTR_CONTACT_LIST, hasSize(2)))
        .andExpect(model().attribute(ATTR_CONTACT_SYUBETU_ITEMS, 
        		itemProps.getContactSyubetu()))
        .andExpect(model().attribute(ATTR_CUR_SYUBETU, HENKO_SYUBETU)).andReturn();
    }

    /** @GetMapping("/select/{id}") ModelAndView showContactDetail(@PathVariable("id") Contact contact) */
    @Test
    @WithMockUser(username = "tosi", password = "tosi", roles = "ADMIN")
    public void contactDetail() throws Exception {
    	logRule.testStart("", "");
        // contacts.html で一覧から特定行の「連絡先IDリンク」をクリック
        mockMvc.perform(get(URL_CONTACT + URL_SELECT + "/" + CONTACT_ID))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name(VIEW_CONTACT_DETAIL))
        .andExpect(model().attributeExists(ATTR_CONTACT))
        .andReturn();
    }

    /** @GetMapping("/form") ModelAndView createContact(Contact contact) */
    @Test
    @WithMockUser(username = "tosi", password = "tosi", roles = "ADMIN")
    public void contactFormCreate() throws Exception {
    	logRule.testStart("", "");
        // contacts.html 「新規作成リンク」をクリック
        mockMvc.perform(get(URL_CONTACT + URL_FORM))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name(VIEW_CONTACT_FORM))
        .andExpect(model().attributeExists(ATTR_CONTACT))
        .andReturn();
    }
    
    /** @GetMapping("/form/{id}") ModelAndView updateContact(@PathVariable("id") Contact contact) */
    @Test
    @WithMockUser(username = "tosi", password = "tosi", roles = "ADMIN")
    public void contactFormUpdate() throws Exception {
    	logRule.testStart("", "");
        // contact_view.html で「修正」リンクをクリック
        MvcResult mvcResult = mockMvc.perform(
            get(URL_CONTACT + URL_FORM + "/" + CONTACT_ID))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name(VIEW_CONTACT_FORM))
        .andExpect(model().attribute(ATTR_CONTACT_SYUBETU_ITEMS, 
        		itemProps.getContactSyubetu()))
        .andExpect(model().attribute(ATTR_SEIBETU_ITEMS, itemProps.getSeibetu()))
        .andExpect(model().attribute(ATTR_SYUMI_ITEMS, itemProps.getSyumi()))
        .andExpect(model().attributeExists(ATTR_CONTACT))
        .andExpect(model().attributeExists(
        		"org.springframework.validation.BindingResult.contact"))
        .andReturn();

        // 次のようにしてモデルを取得する方法もある
        Map<String, Object> models = mvcResult.getModelAndView().getModel();
        // printKeys(models.keySet());
        Contact contact = (Contact) models.get(ATTR_CONTACT);
        log.debug(contact.toString());
        assertThat(contact.getSimeiSei()).isEqualTo("相田");
        assertThat(contact.getSimeiMei()).isEqualTo("陽子");
        assertThat(contact.getSeibetu()).isEqualTo("女");
        assertThat(contact.getSeinenGappi()).isEqualTo("1980/01/01");
        /*
         * BindingResult bindingResult = (BindingResult) models
         * .get("org.springframework.validation.BindingResult.contact");
         * log.debug(bindingResult.toString());
         */
    }

    /**
     * Form SUBMIT (新規作成).
     */
    @Test
    public void addContact() throws Exception {
    	logRule.testStart("", "");
        // この POST リクエストの場合は、一時的にモックの csrf対策は無効にする
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        Contact c = new Contact(data.getUser("tosi"),
        	C_ADD_SIMEI_SEI, C_ADD_SIMEI_MEI, C_ADD_YOMI_SEI, C_ADD_YOMI_MEI, 
        	C_ADD_SEIBETU, C_ADD_SEINEN_GAPPI, C_ADD_SYUBETU);
        // 入力検証
        Set<ConstraintViolation<Contact>> violations = validator.validate(c);
        assertThat(violations.size()).isEqualTo(0); //エラーなし
        // form submit のシミュレーション
        MockHttpServletRequestBuilder form = post(URL_CONTACT + URL_SAVE)
        		.characterEncoding("UTF-8")
        		.contentType(MediaType.APPLICATION_FORM_URLENCODED);
        form = util.buildForm(Collections.unmodifiableMap(
        		new HashMap<String, String>() {{
        			put("version", c.getVersion() + "");
        			put("simeiSei", c.getSimeiSei());
        			put("simeiMei", c.getSimeiMei());
        			put("yomiSei", c.getYomiSei());
        			put("yomiMei", c.getYomiMei());
        			put("seibetu", c.getSeibetu());
        			put("seinenGappi", c.getSeinenGappi());
        			put("syubetu", c.getSyubetu());
        			put("user.id", c.getUser().getId() + ""); 
        			}}), form);
          
        mockMvc.perform(form
        .with(user("tosi").password("tosi").roles("ADMIN")))
        .andDo(print())
        // 保存に成功すればリダイレクトされる
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/contact/select/{contact.id}"))
        .andExpect(flash().attribute("globalMessage", 
        		C_ADD_SIMEI_SEI + msc.getMessage("contact.save.ok", null, null)))
        .andReturn();
        // 新規作成が成功したか、DBをサーチして、確認
        Contact ct = util.getContactLikeSimei("%" + C_ADD_SIMEI_SEI + "%");
        assertThat(ct.getSimeiSei()).isEqualTo(C_ADD_SIMEI_SEI);
        assertThat(ct.getSimeiMei()).isEqualTo(C_ADD_SIMEI_MEI);
        assertThat(ct.getYomiSei()).isEqualTo(C_ADD_YOMI_SEI);
        assertThat(ct.getYomiMei()).isEqualTo(C_ADD_YOMI_MEI);
        assertThat(ct.getSeibetu()).isEqualTo(C_ADD_SEIBETU);
        assertThat(ct.getSeinenGappi()).isEqualTo(C_ADD_SEINEN_GAPPI);
    }

    /**
     * Form SUBMIT (新規作成).
     */
    @Test
    public void addContactInvalid() throws Exception {
    	logRule.testStart("", "");
        // この POST リクエストの場合は、一時的にモックの csrf対策は無効にする
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        Contact c = new Contact(data.getUser("tosi"),
            	C_ADD_SIMEI_SEI, C_ADD_SIMEI_MEI, C_ADD_YOMI_SEI, C_ADD_YOMI_MEI, 
            	C_ADD_SEIBETU, C_ADD_SEINEN_GAPPI, null); //種別が空欄
        // 入力検証
        Set<ConstraintViolation<Contact>> violations = validator.validate(c);
        assertThat(violations.size()).isEqualTo(1); //種別は必須なのでエラー
        ConstraintViolation<Contact> cv = violations.iterator().next();
        assertThat(cv.getPropertyPath().toString()).isEqualTo("syubetu");
        assertThat(cv.getMessage()).isEqualTo("連絡先種別は必ず設定してください");
        c.setSyubetu("");
        // form submit のシミュレーション
        MockHttpServletRequestBuilder form = post(URL_CONTACT + URL_SAVE)
        		.characterEncoding("UTF-8")
        		.contentType(MediaType.APPLICATION_FORM_URLENCODED);
        form = util.buildForm(Collections.unmodifiableMap(
        		new HashMap<String, String>() {{
        			put("version", c.getVersion() + "");
        			put("simeiSei", c.getSimeiSei());
        			put("simeiMei", c.getSimeiMei());
        			put("yomiSei", c.getYomiSei());
        			put("yomiMei", c.getYomiMei());
        			put("seibetu", c.getSeibetu());
        			put("seinenGappi", c.getSeinenGappi());
        			put("syubetu", c.getSyubetu());
        			put("user.id", c.getUser().getId() + ""); 
        			}}), form);
   
        mockMvc.perform(form
           .with(user("tosi").password("tosi").roles("ADMIN")))
        .andDo(print())
        //.andExpect(status().isOk())
        //.andExpect(view().name("views/contact_form"))
        .andReturn();
    }

    /**
     * Form SUBMIT (更新).
     */
    @Test
    public void updContact() throws Exception {
    	logRule.testStart("", "");
        // この POST リクエストの場合は、一時的にモックの csrf対策は無効にする
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        // 修正のシミュレーション
        Contact c = util.getEditedContact(CONTACT_ID);
        assertThat(c).isNotNull();
        // form submit のシミュレーション
        MockHttpServletRequestBuilder form = post(URL_CONTACT + URL_SAVE)
        		.characterEncoding("UTF-8")
        		.contentType(MediaType.APPLICATION_FORM_URLENCODED);
        form = util.buildForm(Collections.unmodifiableMap(
        		new HashMap<String, String>() {{
        			//連絡先
        			put("id", c.getId() + "");
        			put("version", c.getVersion() + "");
        			put("simeiSei", c.getSimeiSei());
        			put("simeiMei", c.getSimeiMei());
        			put("yomiSei", c.getYomiSei());
        			put("yomiMei", c.getYomiMei());
        			put("seibetu", c.getSeibetu());
        			put("seinenGappi", c.getSeinenGappi());
        			put("syubetu", c.getSyubetu());
        			put("user.id", c.getUser().getId() + ""); 
        			//住所：自宅
        			Address a = c.getAddresses().get(0);
        			if (a!=null) {
            			put("addresses[0].id", a.getId()+"");
            			put("addresses[0].version", a.getVersion()+"");
            			put("addresses[0].yubin", a.getYubin());
            			put("addresses[0].todofuken", a.getTodofuken());
            			put("addresses[0].sikuchoson", a.getSikuchoson());
            			put("addresses[0].banchi", a.getBanchi());
            			put("addresses[0].banchi2", a.getBanchi2());
            			put("addresses[0].addressType", a.getAddressType());
        			}
        			//メール：自宅
        			Mail m = c.getMails().get(0);
        			if (m!=null) {
            			put("mails[0].id", m.getId()+"");
            			put("mails[0].version", m.getVersion()+"");
            			put("mails[0].mail", m.getMail());
            			put("mails[0].mailType", m.getMailType());
        			}
        			//電話：自宅：
        			Phone ph = c.getPhones().get(0);
        			if (ph!=null) {
            			put("phones[0].id", ph.getId()+"");
            			put("phones[0].version", ph.getVersion()+"");
            			put("phones[0].telno", ph.getTelno());
            			put("phones[0].phoneType", ph.getPhoneType());
        			}
        			}}), form);
 
        mockMvc.perform(form
        .with(user("tosi").password("tosi").roles("ADMIN")))
        .andDo(print())
        // 保存に成功すればリダイレクトする
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/contact/select/{contact.id}"))
        .andExpect(redirectedUrl(URL_CONTACT + URL_SELECT + "/" + CONTACT_ID))
        .andExpect(flash().attribute("globalMessage", C_UPD_SIMEI_SEI + msc.getMessage("contact.save.ok",null,null)))
        .andReturn();
        // 更新が成功したか、DBをサーチして、確認
        Contact ct = contactRepo.findOne(CONTACT_ID);
        assertThat(ct).isNotNull();
        assertThat(ct.getSimeiSei()).isEqualTo(C_UPD_SIMEI_SEI);
        assertThat(ct.getYomiSei()).isEqualTo(C_UPD_YOMI_SEI);
        assertThat(ct.getSeinenGappi()).isEqualTo(C_UPD_SEINEN_GAPPI);
    }
    
    @Test
    public void deleteContact() throws Exception {
    	logRule.testStart("", "");
        mockMvc.perform(get(URL_CONTACT + URL_DELETE + "/" + CONTACT_ID)
            .session(mockHttpSession)
            .with(user("tosi").password("tosi").roles("ADMIN")))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name(VIEW_CONTACT_LIST))
        .andReturn();
        // 更新が成功したか、DBをサーチして、確認
        Contact c = contactRepo.findOne(CONTACT_ID);
        assertThat(c).isNull();
    }
}

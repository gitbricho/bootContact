package bootcontact.controller;

import static bootcontact.AppConst.*;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;

import org.codehaus.groovy.tools.shell.util.ScriptVariableAnalyzer.VisitorSourceOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import bootcontact.model.Address;
import bootcontact.model.Contact;
import bootcontact.model.ContactCmd;
import bootcontact.model.Mail;
import bootcontact.model.Phone;
import bootcontact.model.User;
import bootcontact.props.ContactProp;
import bootcontact.props.ItemsProps;
import bootcontact.repo.ContactRepository;

//MARK: クラス
/**
 * 連絡先コントローラ.
 * <p>次のような、よくある構成のビューをコントロールする例を示す.
 * <ul>
 * <li>一覧ビュー(contact_list.html)</li>
 * <li>詳細ビュー(contact_detail.html):一覧ビューで選択された行の詳細を表示するビュー</li>
 * <li>編集ビュー(contact_form.html):詳細表示された連絡先を編集するビュー</li>
 * </ul>
 * </p>
 * @author bri_tcho
 */
@Controller
@Scope("session")
@RequestMapping(URL_CONTACT)
public class ContactController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    
    // 外部の設定プロパティ、定数、メッセージを取得するため
    @Autowired
    private MessageSource msgSrc;
    @Autowired
    private ContactProp cprops;
    @Autowired
    private ItemsProps itemProps;

    // 連絡先種別（セッション中、値が保持される）
    private String zCurSyubetu = null;
    // ログインユーザー
    private User zLoginUser;

    @Autowired
    private ContactRepository contactRepo;
    @Autowired
    private HttpSession httpSession;
    
    private Validator validator;
    public ContactController() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
    
    /**
     * <p>このメソッドは、リクエストの度に、マッピングメソッド(@RequestMapping 付き
     * のメソッド)が呼ばれる前に実行される.</p>
     * <p>ここではビューに渡すオブジェクトを Model インスタンスに設定する.</p>
     * @param model org.springframework.ui.Model インスタンス.
     */
    @ModelAttribute
    private void setModels(Model model) {
        zLoginUser = (User)httpSession.getAttribute(ATTR_LOGIN_USER);
        // ログインユーザーを model に追加.
        model.addAttribute(ATTR_LOGIN_USER, zLoginUser);
        if (zCurSyubetu == null) {
            // 初回なので、作業変数の初期化を行う
            zCurSyubetu = cprops.getDefaultSyubetu(); 
        }
        // カレントの連絡先種別を model に追加.
        model.addAttribute(ATTR_CUR_SYUBETU, zCurSyubetu);
        
        // 連絡先種別のリスト(List<String>)を model に追加.
        model.addAttribute(ATTR_CONTACT_SYUBETU_ITEMS, itemProps.getContactSyubetu());
        // 性別のリスト.
        model.addAttribute(ATTR_SEIBETU_ITEMS, itemProps.getSeibetu());
        // 趣味のリスト.
        model.addAttribute(ATTR_SYUMI_ITEMS, itemProps.getSyumi());
        // 電話,メール,住所種別
        model.addAttribute(ATTR_PHONE_SYUBETU_ITEMS, itemProps.getPhoneSyubetu());
        model.addAttribute(ATTR_MAIL_SYUBETU_ITEMS, itemProps.getMailSyubetu());
        model.addAttribute(ATTR_JUSYO_SYUBETU_ITEMS, itemProps.getJusyoSyubetu());
    }
    
    //###################
    // helper
    private String getMessage(String prop, Locale locale) {
        return msgSrc.getMessage(prop, null, locale);
    }
    
    // MARK: マップメソッド
    /**
     * 連絡先一覧表示.
     * <p>ログインユーザーの現在の選択種別の連絡先のリストを取得して、一覧ビューを表示する.</p>
     * @param model org.springframework.ui.Model インスタンス.
     * @return ビュー名
     */
    //@GetMapping(URL_LIST)
    @RequestMapping(value=URL_LIST, method=RequestMethod.GET)
    public String showContactList(Model model, Locale locale) {
        // ログインユーザー、現 連絡先種別で連絡先を取得
        List<Contact> contacts = contactRepo.findAll(zLoginUser, zCurSyubetu);
        log.info("### size"+contacts.size());
        contacts.forEach(c->{
        	log.info(c.toString());
        });
        // このリクエスト特有の情報（連絡先一覧）をmodel に追加
        model.addAttribute(ATTR_CONTACT_LIST, contacts);
        return VIEW_CONTACT_LIST;
    }

    /**
     * 連絡先種別の変更.
     * <p>一覧ビューでグループ（連絡先種別）の選択が行われた場合に、このメソッドが呼ばれる.</p>
     * @param syu　変更する連絡先種別
     * @return ModelAndView インスタンス
     */
    @GetMapping(URL_LIST + "/{syu}")
    public ModelAndView showContactList(@PathVariable("syu") String syu) {
        // 新しく選択された連絡先種別をセッション変数へ保存
        this.zCurSyubetu = syu;
        // setModelsで設定した curGroupを上書き
        ModelAndView mav = new ModelAndView(VIEW_CONTACT_LIST, ATTR_CUR_SYUBETU, zCurSyubetu);
        // ログインユーザー、変更された連絡先種別で連絡先を取得
        mav.addObject(ATTR_CONTACT_LIST, contactRepo.findAll(zLoginUser, zCurSyubetu));
        return mav;
    }

    /**
     * テーマの変更.
     * @param theme 変更するテーマ
     * @param model org.springframework.ui.Model インスタンス.
     * @return ビュー名
     */
    @GetMapping(URL_THEME)
    public String changeTheme(@PathVariable("theme") String theme, Model model) {
        // 新しく選択されたテーマをセッション変数へ保存
    	log.info("them={}", theme);
        httpSession.setAttribute(ATTR_CUR_THEME, theme.equals("_default")
        		? "" : theme);

        model.addAttribute(ATTR_CUR_SYUBETU, zCurSyubetu);
        //model.addAttribute(ATTR_CONTACT_LIST, contactRepo.findAll(zLoginUser, zCurSyubetu));
        //return VIEW_CONTACT_LIST;
        return VIEW_HOME;
    }


    /**
     * 連絡先の詳細表示.
     * <p>一覧ビューで、ある行の IDリンクがクリックされたときに呼ばれる.</p>
     * @param contact 選択された連絡先
     * @return ModelAndView インスタンス
     */
    @GetMapping(URL_SELECT + "/{id}")
    public ModelAndView showContactDetail(@PathVariable("id") Contact contact) {
        log.debug("$$$$$ "+contact.toString());
        return new ModelAndView(VIEW_CONTACT_DETAIL, ATTR_CONTACT, contact);
    }

    /**
     * 連絡先新規追加フォームの表示(CREATE).
     * <p>一覧ビューで、「連絡先の追加」ボタンが押された場合に呼ばれる.</p>
     * 
     * @param contact (@ModelAttributeを付けなくてもOK)
     * @return ModelAndView インスタンス
     */
    @GetMapping(URL_FORM)
    public ModelAndView createContact(Contact contact) {
        // 連絡先編集画面では連絡先種別は入力できないので、カレントの連絡先種別を設定
        contact.setUser(zLoginUser);
        log.debug("$$$$$ {}", contact);
        return new ModelAndView(VIEW_CONTACT_FORM, ATTR_CONTACT, contact);
    }
    
    /**
     * 連絡先の編集フォームの表示(READ/SELECT).
     * <p>詳細ビューで「修正」ボタンが押された場合に呼ばれる.</p>
     * 
     * @param contact : 詳細ビューに表示された連絡先
     * @return ModelAndView インスタンス
     */
    @GetMapping(URL_FORM + "/{id}")
    public ModelAndView updateContact(@PathVariable("id") Contact contact) {
        log.debug("$$$$$ "+contact.toString());
        return new ModelAndView(VIEW_CONTACT_FORM, ATTR_CONTACT, contact);
    }

    /**
     * 連絡先の保存.
     * 編集フォーム/新規追加フォームで、「保存」ボタンが押された場合に呼ばれる.
     * 
     * @param contact 編集された連絡先
     * @param result 
     * @return ModelAndView インスタンス
     */
    @PostMapping(URL_SAVE)
    public ModelAndView  saveContact(@Valid Contact contact, BindingResult result,
            RedirectAttributes redirect, Locale locale) {
        log.debug("$$$$$ "+contact.toString());
        for (int i=0; i < contact.getPhones().size(); i++) {
        	Phone p = contact.getPhones().get(i);
            Set<ConstraintViolation<Phone>> violations = validator.validate(p);
        	for (ConstraintViolation<Phone> cv: violations) {
        		String path = "phones[" + i + "]." + cv.getPropertyPath().toString();
        		String msg = cv.getMessage() + " : '" + cv.getInvalidValue() + "'";
            	result.rejectValue(path, null, msg);
        	}
        }
        for (int i=0; i < contact.getMails().size(); i++) {
        	Mail m = contact.getMails().get(i);
            Set<ConstraintViolation<Mail>> violations = validator.validate(m);
        	for (ConstraintViolation<Mail> cv: violations) {
        		String path = "mails[" + i + "]." + cv.getPropertyPath().toString();
        		String msg = cv.getMessage() + " : '" + cv.getInvalidValue() + "'";
            	result.rejectValue(path, null, msg);
        	}
        }
        for (int i=0; i < contact.getAddresses().size(); i++) {
        	Address a = contact.getAddresses().get(i);
            Set<ConstraintViolation<Address>> violations = validator.validate(a);
        	for (ConstraintViolation<Address> cv: violations) {
        		String path = "addresses[" + i + "]." + cv.getPropertyPath().toString();
        		String msg = cv.getMessage() + " : '" + cv.getInvalidValue() + "'";
            	result.rejectValue(path, null, msg);
        	}
        }
        if (result.hasErrors()) {
            result.getAllErrors().forEach(err->{
            	log.info("name={}, msg={}, code={}",
            			err.getObjectName(),
            			err.getDefaultMessage(),
            			err.getCode());
            });
            // エラーがあれば編集フォームへ戻る
            //result.reject("入力エラー", getMessage(MSG_VALID_ERR, locale));
            return new ModelAndView(VIEW_CONTACT_FORM, ATTR_CONTACT, contact);
        }
        //エラーがなければ保存して詳細ビューへ. 保存前にカレント連絡先種別を設定.
        contact.setSyubetu(zCurSyubetu);
        contact = contactRepo.save(contact);
        redirect.addFlashAttribute("globalMessage", contact.getSimeiSei() + getMessage(MSG_SAVE_OK, locale));
        return new ModelAndView("redirect:/contact/select/{contact.id}", ATTR_CONTACT_ID, contact.getId());        
    }

    /**
     * 連絡先の削除(DELETE).
     * <p>詳細ビューで、「削除」ボタンが押された場合に呼ばれる.
     * 削除処理の後、連絡先一覧ビューを表示する.</p>
     * 
     * @param id 削除する連絡先のID
     * @return ModelAndView インスタンス
     */
    @GetMapping(URL_DELETE + "/{id}")
    public ModelAndView deleteContact(@PathVariable("id") Long id) {
        log.debug("$$$$$ 削除する id="+id);
        this.contactRepo.delete(id);
        List<Contact> contacts = contactRepo.findAll(zLoginUser, zCurSyubetu);
        return new ModelAndView(VIEW_CONTACT_LIST, ATTR_CONTACT_LIST, contacts);
    }
}

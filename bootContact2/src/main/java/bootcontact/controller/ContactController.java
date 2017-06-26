package bootcontact.controller;


import static bootcontact.AppConst.*;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import bootcontact.model.Contact;
import bootcontact.model.ContactCmd;
import bootcontact.model.User;
import bootcontact.props.ContactProp;
import bootcontact.props.ItemsProps;
import bootcontact.repo.ContactRepository;

/**
 * 単一の画面（3グリッド構成）で以下の機能を提供します。
 * <p>
 * +---------------------+
 * | グリッド1            +
 * +---------------------+
 * | グリッド2 | グリッド3 |
 * +---------------------+
 * </p>
 * <ul>
 * <li>グリッド1: 連絡先種別選択：（家族・親戚、友人・知人、同僚、仕事、生活）</br>
 * 　ユーザーが連絡先種別リストから種別を選択すると、選択された種別の連絡先一覧を表示。
 * <li>グリッド2：ログインユーザーの選択種別の連絡先一覧を表示。</br>
 * （検索）：検索フィールドに入力された文字列で、氏名、よみに対するあいまい検索を実行。</br>
 * （削除）：選択行の連絡先データを削除（削除確認は行わない）。</br>
 * <li>グリッド3：連絡先一覧で選択された行の詳細情報を表示。</br>
 * （追加）：「追加」ボタンを押すと、連絡先情報の入力画面を表示。</br>
 * （編集）：「編集」ボタンを押すと、表示中の連絡先の編集画面を表示。</br>
 * （保存）：「保存」ボタンを押すと、編集中の連絡先を保存。</br>
 * </ul>
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
    // 該当ページの連絡先一覧を保持
    private List<Contact> zPageContactList;
    // カレントページの番号(0オリジン)
    private int zCurPageNo;
    // 選択行の番号(0..ページ表示件数-1)
    private int zCurRowNo;
    // ページ表示件数
    private int zPageSize = 10;
    // ログインユーザー、現連絡先種別でのページ数
    private long zPageCount;
    // ログインユーザー、現連絡先種別でのページ数
    private long zContactCount;
    // 選択行の連絡先ID
    private long zSelectContactId;

    @Autowired
    private ContactRepository contactRepo;
    @Autowired
    private HttpSession httpSession;
    
    // リクエストの度に、リクエストを処理する前に実行される
    @ModelAttribute
    private void setModels(Model model) {
        zLoginUser = (User)httpSession.getAttribute(ATTR_LOGIN_USER);
        model.addAttribute(ATTR_LOGIN_USER, zLoginUser);
        if (zCurSyubetu == null) {
            // 初回なので、作業変数の初期化を行う
            zPageSize = cprops.getPageSize();
            zCurSyubetu = cprops.getDefaultSyubetu();
            log.debug("pageSize="+zPageSize+",syubetu="+zCurSyubetu);
            init(model, 0, 0, contactRepo.countContact(zLoginUser, zCurSyubetu));
        }
        model.addAttribute(ATTR_CUR_SYUBETU, zCurSyubetu);
        // ビューに渡すモデルやコントローラの情報を modelに追加する
        model.addAttribute(ATTR_CONTACT_SYUBETU_ITEMS, itemProps.getContactSyubetu());
        model.addAttribute(ATTR_SEIBETU_ITEMS, itemProps.getSeibetu());
        model.addAttribute(ATTR_SYUMI_ITEMS, itemProps.getSyumi());
    }
    
    /**
     * 連絡先一覧表示.
     * ログインユーザーの現在の選択種別の連絡先のリストを取得して、一覧ビューを表示する.
     * @param model
     * @return
     */
    @GetMapping(URL_LIST)
    public String showContact(Model model) {
        loadContactList();
        log.debug(getLogStr());
        setCurent(model);
        //デフォルトのテーマ、連絡先種別で、連絡先管理ビューを表示す
        return VIEW_CONTACT;
    }
    
    /**
     * 連絡先種別の変更.
     * 一覧ビューでグループ（連絡先種別）の選択が行われた場合に、このメソッドが呼ばれる.
     * @param syu
     * @return
     */
    @GetMapping(URL_SYUBETU)
    public String showContact(@PathVariable("syu") String syu, Model model) {
        // 新しく選択された連絡先種別をセッション変数へ保存
        zCurSyubetu = syu;
        model.addAttribute(ATTR_CUR_SYUBETU, zCurSyubetu);
        // 種別が変われば ページ番号=0 (先頭ページ)、行番号=0 (先頭行) になる
        init(model, 0, 0, contactRepo.countContact(zLoginUser, zCurSyubetu));
        loadContactList();
        log.debug(getLogStr());
        setCurent(model);
        
        //setModel(model, zCurSyubetu, )
        return VIEW_CONTACT;
    }

    /**
     * テーマを変更する
     * @param theme
     * @param model
     * @return ビュー名
     */
    @GetMapping(URL_THEME)
    public String changeTheme(@PathVariable("theme") String theme, Model model) {
        // 新しく選択されたテーマをセッション変数へ保存
        httpSession.setAttribute(ATTR_CUR_THEME, theme.equals("_default")
        		? "" : theme);
        // その他のセッション変数は変更されない
        log.debug(getLogStr());
        setCurent(model);
        return VIEW_CONTACT;
    }
    
    /**
     * 前のページへ移動.
     * @param model
     * @return ビュー名
     */
    @GetMapping(URL_PREV)
    public String prevPage(Model model) {
        if (zCurPageNo > 0) {
            zCurPageNo--;
            zCurRowNo = 0;
        }
        loadContactList();
        log.debug(getLogStr());
        setCurent(model);
        return VIEW_CONTACT;
    }
    
    /**
     * 次のページへ移動. 
     * @param model
     * @return ビュー名
     */
    @GetMapping(URL_NEXT)
    public String nextPage(Model model) {
        if (zCurPageNo < zPageCount-1) {
            zCurPageNo++;
            zCurRowNo = 0;
        }
        loadContactList();
        log.debug(getLogStr());
        setCurent(model);
        return VIEW_CONTACT;
    }
    
    /**
     * 検索結果を表示.
     * @param model
     * @return ビュー名
     */
    @GetMapping(URL_SEARCH)
    public String serchContact(Model model) {
        //条件で検索
        
        return VIEW_CONTACT;
    }

    /**
     * 連絡先の保存.
     * 編集フォーム/新規追加フォームで、「保存」ボタンが押された場合に呼ばれる.
     * 
     * @param contact 編集された連絡先
     * @param result 
     * @return ビュー名
     */
    @PostMapping(URL_SAVE)
    public String  saveContact(@Valid Contact contact, BindingResult result,
            RedirectAttributes redirect, Locale locale, Model model) {
        log.debug("$$$$$ "+contact.toString());
        boolean createMode = (contact.getId()==null);
        if (result.hasErrors()) {
            result.getAllErrors().forEach(o->{
                log.info(o.getDefaultMessage());
            });
            // エラーがあれば編集フォームへ戻る
            result.reject("入力エラー", getMessage(MSG_VALID_ERR, locale));
            setCurent(model);
            return VIEW_CONTACT;
        }
        //エラーがなければ保存して連絡先管理ビューへ
        contact.setSyubetu(zCurSyubetu);
        contact = contactRepo.save(contact);
        redirect.addFlashAttribute("globalMessage", contact.getSimei() + getMessage(MSG_SAVE_OK, locale));
        if (createMode) {
            zCurPageNo = (int)calcPageCount(zPageSize, ++zContactCount)-1;
            zPageContactList = contactRepo.findAll(zLoginUser, zCurSyubetu, new PageRequest(zCurPageNo, zPageSize));
            zCurRowNo = zPageContactList.size()-1;
        } else {
            for (int idx = 0; idx < zPageContactList.size(); idx++) {
                if (zPageContactList.get(idx).getId()==zSelectContactId) {
                    zCurRowNo = idx;
                    break;
                }
            }
        }
        loadContactList();
        log.info("CREATE_MODE="+createMode+" pageNo="+ zCurPageNo + " rowNo=" + zCurRowNo + " id=" + zSelectContactId);
        setCurent(model);
        return VIEW_CONTACT;
    }

    /**
     * 現在洗濯中の連絡先を削除.
     * @param model
     * @return
     */
    @GetMapping(URL_DELETE)
    public String deleteContact(Model model) {
        log.debug("$$$$$ 削除する id="+zSelectContactId);
        // 選択行を削除.
        contactRepo.delete(zSelectContactId);
        // 削除後のページ数を求める.
        zPageCount = calcPageCount(zPageSize, --zContactCount);
        if (zCurPageNo>=zPageCount && zPageCount>0) {
        	// 新しいページ数に合わせてページ番号と行番号を更新.
            zCurPageNo--;
            zCurRowNo=zPageSize-1;
        }
        // 削除後のカレントページの連絡先リストを取得.
        loadContactList();
        /*
        if (zPageContactList.size()<=zCurRowNo) {
            zCurRowNo--;
        }*/
        // カレントの値をモデルに登録.
        setCurent(model);
        return VIEW_CONTACT;
    }
    
    /**
     * 連絡先一覧テーブルのクリック行の連絡先を選択.
     * @param rowNo
     * @return
     */
    @GetMapping(URL_SELECT_ROW)
    @ResponseBody
    public ContactCmd selectContact(@PathVariable("rowNo") Integer rowNo) {
    	log.info("$$$$$$ rowNo={}", rowNo);
    	if (zPageContactList.size() > rowNo) {
    		//連絡先一覧ビューで行を選択すれば必ずこの条件は満たされる.
    		Contact c = zPageContactList.get(rowNo);
    		zCurRowNo = rowNo;
    		zSelectContactId = c.getId();
            return new ContactCmd(c);
    	} else {
    		return null;
    	}
    }
    
    //###################
    // helper
    private String getMessage(String prop, Locale locale) {
        return msgSrc.getMessage(prop, null, locale);
    }
    // ログ出力のメッセージを返す.
    private String getLogStr() {
        return "$$$$$ loginUser=" + zLoginUser.getName() + ", zCurSyubetu=" + zCurSyubetu
                + ", pageNo=" + zCurPageNo + ", rowNo=" + zCurRowNo + ", pageCount=" + zPageCount;
    }
    
    // VIEW_CONTACT が表示のために必要とする情報を Model に追加する.
    private void setCurent(Model model) {
        model.addAttribute(ATTR_CONTACT_LIST, zPageContactList);
        if (zContactCount == 0) {
        	model.addAttribute(ATTR_CONTACT, null);
        } else {
        	model.addAttribute(ATTR_CONTACT, zPageContactList.get(zCurRowNo));
        	log.info("####### contact.id={}", zPageContactList.get(zCurRowNo).getId());
        }
        model.addAttribute(ATTR_CUR_PAGE_NO, zCurPageNo);
        model.addAttribute(ATTR_PAGE_COUNT, zPageCount);
        model.addAttribute(ATTR_CUR_ROW_NO, zCurRowNo);
    }
    
    // セッション中に保持される作業変数を初期化する
    private void init(Model model, int pageNo, int rowNo, long contactCount) {
        //連絡先全件数
        zContactCount = contactCount;
        //ページ数
        zPageCount = calcPageCount(zPageSize, zContactCount);
        //カレントページ番号
        zCurPageNo = pageNo;
        //カレント行番号
        zCurRowNo = rowNo;
    }
    //該当ページの連絡先一覧を取得する.
    private void loadContactList() {
        //## 該当ページの連絡先一覧を取得.
        zPageContactList = contactRepo.findAll(zLoginUser, zCurSyubetu, new PageRequest(zCurPageNo, zPageSize));
        //## カレント行の連絡先IDを保存.
        // カレントページの連絡先件数を取得.
        int rowCountOfPage = zPageContactList.size();
        if (rowCountOfPage > 0) {
        	// 登録データが存在する.
        	if (rowCountOfPage-1 <= zCurRowNo) {
        		// 削除行がページの最終行だった.
            	zCurRowNo = rowCountOfPage - 1;
        	}
            zSelectContactId = zPageContactList.get(zCurRowNo).getId();
        }
    }
    // ログインユーザー、連絡先種別の連絡先一覧の件数と1ページ当たりの表示件数からページ数を求める.
    private long calcPageCount(int pageSize, long contactCount) {
        long pageCount = 0;
        if (contactCount > 0) {
            pageCount = (contactCount-1) / pageSize + 1;
        }
        return pageCount;
    }
}

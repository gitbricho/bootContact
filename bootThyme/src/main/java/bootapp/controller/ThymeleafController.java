package bootapp.controller;

import static bootapp.AppConst.ATTR_CONTACT;
import static bootapp.AppConst.ATTR_CONTACT_SYUBETU_ITEMS;
import static bootapp.AppConst.ATTR_CUR_SYUBETU;
import static bootapp.AppConst.ATTR_KEKKA;
import static bootapp.AppConst.ATTR_SEIBETU_ITEMS;
import static bootapp.AppConst.ATTR_SYUMI_ITEMS;
import static bootapp.AppConst.URL_FRAG_FIELD01;
import static bootapp.AppConst.URL_THYME;
import static bootapp.AppConst.URL_TH_CONDITION;
import static bootapp.AppConst.URL_TH_EXPRESSIONS;
import static bootapp.AppConst.URL_TH_INLINE;
import static bootapp.AppConst.URL_TH_ITERATION;
import static bootapp.AppConst.URL_TH_LITERAL;
import static bootapp.AppConst.VIEW_FRAG_FIELD01;
import static bootapp.AppConst.VIEW_THYME_KEKKA;
import static bootapp.AppConst.VIEW_TH_CONDITION;
import static bootapp.AppConst.VIEW_TH_EXPRESSIONS;
import static bootapp.AppConst.VIEW_TH_INLINE;
import static bootapp.AppConst.VIEW_TH_ITERATION;
import static bootapp.AppConst.VIEW_TH_LITERAL;

import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import bootapp.model.Contact;
import bootapp.model.Mail;
import bootapp.model.Phone;
import bootapp.model.User;
import bootapp.props.ItemsProps;

@Controller
@RequestMapping(URL_THYME)
public class ThymeleafController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private ItemsProps itemProps;
        
    @ModelAttribute
    private void setModels(Model model) {
        // ビューに渡すモデルやコントローラの情報を modelに追加する
        model.addAttribute(ATTR_SEIBETU_ITEMS, itemProps.getSeibetu());
        model.addAttribute(ATTR_SYUMI_ITEMS, itemProps.getSyumi());
        model.addAttribute(ATTR_CONTACT_SYUBETU_ITEMS, itemProps.getContactSyubetu());
    }
    // ##### Helper #####
    private ModelAndView createMavWithContact(String viewName) {
        return new ModelAndView(viewName, ATTR_CONTACT, createContact(new User("user1")));
    }
    private Contact createContact(User u) {
        Contact c = new Contact(100000L, u, "山本", "有三", "やまもと", "ゆうぞう", 
        		"男", "1980/01/02", "家族親戚");
        c.getPhones().add(new Phone(100101L, "0311112222", "自宅"));
        c.getMails().add(new Mail(100201L, "y.yamamoto@xyz.com", "自宅"));
        c.setSyumi("音楽,スポーツ");
        return c;
    }
    
    //=====(MAP-METHOD)=====

    /** テキストフィールド・フラグメントに置換 */
    @GetMapping(URL_FRAG_FIELD01)
    public ModelAndView fragField01() {
        return createMavWithContact(VIEW_FRAG_FIELD01);
    }

    //%%%%% th:each, th:if, th:text .. %%%%%
    /** 繰り返し処理（配列|List）*/
    @GetMapping(URL_TH_ITERATION)
    public ModelAndView thIteration() {
        ModelAndView mav = new ModelAndView(VIEW_TH_ITERATION,
                "array1", new String[] {"配列要素1", "配列要素2", "配列要素3"});
        Map<String, String> map1 = new TreeMap<>();
        map1.put("キー1", "値1");
        map1.put("キー2", "値2");
        map1.put("キー3", "値3");
        mav.addObject("map1", map1);
        Map<String, Phone> map2 = new TreeMap();
        map2.put("自宅", new Phone("0355556666", "自宅"));
        map2.put("携帯", new Phone("09011112222", "携帯"));
        map2.put("会社", new Phone("0388889999", "会社"));
        mav.addObject("map2", map2);
        return mav;
    }

    /** th:if の使用.*/
    @GetMapping(URL_TH_CONDITION)
    public ModelAndView thCondition() {
        return createMavWithContact(VIEW_TH_CONDITION);
    }

    @GetMapping(URL_TH_EXPRESSIONS)
    public ModelAndView thExpressions() throws JsonProcessingException {
        ModelAndView mav = createMavWithContact(VIEW_TH_EXPRESSIONS);
        Calendar cal = Calendar.getInstance();
        cal.set(2016, 00, 02, 20, 30, 40); // 2016-01-02 20:30:40
        mav.addObject("date20160102_203040", cal.getTime()); // 指定した日付
        mav.addObject(ATTR_CUR_SYUBETU, "家族親戚");
        ObjectMapper om = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        Contact c = createContact(new User("user1"));
        mav.addObject(ATTR_CONTACT, c);
        mav.addObject("contactJson", om.writeValueAsString(c));
        return mav;
    }

    @GetMapping(URL_TH_INLINE)
    public ModelAndView thInline() throws JsonProcessingException {
        ModelAndView mav = createMavWithContact(VIEW_TH_INLINE);
        ObjectMapper om = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        Contact c = createContact(new User("user1"));
        mav.addObject(ATTR_CONTACT, c);
        mav.addObject("contactJson", om.writeValueAsString(c));
        return mav;
    }
    
    @GetMapping(URL_TH_LITERAL)
    public String thLiteral() {
        return VIEW_TH_LITERAL;
    }
    
    // フォワード
    @GetMapping("/fwrdOk/{data}")
    public ModelAndView fwrdOk(@PathVariable("data") int data) {
        return new ModelAndView(VIEW_THYME_KEKKA, ATTR_KEKKA, 
                "フォワード:OK data=" + data);
    }
    @GetMapping("/fwrdError/{data}")
    public ModelAndView fwrdError(@PathVariable("data") int data) {
        log.debug("fwrdError:" + data);
        return new ModelAndView(VIEW_THYME_KEKKA, ATTR_KEKKA,
                "フォワード:Error data=" + data);
    }
   
    // /thyme/kensho/50 | /thyme/kensyo/20
    @GetMapping("/kensyo/{data}")
    public ModelAndView kensyo(@PathVariable("data") Integer data) {
        log.debug("kensyo:" + data);
        if  (data > 30) {
            return new ModelAndView("forward:/thyme/fwrdOk/" + data, ATTR_KEKKA, data + ""); 
        } else {
            return new ModelAndView("forward:/thyme/fwrdError/" + data, ATTR_KEKKA, data + "");
        }
    }
    
    // リダイレクト
    @RequestMapping("/redirect1")
    public String redirect1() {
        log.debug("redirect1:");
        // redirect の場合、/thyme は不要
        return "redirect:method1";
    }
    @RequestMapping("/method1")
    public ModelAndView method1() {
        log.debug("method1:");
        return new ModelAndView(VIEW_THYME_KEKKA, ATTR_KEKKA,
                "method1へリダイレクト");
    }
    
}

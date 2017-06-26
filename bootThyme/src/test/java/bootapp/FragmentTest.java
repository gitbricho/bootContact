package bootapp;

import static bootapp.AppConst.ATTR_CONTACT;
import static bootapp.AppConst.ATTR_SYUMI_ITEMS;
import static bootapp.AppConst.URL_FRAG_FIELD01;
import static bootapp.AppConst.URL_THYME;
import static bootapp.AppConst.VIEW_FRAG_FIELD01;
import static org.hamcrest.Matchers.containsString;
//get, post, request
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import bootapp.props.ItemsProps;

/**
 * Thymeleaf テストコントローラ(ThymeLeafController)のテスト.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class FragmentTest {
    private static final Logger log = LoggerFactory.getLogger(FragmentTest.class);
    private static final String LOG_TEST_BAR = "&&&&&&&&&&&&&&& {} {}: {}";
    
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private ItemsProps itemProps;
    
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        // log.debug(LOG_SETUP_BAR);
        // テストで使用する MockMvc を作成
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .build();
    }

    private void logTestHeader(String kind, String testCaseName, String rem) {
    	log.info(LOG_TEST_BAR, kind, testCaseName, rem);
    }
    
    //=====(TEST-METHOD)=====
    @Test
    public void testFragField01() throws Exception {
    	logTestHeader("FragField01", "", "");
        mockMvc.perform(get(URL_THYME + URL_FRAG_FIELD01))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name(VIEW_FRAG_FIELD01))
        .andExpect(model().attributeExists(ATTR_CONTACT))
        //テキストフィールド
        .andExpect(content().string(containsString("<label class=\"control-label col-md-2\" for=\"simeiSei\">姓</label>")))
        .andExpect(content().string(containsString("<div class=\"controls col-md-10\">")))
        .andExpect(content().string(containsString("<input type=\"text\" placeholder=\"姓\"")))
        //.andExpect(content().string(containsString("maxlength=\"18\" class=\"form-control\" id=\"simei\" name=\"simei\" value=\"山本雄三\" />"))) 
        //チェックボックス： test に渡す contact の趣味は "音楽,スポーツ"
        .andExpect(model().attribute(ATTR_SYUMI_ITEMS, itemProps.getSyumi()))
        .andExpect(content().string(containsString("<label class=\"control-label col-md-2\" for=\"syumi\">趣味</label>")))
        .andExpect(content().string(containsString("<div id=\"syumi\">")))
        .andExpect(content().string(containsString("<input type=\"checkbox\" value=\"読書\" id=\"syumiList1\" name=\"syumiList\" >読書</input>")))
        .andExpect(content().string(containsString("<input type=\"checkbox\" value=\"スポーツ\" id=\"syumiList2\" name=\"syumiList\" checked=\"checked\" >スポーツ</input>")))
        .andExpect(content().string(containsString("<input type=\"checkbox\" value=\"音楽\" id=\"syumiList3\" name=\"syumiList\" checked=\"checked\" >音楽</input>")))
        .andExpect(content().string(containsString("<input type=\"checkbox\" value=\"ゲーム\" id=\"syumiList4\" name=\"syumiList\" >ゲーム</input>")))
        .andExpect(content().string(containsString("<input type=\"checkbox\" value=\"その他\" id=\"syumiList5\" name=\"syumiList\" >その他</input>")))
        //ラジオボタン
        .andExpect(content().string(containsString("<label class=\"control-label col-md-2\" for=\"seibetu\">性別</label>")))
        .andExpect(content().string(containsString("<input type=\"radio\" value=\"男\" id=\"seibetu1\" name=\"seibetu\" checked=\"checked\" >男</input>")))
        .andExpect(content().string(containsString("<input type=\"radio\" value=\"女\" id=\"seibetu2\" name=\"seibetu\" >女</input>")))
        //セレクト
        .andReturn();
    }
}

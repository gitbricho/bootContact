package bootapp;

import static bootapp.AppConst.ATTR_CONTACT;
import static bootapp.AppConst.URL_THYME;
import static bootapp.AppConst.URL_TH_EXPRESSIONS;
import static bootapp.AppConst.VIEW_TH_EXPRESSIONS;
import static org.hamcrest.Matchers.containsString;
//get, post, request
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import bootapp.model.User;
import bootapp.props.ItemsProps;

/**
 * Thymeleaf テストコントローラ(ThymeLeafController)のテスト.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class ExpressionsTest {
    private static final Logger log = LoggerFactory.getLogger(ExpressionsTest.class);
    private static final String LOG_TEST_BAR = "&&&&&&&&&&&&&&& {} {}: {}";
    
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private ItemsProps itemProps;
    
    private MockMvc mockMvc;
    private MockHttpSession mockHttpSession;

    @Before
    public void setUp() {
        // log.debug(LOG_SETUP_BAR);
        // テストで使用する MockMvc を作成
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .build();
        this.mockHttpSession = new MockHttpSession(wac.getServletContext(),
                UUID.randomUUID().toString());
    }

    private void logTestHeader(String kind, String testCaseName, String rem) {
    	log.info(LOG_TEST_BAR, kind, testCaseName, rem);
    }
    
    //=====(TEST-METHOD)=====
    @Test
    public void testExpressions() throws Exception {
    	logTestHeader("Expressions", "", "");
        mockHttpSession.setAttribute("loginUser", new User("tosi"));
        mockMvc.perform(get(URL_THYME + URL_TH_EXPRESSIONS)
            .session(mockHttpSession)
            .param("param1", "AAA")
            .param("param2", "BBB"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name(VIEW_TH_EXPRESSIONS))
        .andExpect(model().attributeExists(ATTR_CONTACT))
        .andExpect(model().attributeExists("date20160102_203040"))
        .andExpect(content().string(containsString(
        		"id='01'>これは &lt;b&gt;Spring Boot Demo アプリケーション!&lt;/b&gt; です。")))
        .andExpect(content().string(containsString(
        		"id='02'>これは <b>Spring Boot Demo アプリケーション!</b> です。")))
        .andExpect(content().string(containsString(
        		"id='03'>太田 さん、今日の日付は 2017/05/01 です。")))
        .andExpect(content().string(containsString("id='04'>変数var1はNULL")))
        .andExpect(content().string(containsString("id='05'>[男, 女]")))
        .andExpect(content().string(containsString("id='06'>男")))
        .andExpect(content().string(containsString("id='07'>128KB")))
        .andExpect(content().string(containsString("id='08'>contact.save.ok")))
        .andExpect(content().string(containsString("id='09'>:保存に成功しました!!")))
        .andExpect(content().string(containsString("id='10'>tosi")))
        .andExpect(content().string(containsString("id='11'>2")))
        .andExpect(content().string(containsString("id='12'>AAA, BBB")))
        .andExpect(content().string(containsString("id='13'>layout/layout")))
        .andExpect(content().string(containsString("id='14'>2016/01/02")))
        .andExpect(content().string(containsString("id='15'>12,345.7")))
        .andExpect(content().string(containsString("id='16'>true")))
        .andExpect(content().string(containsString("id='17'>true")))
        .andReturn();
    }
}

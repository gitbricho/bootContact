package bootapp;

import static bootapp.AppConst.URL_THYME;
import static bootapp.AppConst.URL_TH_CONDITION;
import static bootapp.AppConst.VIEW_TH_CONDITION;
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

/**
 * Thymeleaf テストコントローラ(ThymeLeafController)のテスト.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class ConditionTest {
    private static final Logger log = LoggerFactory.getLogger(ConditionTest.class);
    private static final String LOG_TEST_BAR = "&&&&&&&&&&&&&&& {} {}: {}";
    
    @Autowired
    private WebApplicationContext wac;
    
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
    public void testCondition() throws Exception {
    	logTestHeader("Condition", "", "");
        mockMvc.perform(get(URL_THYME + URL_TH_CONDITION))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name(VIEW_TH_CONDITION))
        .andExpect(model().attributeExists("contact"))
        .andExpect(content().string(containsString("<p style=\"color:yellow;\">読書")))
        .andExpect(content().string(containsString("<p>スポーツ ( th:case=\"*\" )")))
        .andExpect(content().string(containsString("<p>音楽 ( th:case=\"*\" )")))
        .andExpect(content().string(containsString("<p>ゲーム ( th:case=\"*\" )")))
        .andExpect(content().string(containsString("<p style=\"color:red;\">その他")))
        .andExpect(content().string(containsString("真(1)")))
        .andExpect(content().string(containsString("真(2)")))
        .andExpect(content().string(containsString("真(4)")))
        .andExpect(content().string(containsString("真(5)")))
        .andExpect(content().string(containsString("真(6)")))
        .andExpect(content().string(containsString("真(7)")))
        .andExpect(content().string(containsString("真(8)")))
        .andReturn();
    }
}

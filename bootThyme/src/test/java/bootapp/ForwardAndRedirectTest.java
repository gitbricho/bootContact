package bootapp;

import static bootapp.AppConst.ATTR_KEKKA;
import static bootapp.AppConst.URL_THYME;
//get, post, request
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
public class ForwardAndRedirectTest {
    private static final Logger log = LoggerFactory.getLogger(ForwardAndRedirectTest.class);
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
    public void testKensyo() throws Exception {
    	logTestHeader("Kensyo", "", "");
        mockMvc.perform(get(URL_THYME + "/kensyo/22"))
          .andExpect(status().isOk())
          .andDo(print())
          .andExpect(view().name("forward:/thyme/fwrdError/22"))
          .andExpect(model().attribute(ATTR_KEKKA, "22"));
    }
    @Test
    public void testRedirect() throws Exception {
    	logTestHeader("Redirect", "", "");
        mockMvc.perform(get(URL_THYME + "/redirect1"))
          .andDo(print())
          .andExpect(status().isFound())
          .andExpect(view().name("redirect:method1"));
    }
}

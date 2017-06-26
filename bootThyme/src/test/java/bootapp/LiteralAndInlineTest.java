package bootapp;

import static bootapp.AppConst.URL_THYME;
import static bootapp.AppConst.URL_TH_INLINE;
import static bootapp.AppConst.URL_TH_LITERAL;
//get, post, request
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

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

/**
 * Thymeleaf テストコントローラ(ThymeLeafController)のテスト.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class LiteralAndInlineTest {
    private static final Logger log = LoggerFactory.getLogger(LiteralAndInlineTest.class);
    private static final String LOG_TEST_BAR = "&&&&&&&&&&&&&&& {} {}: {}";
    
    @Autowired
    private WebApplicationContext wac;
    
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
    public void testLiteral() throws Exception {
    	logTestHeader("Literal", "", "");
        mockHttpSession.setAttribute("user", new User("tosi"));
        mockMvc.perform(get(URL_THYME + URL_TH_LITERAL)
                .session(mockHttpSession))
                .andDo(print());
    }
    
    @Test
    public void testInline() throws Exception {
    	logTestHeader("Inline", "", "");
        mockHttpSession.setAttribute("user", new User("tosi"));
        mockMvc.perform(get(URL_THYME + URL_TH_INLINE)
                .session(mockHttpSession))
                .andDo(print());
    }
    
}

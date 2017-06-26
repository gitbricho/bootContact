package bootapp;

import static bootapp.AppConst.VIEW_KEKKA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * 受注コントローラのハンドラーメソッドをテストする.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class JutyuControllerTest {

    private static final Logger log = LoggerFactory.getLogger(JutyuControllerTest.class);
    private static final String LOG_TEST_BAR = "&&&&&&&&&&&&&&& {} {}: {}";

    @Autowired
    private WebApplicationContext wac;
    
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .build();
    }
    private void logTestHeader(String kind, String testCaseName, String rem) {
    	log.info(LOG_TEST_BAR, kind, testCaseName, rem);
    }
    
    //=====(TEST-METHOD)=====
   @Test
    public void testGetJutyuMeisai() throws Exception {
    	logTestHeader("GetJutyuMeisai", "", "");
        mockMvc.perform(get("/jutyu/meisai/301"))
        	.andDo(print())
        	.andExpect(status().isOk())
        	.andExpect(view().name(VIEW_KEKKA));
        	//.andExpect(model().attribute(ATTR_KEKKA, "meisaiId=301"));
    }
    @Test
    public void testGetJutyuSyohin() throws Exception {
    	logTestHeader("GetJutyuSyohin", "", "");
        mockMvc.perform(get("/jutyu/syohin/100/15"))
        	.andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name(VIEW_KEKKA));
            //.andExpect(model().attribute(ATTR_KEKKA, "tanka=100,kosu=15"));
    }
    @Test
    public void testGetKokyaku() throws Exception {
    	logTestHeader("GetKokyaku", "", "");
        mockMvc.perform(get("/kokyaku?id=10&name=納品先01"))
        	.andDo(print())
        	.andExpect(status().isOk())
        	.andExpect(view().name(VIEW_KEKKA));
        	//.andExpect(model().attribute(ATTR_KEKKA, "id=10,name=納品先01"));
    }
}

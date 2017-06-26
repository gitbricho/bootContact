package bootapp;

import static bootapp.AppConst.URL_THYME;
import static bootapp.AppConst.URL_TH_ITERATION;
import static bootapp.AppConst.VIEW_TH_ITERATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
//get, post, request
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Map;

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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Thymeleaf テストコントローラ(ThymeLeafController)のテスト.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class IterationTest {
    private static final Logger log = LoggerFactory.getLogger(IterationTest.class);
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
    public void testIteration() throws Exception {
    	logTestHeader("Iteration", "", "");
        MvcResult mvcResult = mockMvc.perform(
            get(URL_THYME + URL_TH_ITERATION))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name(VIEW_TH_ITERATION))
        //配列の繰り返し
        .andExpect(content().string(containsString("${stat.index}= 0, ${elm} = 配列要素1")))
        .andExpect(content().string(containsString("${stat.index}= 1, ${elm} = 配列要素2")))
        .andExpect(content().string(containsString("${stat.index}= 2, ${elm} = 配列要素3")))
        //リストの繰り返し
        .andExpect(content().string(containsString("${stat.index+1}=1:${syumi}=読書")))
        .andExpect(content().string(containsString("${stat.index+1}=2:${syumi}=スポーツ")))
        .andExpect(content().string(containsString("${stat.index+1}=3:${syumi}=音楽")))
        .andExpect(content().string(containsString("${stat.index+1}=4:${syumi}=ゲーム")))
        .andExpect(content().string(containsString("${stat.index+1}=5:${syumi}=その他")))
        //マップの繰り返し
        .andExpect(content().string(containsString("${m1.key}=キー1 , ${m1.value}=値1")))
        .andExpect(content().string(containsString("${m1.key}=キー2 , ${m1.value}=値2")))
        .andExpect(content().string(containsString("${m1.key}=キー3 , ${m1.value}=値3")))
        .andReturn();

        Map<String, Object> models = mvcResult.getModelAndView().getModel();
        String[] array1 = (String[])models.get("array1");
        assertThat(array1).isNotNull();
        assertThat(array1.length).isEqualTo(3);
        assertThat(array1[0]).isEqualTo("配列要素1");
        assertThat(array1[1]).isEqualTo("配列要素2");
        assertThat(array1[2]).isEqualTo("配列要素3");
        Map<String, String> map1 = (Map<String, String>)models.get("map1");
        assertThat(map1).isNotNull();
        assertThat(map1.size()).isEqualTo(3);
        assertThat(map1.get("キー1")).isEqualTo("値1");
        assertThat(map1.get("キー2")).isEqualTo("値2");
        assertThat(map1.get("キー3")).isEqualTo("値3");
    }
}

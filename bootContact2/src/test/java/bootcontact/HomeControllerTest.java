package bootcontact;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * ホームコントローラのテスト.
 * セキュリティ保護されていないページリクエストを処理するメソッドをテストする。
 * TestRestTemplate の使い方のサンプル.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class HomeControllerTest {
    private static final Logger log = LoggerFactory
            .getLogger(HomeControllerTest.class);

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private WebApplicationContext wac;

    @LocalServerPort
    private int port;
    
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .build();
    }

    @Test
    public void testRoot() throws Exception {
        log.debug("testRoot: %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        this.mockMvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(view().name("views/home"));
    }

    @Test
    public void testHome() throws Exception {
        log.debug("testHome: %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        this.mockMvc.perform(get("/home")).andExpect(status().isOk())
                .andExpect(view().name("views/home"));

        restTemplate = new TestRestTemplate();
        ResponseEntity<Void> entity = restTemplate.getForEntity(
                "http://localhost:" + this.port + "/bootContact", Void.class);
        log.debug("entity.status >> " + entity.getStatusCode().name() + ":"
                + entity.getStatusCodeValue());
    }
    
}
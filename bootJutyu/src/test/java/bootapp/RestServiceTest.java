package bootapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
//get, post, request
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//status, view, content, model, forwardedUrl, redirectedUrl, print, crsf
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bootapp.model.Kokyaku;
import bootapp.repo.KokyakuRepo;

/**
 * RESTサービスのテスト.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RestServiceTest {
  private static final Logger log = LoggerFactory.getLogger(RestServiceTest.class);
  private static final String LOG_TEST_BAR = "&&&&&&&&&&&&&&& {} {}: {}";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private KokyakuRepo repo;
  @Autowired
  private ObjectMapper mapper;

  private MockMvc mockMvc;
  private Map<String, Long> idMap = new HashMap<>();

  @Autowired
  private TestRestTemplate testRestTemplate;
  @LocalServerPort
  private int port;

  @Before
  public void setUp() {
    if (repo.findAll().size() == 0) {
      log.debug("##### ４件の顧客を追加 #####");
      saveKokyaku(new Kokyaku(101L, "倉岡宏", "h.kuraoka@xxx.com", 10));
      saveKokyaku(new Kokyaku(102L, "田中優子", "y.tanaka@zzz.com", 20));
      saveKokyaku(new Kokyaku(103L, "岡田英二", "e.okada@yyy.com", 5));
      saveKokyaku(new Kokyaku(104L, "岡本恵子", "k.okamoto@yyy.com", 8));
      assertThat(repo.findAll().size()).isEqualTo(4);
    }

    // テストで使用する MockMvc を作成
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  private void logTestHeader(String kind, String testCaseName, String rem) {
    log.info(LOG_TEST_BAR, kind, testCaseName, rem);
  }

  // ## Helper
  // respons からコンテンツを取り出しさらにJSON文字列をCustomerオブジェクトに変換.
  private Kokyaku JsonToCustomer(MockHttpServletResponse response)
      throws JsonParseException, JsonMappingException, IOException {
    String body = response.getContentAsString();
    Kokyaku k = mapper.readValue(body, Kokyaku.class);
    return k;
  }

  // レスポンスのリダイレクトURLから"/customer/{id}"部分を取得.
  private String customerUrl(MockHttpServletResponse response) {
    String url = response.getRedirectedUrl();
    return url == null ? "" : url.substring(url.indexOf("/customer"));
  }

  // 顧客を保存して、保存した顧客の<名前, id>をマップに追加.
  private void saveKokyaku(Kokyaku kokyaku) {
    Kokyaku k = repo.save(kokyaku);
    idMap.put(k.getName(), k.getId());
  }

  // =====(TEST-METHOD)=====
  @Test
  public void testGetCustomers() throws Exception {
    logTestHeader("GetCustomers", "", "");
    mockMvc.perform(get("/customer")).andDo(print()).andExpect(status().isOk());
  }

  @Test
  public void testGetCustomer101() throws Exception {
    logTestHeader("GetCustomer101", "", "");
    // testTemplate を使った場合：
    @SuppressWarnings("rawtypes")
    ResponseEntity<Map> entity = this.testRestTemplate
        .getForEntity("http://localhost:" + this.port + "/bootApp/customer", Map.class);

    then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    log.info("port={}", this.port);
    log.info("headers={}", entity.getHeaders());
    log.info("body={}", entity.getBody());
  }

  @Test
  public void testGetCustomerPage1Size1() throws Exception {
    logTestHeader("GetCustomerPage1Size1", "", "");
    mockMvc.perform(get("/customer?page=1&size=1")).andDo(print()).andExpect(status().isOk());
  }

  @Test
  public void testGetCustomerSortEmail() throws Exception {
    logTestHeader("GetCustomerSortEmail", "", "");
    mockMvc.perform(get("/customer?sort=email,desc")).andDo(print()).andExpect(status().isOk());
  }

  @Test
  public void testAddCustomer() throws Exception {
    logTestHeader("AddCustomer", "", "");
    Kokyaku k = new Kokyaku(201L, "Kokyaku1", "kokyaku1@xxx.com", 0);
    String json = mapper.writeValueAsString(k);
    MvcResult mvcResult = mockMvc
        .perform(post("/customer").contentType(MediaType.APPLICATION_JSON).content(json)
            .with(user("tosi").password("tosi").roles("ADMIN")))
        .andDo(print()).andExpect(status().is(201)).andReturn();
    String url = customerUrl(mvcResult.getResponse());
    log.debug(">>> customerUrl={}", url);
    // 追加したKokyakuを取得
    mvcResult = mockMvc.perform(get(url)).andDo(print()).andReturn();
    log.debug(">>> kokyaku={}", JsonToCustomer(mvcResult.getResponse()));
  }
}

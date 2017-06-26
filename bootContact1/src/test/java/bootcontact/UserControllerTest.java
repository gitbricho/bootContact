package bootcontact;

import static bootcontact.AppConst.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * ユーザーコントローラのテスト.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class UserControllerTest {
    public static final Logger log = LoggerFactory.getLogger(UserControllerTest.class);
    private static final String LOG_TEST_BAR = "&&&&&&&&&&&&&&& {} {}: {}";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }
    void logTestHeader(String kind, String testCaseName, String rem) {
    	log.info(LOG_TEST_BAR, kind, testCaseName, rem);
    }

    //=====(TEST-METHOD)=====
    @Test
    public void testUserListWithTosi() throws Exception {
    	logTestHeader("UserListWithTosi", "", "");
        this.mockMvc.perform(get(URL_USER_LIST)
            .with(user("tosi").password("tosi").roles("ADMIN")))
        .andDo(print())
        .andExpect(status().isOk());
    }

    @Test
    public void testUserListWithYumi() throws Exception {
    	logTestHeader("UserListWithYumi", "", "");
        this.mockMvc.perform(get(URL_USER_LIST)
            .with(user("yumi").password("yumi").roles("USER")))
        .andDo(print())
        //ログイン済みだがアクセスが許可されていないユーザー
        .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "tosi", password = "tosi", roles = "ADMIN")
    public void testUserList() throws Exception {
    	logTestHeader("UserList", "", "");
        this.mockMvc.perform(get(URL_USER_LIST))
        .andDo(print())
        .andExpect(status().isOk());
    }

    @Test
    public void testUserListNotLogin() throws Exception {
    	logTestHeader("UserListNotLogin", "", "");
        this.mockMvc.perform(get(URL_USER_LIST))
        .andDo(print())
        //まだログインしていなのでログインページへリダイレクトされる
        .andExpect(redirectedUrl("http://localhost/login"))
        .andExpect(status().isFound());
    }
}

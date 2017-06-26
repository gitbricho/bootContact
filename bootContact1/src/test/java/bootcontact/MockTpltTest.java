package bootcontact;

import static bootcontact.AppConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import bootcontact.controller.UserController;

//MARK: クラス
/**
 * Spring Boot + MockMvc + Mockit による単体テストのテンプレート.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class MockTpltTest {
    private static final Logger log = LoggerFactory.getLogger(MockTpltTest.class);
    private static final String LOG_TEST_BAR = "&&&&&&&&&&";

    /*
     * MockMvc + Mockito のセットアップには、様々な方法があるが、 
     * 動作するものをひとつ覚えておく事が大事。
     */
    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    
    // テスト対象のクラスをセットアップ
    @Spy @InjectMocks
    private MyServiceA serviceA;
    @Spy @InjectMocks
    private UserController userController;
    

    // MockMvc のセットアップ
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;

    // リポジトリやモデルのセットアップ
    @Autowired
    ObjectMapper mapper;

    @Before
    public void setUp() {
        //MockitoAnnotations.initMocks(testClass);
        // テストで使用する MockMvc を作成
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    };
    
    //=====(TEST-METHOD)=====
    //まずは基本、mock(), verify(), when() の使い方
    @Test
    public void testCase01() throws Exception {
        log.debug(LOG_TEST_BAR);
        //モックを作る
        @SuppressWarnings("unchecked")
		List<String> mockList = mock(List.class);
        
        //作ったモックを使い
        mockList.add("AAA");
        //add メソッドの使用が１回でかつ引数が"AAA"をチェック
        verify(mockList, times(1)).add("AAA");
        
        //同じように、作ったモックを使い
        mockList.clear();
        mockList.clear();
        //clear メソッドの使用が2回をチェック
        verify(mockList, times(2)).clear();

        //when を使って、get(0)の挙動をモック化する
        when(mockList.get(0)).thenReturn("先頭要素");
        //テスト：どんな要素を追加しても
        mockList.add("どんな要素を追加しても");
        //get(0)が返すのは、文字列の"先頭要素"
        assertThat(mockList.get(0)).isEqualTo("先頭要素");
        //when は挙動を追加する
        when(mockList.get(999)).thenReturn("データがありません");
        assertThat(mockList.get(0)).isEqualTo("先頭要素");
        //本当ならそんな要素はないので、nullが返されるが、
        assertThat(mockList.get(999)).isEqualTo("データがありません");
    }
    
    // @Rule, @Spy, @InjectMocks を使ってテスト対象をセットアップ
    @Test
    public void testCase02() throws Exception {
        log.debug(LOG_TEST_BAR);
        // @Spy @InjectMocks 注釈により、serviceA はテスト対象として扱われる
        // serviceA をモック化する
        // 本来の serviceA.doSomething1("AAA") --> "AAA!!!" だが
        // これを、どんな文字列の引数でも "" を返すように設定
        when(this.serviceA.doSomething1(anyString())).thenReturn("");
        // 本来の serviceA.doSomething2(10) --> 20 だが
        // これを、どんなLong引数でも 1L を返すように設定
        when(this.serviceA.doSomething2(any(Long.class))).thenReturn(1L);

        // モック化された挙動をチェックする
        assertThat(serviceA.doSomething1("AAA")).isEqualTo("");
        
        // when で doSomething1() モック機能を追加
        when(this.serviceA.doSomething1("name1")).thenReturn("Name1");
        assertThat(serviceA.doSomething1("name1")).isEqualTo("Name1");
        
        // reset でモックをリセット
        reset(this.serviceA);
        // 本来のメソッドが有効になる
        assertThat(serviceA.doSomething1("bbb")).isEqualTo("bbb!!!");
        assertThat(serviceA.doSomething2(5L)).isEqualTo(10L);
    }
    
    // ここから MockMvc を使ってコントローラをテストする
    @Test
    public void testCase03() throws Exception {
        log.debug(LOG_TEST_BAR);
        mockMvc.perform(get(URL_USER_LIST))
        .andExpect(status().isOk())
        .andExpect(view().name(VIEW_USER_LIST));
    }
}

/*
 * このテストクラスの説明用に使うクラス.
 */
class MyServiceA {
    public MyServiceA() {
    }
    public String doSomething1(String name) {
        return name + "!!!";
    }
    public Long doSomething2(Long a) {
        return a * 2;
    }
}


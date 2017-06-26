package bootcontact;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import javax.persistence.EntityManagerFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.junit4.SpringRunner;

import bootcontact.controller.ContactController;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AppTest {
    
    private static final Logger log = LoggerFactory.getLogger(AppTest.class);
    
    @Autowired
    ApplicationContext ctx;
    @Autowired
    Environment env;

    //=====(TEST-METHOD)=====
    @Test
    public void testContextLoads() throws Exception {
        log.info("server.port="+env.getRequiredProperty("server.port"));

        assertThat(this.ctx).isNotNull();
        
        // ### 全てはログの出力で確認できるがテストで確認する
        log.info("## アプリは起動してる？");
        assertThat(this.ctx.containsBean("app")).isTrue();
        assertThat(this.ctx.containsBean("dispatcherServlet")).isTrue();
        
        log.info("## 設定クラスは？");
        assertThat(this.ctx.containsBean("appConfig")).isTrue();
        assertThat(this.ctx.containsBean("secConfig")).isTrue();
        assertThat(this.ctx.containsBean("dbConfig")).isTrue();
        assertThat(this.ctx.containsBean("entityManagerFactory")).isTrue();
                
        log.info("## プロパティの値: application.properties");
        assertThat(env.getProperty("server.contextPath")).isEqualTo("/bootContact");
        assertThat(env.getProperty("spring.thymeleaf.cache")).isEqualTo("false");
        assertThat(env.getProperty("logging.file")).isEqualTo("bootContact");

        log.info("## プロパティの値: application.yml");
        assertThat(env.getProperty("spring.profiles.active")).isEqualTo("");
        assertThat(env.getProperty("model.scan.package")).isEqualTo("bootcontact.model");
        assertThat(env.getProperty("message.source.basename")).isEqualTo("classpath:messages/messages");
        assertThat(env.getProperty("eclipselink.ddl-generation")).isEqualTo("drop-and-create-tables");
        assertThat(env.getProperty("eclipselink.target-database"))
            .isEqualTo("org.eclipse.persistence.platform.database.H2Platform");
        assertThat(env.getProperty("eclipselink.weaving")).isEqualTo("false");
        assertThat(env.getProperty("eclipselink.logging.level")).isEqualTo("config");

        log.info("## アクティブ・プロファイル?");
        Arrays.asList(env.getActiveProfiles()).forEach( ap -> {
            log.info(">>> activeProfile=" + ap);
        });

        log.info("## プロパティの値: application-devh2.yaml");
        assertThat(env.getProperty("spring.datasource.driverClassName")).isEqualTo("org.h2.Driver");
        assertThat(env.getProperty("spring.datasource.url")).isEqualTo("jdbc:h2:~/_data/h2/contact;MODE=MySQL");
        assertThat(env.getProperty("spring.datasource.username")).isEqualTo("demo");
        assertThat(env.getProperty("spring.datasource.password")).isEqualTo("pass");
        
        log.info("## DBの設定は？");
        EntityManagerFactory emf = (EntityManagerFactory)this.ctx.getBean("entityManagerFactory");
        assertThat(emf.getProperties().get("eclipselink.ddl-generation")).isEqualTo("drop-and-create-tables");
        assertThat(this.ctx.containsBean("dataSource")).isTrue();
        DriverManagerDataSource dsc = (DriverManagerDataSource)this.ctx.getBean("dataSource");
        log.info("## DBの設定: H2");
        assertThat(dsc.getUrl()).isEqualTo("jdbc:h2:~/_data/h2/contact;MODE=MySQL");
        log.info("## DBの設定: MySQL");
        //assertThat(dsc.getUrl()).isEqualTo("jdbc:mysql://localhost:3306/bootTest");
        
        assertThat(this.ctx.containsBean("transactionManager")).isTrue();
        
        log.info("## Bean: restTemplate");
        assertThat(this.ctx.containsBean("restTemplate"));

        log.info("## コントローラは？");
        assertThat(this.ctx.containsBean("contactController")).isTrue();
        assertThat(this.ctx.getType("contactController")).isEqualTo(ContactController.class);
        
        log.info("## リポジトリは？");
        assertThat(this.ctx.containsBean("userRepository")).isTrue();
        assertThat(this.ctx.containsBean("contactRepository")).isTrue();

        log.info("## サービスは？");
        assertThat(this.ctx.containsBean("userServiceImpl")).isTrue();        
        assertThat(this.ctx.containsBean("contactService")).isFalse();
        assertThat(this.ctx.containsBean("requestMappingHandlerAdapter")).isTrue();
    }
}

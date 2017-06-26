package bootapp;

import static bootapp.AppConst.*;

import javax.annotation.Resource;

import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
// TODO bootSec:DB設定、セキュリティ設定クラスを指定
//@Import({ DbConfig.class, SecConfig.class })
public class AppConfig extends WebMvcConfigurerAdapter {
    
    @Resource
    private Environment env;
    @Resource
    EmbeddedWebApplicationContext ewa;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController(URL_ERROR).setViewName(VIEW_ERROR);
        registry.addViewController(URL_LOGIN).setViewName(VIEW_LOGIN);
        registry.addViewController(URL_HOME).setViewName(VIEW_HOME);
        registry.addViewController(URL_ROOT).setViewName(VIEW_HOME);
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename(env.getProperty("message.source.basename"));
        messageSource.setDefaultEncoding("UTF-8");
        // キャッシュ -1: リロードしない、0: 常にリロード
        messageSource.setCacheSeconds(0);
        return messageSource;
    }
}
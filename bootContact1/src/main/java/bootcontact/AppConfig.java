package bootcontact;

import static bootcontact.AppConst.URL_ERROR;
import static bootcontact.AppConst.URL_HOME;
import static bootcontact.AppConst.URL_LOGIN;
import static bootcontact.AppConst.URL_ROOT;
import static bootcontact.AppConst.VIEW_ERROR;
import static bootcontact.AppConst.VIEW_HOME;
import static bootcontact.AppConst.VIEW_LOGIN;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//MARK: クラス
/**
 * アプリケーション構成クラス.
 * <p>スーパークラス ( WebMvcConfigurerAdapter ) のメソッドを
 * 上書きして、アプリケーションを構成する.</p>
 */
@Configuration
public class AppConfig extends WebMvcConfigurerAdapter {
	private static Logger log = LoggerFactory.getLogger(AppConfig.class);
    
    @Resource
    private Environment env;
    @Resource
    EmbeddedWebApplicationContext ewa;

    /**
     * 応答ステータス・コードや応答ビューを構成する簡単なコントローラを
     * 自動生成するように設定する.　これは、カスタムのコントローラロジックが
     * 不要の場合に役立つ－例えば以下の例では URL_ERROR が示す URL に
     * 対して error.html を表示するコントローラを生成する.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController(URL_ERROR).setViewName(VIEW_ERROR);
        registry.addViewController(URL_LOGIN).setViewName(VIEW_LOGIN);
        registry.addViewController(URL_HOME).setViewName(VIEW_HOME);
        registry.addViewController(URL_ROOT).setViewName(VIEW_HOME);
    }

    /**
     * メッセージソース・ビーンの生成.
     * @return
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename(env.getProperty("message.source.basename"));
        messageSource.setDefaultEncoding("UTF-8");
        // キャッシュ -1: リロードしない、0: 常にリロード
        messageSource.setCacheSeconds(0);
        return messageSource;
    }

    /**
     * サンプルインターセプターを登録する.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sampleIntercepter());
    }
    
    /**
     * サンプルインターセプターを生成する.
     * @return サンプルインターセプター・インスタンス
     */
    @Bean
    HandlerInterceptor sampleIntercepter(){
        return new SampleIntercepter();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
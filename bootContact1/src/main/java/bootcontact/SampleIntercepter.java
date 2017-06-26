package bootcontact;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * サンプルインターセプター.
 * <p>リクエスト処理の前後で共通処理を行うインターセプターの使い方を示すサンプル.</p>
 */
public class SampleIntercepter extends HandlerInterceptorAdapter {

    private static final Logger log = LoggerFactory.getLogger(SampleIntercepter.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, 
    		HttpServletResponse httpServletResponse, Object o) throws Exception {
        log.info(">>>>> サンプルインターセプター");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, 
    		HttpServletResponse httpServletResponse, Object o, 
    		ModelAndView modelAndView) throws Exception {
        //log.info(">>>>> サンプルインターセプター");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, 
    		HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //log.info(">>>>> サンプルインターセプター");
    }

}

package bootcontact.controller;

import static bootcontact.AppConst.URL_ERROR;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;

// MARK: クラス
/**
 * ホワイト・ラベル・エラーページ.
 * <p>特に、カスタムのエラー処理実装を設定しなければ、
 * Spring Boot は自動的に BasicErrorController ビーンを登録する。
 * このクラスは、カスタムのエラー処理コントローラを設定する。</p>
 */
//@Controller
public class MyErrorController implements ErrorController{

    @RequestMapping(URL_ERROR)
    public String error() {
        return "error01";
    }
    
    @Override
    public String getErrorPath() {
        return URL_ERROR;
    }

}

package bootcontact;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;

//MARK: クラス
/**
 * 組み込みサーブレット・コンテナのカスタマイズ.
 * EmbeddedServletContainerCustomizer:
 * <p>自動構成された組み込みサーブレットコンテナをカスタマイズするためのストラテジインターフェース。
 * コンテナ自体が起動される前に、このタイプのどのビーンもコンテナファクトリでコールバックされるので、
 * ポート、アドレス、エラー・ページなどを設定できる。</p>
 * <p>注意： このインターフェースは、通常 EmbeddedServletContainerCustomizerBeanPostProcessorから呼ばれる。
 * これは、BeanPostProcessor (アプリコンテキスト・ライフサイクルの非常に早い時期に呼ばれるので) である。 
 * @Autowiredで注入するより、BeanFactoryで依存を参照するほうが安全かもしれない。</p>
 */
public class CustomizeBean implements EmbeddedServletContainerCustomizer {
    
	/**
	 * カスタマイズの設定を実装する.
	 * <p>カスタマイズビーンの例を示すのが目的なので,
	 * ここでは何もしない.</p>
	 */
    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
    	// ポートを設定する
        //container.setPort(9050);
    	// コンテキストパスを設定する.
        //container.setContextPath("/bootContact01");
    }
}

package bootcontact;

import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaDialect;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;

/**
 * データベース設定. 
 * <p>注釈によるトランザクション管理を有効にする。
 * リポジトリクラスのパッケージを指定する。</p>
 */
@Configuration
@EnableJpaRepositories("bootcontact.repo")
// @EnableTransactionManagement
public class DbConfig {

    @Resource
    private Environment env;

    /**
     * データソース・ビーン (dataSource) の作成. 
     * <p>application.yml のプロパティは、Environment コンテキストに登録されるので、
     * env.getRequiredProperty を使ってプロパティの値を取得する.</p>
     * @return データソース
     */
    @Bean
    public DataSource dataSource() {
    	// データソースを生成.
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        // ドライバ、URL、ユーザー名、パスワードを設定する.
        dataSource.setDriverClassName(env.getRequiredProperty("spring.datasource.driverClassName"));
        dataSource.setUrl(env.getRequiredProperty("spring.datasource.url"));
        dataSource.setUsername(env.getRequiredProperty("spring.datasource.username"));
        dataSource.setPassword(env.getRequiredProperty("spring.datasource.password"));
        return dataSource;
    }

    /**
     * エンティティマネージャ・ファクトリ・ビーン (entityManagerFactory) の作成.
     * 
     * @return エンティティマネージャ・ファクトリ
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        EclipseLinkJpaVendorAdapter vendorAdapter = new EclipseLinkJpaVendorAdapter();
        // DDLを生成する.
        vendorAdapter.setGenerateDdl(true);
        // ログでSQLを表示したい場合は true にする.
        vendorAdapter.setShowSql(false);
        
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        // ベンダーアダプターを設定する.
        factory.setJpaVendorAdapter(vendorAdapter);
        // ドメインモデル（エンティティ）をスキャンするパッケージを指定.
        factory.setPackagesToScan(env.getRequiredProperty("model.scan.package"));
        // データソースを設定.
        factory.setDataSource(dataSource());

        //### JPAプロパティの設定
        Properties properties = new Properties();
        // デプロイ時のデータ定義言語(DDL)の生成方法を設定.
        // - create-tables : 表ごとに CREATE TABLE SQL を実行.
        // - drop-and-create-tables : 全表を DROP 後、表の CREATE を実行.
        // - none : DDLを生成しない. スキーマも生成しない.
        properties.put("eclipselink.ddl-generation",
                env.getRequiredProperty("eclipselink.ddl-generation"));
        // ターゲットのDBを設定(H2Platform, MySQLPlatform, ...).
        properties.put("eclipselink.target-database",
                env.getRequiredProperty("eclipselink.target-database"));
        // ログの出力レベルを設定. FINEST を設定すれば非常に詳細に出力.
        properties.put("eclipselink.logging.level",
                env.getRequiredProperty("eclipselink.logging.level"));

        // 以下は、どんな設定ができるかを示すもので、実際的なものではない.
        properties.put("eclipselink.deploy-on-startup", "true");
        properties.put("eclipselink.ddl-generation.output-mode", "database");
        properties.put("eclipselink.weaving", "static");
        properties.put("eclipselink.weaving.lazy", "true");
        properties.put("eclipselink.weaving.internal", "true");
        properties.put("eclipselink.query-results-cache.type", "WEAK");
        properties.put("eclipselink.jdbc.batch-writing", "JDBC");
        properties.put("eclipselink.jdbc.batch-writing.size", "1000");
        factory.setJpaProperties(properties);

        return factory;
    }

    /**
     * トランザクションマネージャ・ビーン (transactionManager) の作成.
     * 
     * @return トランザクションマネージャ
     */
    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager
                .setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    /**
     * エクリプス JPA ダイアレクト (eclipseLinkJpaDialect) の作成.
     * <p>このビーンを使って、エクリプス固有のトランザクション管理を使用できる。</p>
     * 
     * @return EclipseLink JPAダイアレクト
     */
    @Bean
    public EclipseLinkJpaDialect eclipseLinkJpaDialect() {
        EclipseLinkJpaDialect dialect = new EclipseLinkJpaDialect();
        return dialect;
    }
}

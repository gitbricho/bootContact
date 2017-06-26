package bootviewer;

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
 * データベース設定. 注釈によるトランザクション管理を有効にする。 リポジトリパスの設定。
 */
@Configuration
@EnableJpaRepositories("bootviewer.repo")
// @EnableTransactionManagement
public class DbConfig {

  @Resource
  private Environment env;

  /**
   * データソース・ビーン (dataSource) の作成. プロパティの値は application.properties から取得する。
   * 
   * @return データソース
   */
  @Bean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();

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
    vendorAdapter.setGenerateDdl(true);
    vendorAdapter.setShowSql(false);

    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
    factory.setJpaVendorAdapter(vendorAdapter);
    factory.setPackagesToScan(env.getRequiredProperty("model.scan.package"));
    factory.setDataSource(dataSource());

    // JPAプロパティの設定
    Properties properties = new Properties();
    properties.put("eclipselink.ddl-generation",
        env.getRequiredProperty("eclipselink.ddl-generation"));
    properties.put("eclipselink.target-database",
        env.getRequiredProperty("eclipselink.target-database"));
    properties.put("eclipselink.logging.level",
        env.getRequiredProperty("eclipselink.logging.level"));

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
   * @return
   */
  @Bean
  public JpaTransactionManager transactionManager() {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
    return transactionManager;
  }

  /**
   * エクリプス JPA ダイアレクト (eclipseLinkJpaDialect) の作成.
   * このビーンを使って、エクリプス固有のトランザクション管理を使用できる。
   * 
   * @return
   */
  @Bean
  public EclipseLinkJpaDialect eclipseLinkJpaDialect() {
    EclipseLinkJpaDialect dialect = new EclipseLinkJpaDialect();
    return dialect;
  }
}

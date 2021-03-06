package ua.softgroup.medreview.persistent;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import ua.softgroup.medreview.persistent.entity.User;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
@Configuration
@EnableJpaRepositories("ua.softgroup.medreview.persistent.repository")
public class SpringDataConfig {

    private static final String PROPERTY_HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String PROPERTY_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    private static final String PROPERTY_HIBERNATE_HBM2DDL = "hibernate.hbm2ddl.auto";
    private static final String PROPERTY_HIBERNATE_NAMING_STRATEGY = "hibernate.physical_naming_strategy";
    private static final String PROPERTY_HIBERNATE_SEARCH_LUCENE_VERSION = "hibernate.search.lucene_version";
    private static final String PROPERTY_HIBERNATE_SEARCH_DIRECTORY_PROVIDER = "hibernate.search.default.directory_provider";
    private static final String PROPERTY_HIBERNATE_SEARCH_INDEX_BASE = "hibernate.search.default.indexBase";

    @Resource
    private Environment env;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getRequiredProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(env.getRequiredProperty("spring.datasource.url"));
        dataSource.setUsername(env.getRequiredProperty("spring.datasource.username"));
        dataSource.setPassword(env.getRequiredProperty("spring.datasource.password"));

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setDataSource(dataSource());
        entityManager.setPackagesToScan(User.class.getPackage().getName());
        entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManager.setJpaProperties(hibernateProperties());
        entityManager.afterPropertiesSet();

        return entityManager;
    }

    @Bean
    public PlatformTransactionManager transactionManager(){
        return new JpaTransactionManager(entityManagerFactory().getObject());
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put(PROPERTY_HIBERNATE_DIALECT, env.getRequiredProperty("spring.jpa.properties.hibernate.dialect"));
        properties.put(PROPERTY_HIBERNATE_SHOW_SQL, env.getRequiredProperty("spring.jpa.show-sql"));
        properties.put(PROPERTY_HIBERNATE_HBM2DDL, env.getRequiredProperty("spring.jpa.hibernate.ddl-auto"));
        properties.put(PROPERTY_HIBERNATE_NAMING_STRATEGY, env.getRequiredProperty("spring.jpa.hibernate.naming.physical-strategy"));
        properties.put(PROPERTY_HIBERNATE_SEARCH_DIRECTORY_PROVIDER, env.getRequiredProperty("spring.jpa.properties.hibernate.search.default.directory_provider"));
        properties.put(PROPERTY_HIBERNATE_SEARCH_INDEX_BASE, env.getRequiredProperty("spring.jpa.properties.hibernate.search.default.indexBase"));
        properties.put(PROPERTY_HIBERNATE_SEARCH_LUCENE_VERSION, env.getRequiredProperty("spring.jpa.properties.hibernate.search.lucene_version"));
        return properties;
    }

}

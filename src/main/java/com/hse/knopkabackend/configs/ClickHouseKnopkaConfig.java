package com.hse.knopkabackend.configs;

import com.hse.knopkabackend.models.entityforclick.EntityForClick;
import com.hse.knopkabackend.models.knopkauser.KnopkaUser;
import com.hse.knopkabackend.models.profile.Profile;
import com.hse.knopkabackend.repositories.entityforclick.EntityForClickRepository;
import com.hse.knopkabackend.repositories.knopkauser.KnopkaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Configuration
@PropertySource({"classpath:application.properties"})
@ComponentScan
@EnableJpaRepositories(
        basePackages = {"com.hse.knopkabackend.repositories.entityforclick"},
        entityManagerFactoryRef = "clickHouseKnopkaEntityManager",
        transactionManagerRef = "clickHouseKnopkaTransactionManager")
public class ClickHouseKnopkaConfig {

    @Autowired
    private Environment env;

//    @Primary
//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource postgresKnopkaDataSource() {
//        return DataSourceBuilder.create().build();
//    }

    @Primary
    @Bean
    public DataSource clickHouseKnopkaDataSource() {

        DriverManagerDataSource dataSource
                = new DriverManagerDataSource();
        dataSource.setDriverClassName(
                Objects.requireNonNull(env.getProperty("spring.clickhouse-datasource.driver-class-name")));
        dataSource.setUrl(env.getProperty("spring.clickhouse-datasource.url"));
        dataSource.setUsername(env.getProperty("spring.clickhouse-datasource.username"));
        dataSource.setPassword(env.getProperty("spring.clickhouse-datasource.password"));
//        dataSource.setConnectionProperties();

        return dataSource;
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean clickHouseKnopkaEntityManager() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(clickHouseKnopkaDataSource());
        em.setPackagesToScan(
                "com.hse.knopkabackend.models.entityforclick");

        HibernateJpaVendorAdapter vendorAdapter
                = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto",
                "none");
        properties.put("hibernate.dialect",
                "org.hibernate.dialect.MySQLDialect");
        em.setJpaPropertyMap(properties);

        return em;
    }


    @Primary
    @Bean
    public PlatformTransactionManager clickHouseKnopkaTransactionManager() {

        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                clickHouseKnopkaEntityManager().getObject());
        return transactionManager;
    }

    @Bean
    CommandLineRunner clickCommandLineRunner(EntityForClickRepository repository) {
        return args -> {
            if (repository.checkTable().get(0) != 1)
                try {
                    repository.createTable();
                } catch (Exception ignored) { //some incompability https://github.com/ClickHouse/ClickHouse/issues/8030 but still works
                }
        };
    }
}

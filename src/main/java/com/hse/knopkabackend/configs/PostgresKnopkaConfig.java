package com.hse.knopkabackend.configs;

import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Objects;

@Configuration
@PropertySource({"classpath:application.properties"})
@ComponentScan
@EnableJpaRepositories(
        basePackages = {"com.hse.knopkabackend.repositories.knopkauser",
                "com.hse.knopkabackend.repositories.description",
                "com.hse.knopkabackend.repositories.knopka",
                "com.hse.knopkabackend.repositories.profile"
        },
        entityManagerFactoryRef = "postgresKnopkaEntityManager",
        transactionManagerRef = "postgresKnopkaTransactionManager")
public class PostgresKnopkaConfig {

    @Autowired
    private Environment env;

    @Primary
    @Bean
    public DataSource postgresKnopkaDataSource() {

        DriverManagerDataSource dataSource
                = new DriverManagerDataSource();
        dataSource.setDriverClassName(
                Objects.requireNonNull(env.getProperty("spring.datasource.driver-class-name")));
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));

        return dataSource;
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean postgresKnopkaEntityManager() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(postgresKnopkaDataSource());
        em.setPackagesToScan(
                "com.hse.knopkabackend.models.knopkauser",
                "com.hse.knopkabackend.models.knopka",
                "com.hse.knopkabackend.models.profile",
                "com.hse.knopkabackend.models.description");

        HibernateJpaVendorAdapter vendorAdapter
                = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto",
                "none");
        properties.put("hibernate.dialect",
                "org.hibernate.dialect.PostgreSQLDialect");
        em.setJpaPropertyMap(properties);

        return em;
    }


    @Primary
    @Bean
    public PlatformTransactionManager postgresKnopkaTransactionManager() {

        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                postgresKnopkaEntityManager().getObject());
        return transactionManager;
    }

//    @Bean
//    CommandLineRunner commandLineRunner(KnopkaUserRepository repository) {
//        return args -> {
//            Profile BibaProfile = new Profile("Biba");
//            KnopkaUser Biba = new KnopkaUser("biba.com");
//            Biba.setToken("111");
//            Biba.setProfile(BibaProfile);
//            BibaProfile.setUser(Biba);
//            Profile BobaProfile = new Profile("Boba");
//            KnopkaUser Boba = new KnopkaUser("boba.com");
//            Boba.setToken("121");
//            Boba.setProfile(BobaProfile);
//            BobaProfile.setUser(Boba);
//            Profile AbobaProfile = new Profile("Aboba");
//            KnopkaUser Aboba = new KnopkaUser("aboba.com");
//            Aboba.setToken("333");
//            Aboba.setProfile(AbobaProfile);
//            AbobaProfile.setUser(Aboba);
//
//            repository.saveAll(List.of(Biba, Boba, Aboba));
//        };
//    }
}

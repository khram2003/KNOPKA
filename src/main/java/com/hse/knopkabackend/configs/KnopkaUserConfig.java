package com.hse.knopkabackend.configs;

import com.hse.knopkabackend.models.Profile;
import com.hse.knopkabackend.repositories.KnopkaUserRepository;
import com.hse.knopkabackend.models.KnopkaUser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class KnopkaUserConfig {

//    @Bean
//    @Primary
//    @ConfigurationProperties(prefix="spring.datasource")
//    public DataSource primaryDataSource() {
//        return DataSourceBuilder.create().build();
//    }

//    @Bean
//    @ConfigurationProperties(prefix="spring.datasourceClickhouse")
//    public DataSource secondaryDataSource() {
//        return DataSourceBuilder.create().build();
//    }

    @Bean
    CommandLineRunner commandLineRunner(KnopkaUserRepository repository) {
        return args -> {
            Profile BibaProfile = new Profile("Biba");
            KnopkaUser Biba = new KnopkaUser("biba.com");
            Biba.setToken("111");
            Biba.setProfile(BibaProfile);
            BibaProfile.setUser(Biba);
            Profile BobaProfile = new Profile("Boba");
            KnopkaUser Boba = new KnopkaUser("boba.com");
            Boba.setToken("121");
            Boba.setProfile(BobaProfile);
            BobaProfile.setUser(Boba);
            Profile AbobaProfile = new Profile("Aboba");
            KnopkaUser Aboba = new KnopkaUser("aboba.com");
            Aboba.setToken("333");
            Aboba.setProfile(AbobaProfile);
            AbobaProfile.setUser(Aboba);

            repository.saveAll(List.of(Biba, Boba, Aboba));
        };
    }

}

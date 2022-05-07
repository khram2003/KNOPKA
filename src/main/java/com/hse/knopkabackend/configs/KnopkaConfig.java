package com.hse.knopkabackend.configs;

import com.hse.knopkabackend.models.profile.Profile;
import com.hse.knopkabackend.repositories.knopkauser.KnopkaUserRepository;
import com.hse.knopkabackend.models.knopkauser.KnopkaUser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.List;

public class KnopkaConfig {

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

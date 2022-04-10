package com.hse.knopkabackend.configs;

import com.hse.knopkabackend.models.Profile;
import com.hse.knopkabackend.repositories.KnopkaUserRepository;
import com.hse.knopkabackend.models.KnopkaUser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class KnopkaUserConfig {

    @Bean
    CommandLineRunner commandLineRunner(KnopkaUserRepository repository) {
        return args -> {
            Profile BibaProfile = new Profile("Biba");
            KnopkaUser Biba = new KnopkaUser("biba.com");
            Biba.setProfile(BibaProfile);
            BibaProfile.setUser(Biba);
            Profile BobaProfile = new Profile("Boba");
            KnopkaUser Boba = new KnopkaUser("boba.com");
            Boba.setProfile(BobaProfile);
            BobaProfile.setUser(Boba);
            Profile AbobaProfile = new Profile("Aboba");
            KnopkaUser Aboba = new KnopkaUser("aboba.com");
            Aboba.setProfile(AbobaProfile);
            AbobaProfile.setUser(Aboba);

            repository.saveAll(List.of(Biba, Boba, Aboba));
        };
    }

}

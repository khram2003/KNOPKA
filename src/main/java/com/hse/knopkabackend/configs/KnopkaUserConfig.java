package com.hse.knopkaBackend.knopkaUser;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class KnopkaUserConfig {

    @Bean
    CommandLineRunner commandLineRunner(KnopkaUserRepository repository) {
        return args -> {
            KnopkaUser Biba = new KnopkaUser("Biba");
            KnopkaUser Boba = new KnopkaUser("Boba");
            KnopkaUser Aboba = new KnopkaUser("Aboba");

            repository.saveAll(List.of(Biba, Boba, Aboba));
        };
    }

}

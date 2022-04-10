package com.hse.knopkabackend.repositories;

import com.hse.knopkabackend.models.KnopkaUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KnopkaUserRepository extends JpaRepository<KnopkaUser, Long> {
    Optional<KnopkaUser> findKnopkaUserByEmail(String email);
}

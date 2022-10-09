package com.hse.knopkabackend.repositories.knopkauser;

import com.hse.knopkabackend.models.knopkauser.KnopkaUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KnopkaUserRepository extends JpaRepository<KnopkaUser, Long> {
    Optional<KnopkaUser> findKnopkaUserByEmail(String email);
    Optional<KnopkaUser> findKnopkaUserByToken(String token);
}

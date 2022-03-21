package com.hse.knopkabackend.repositories;

import com.hse.knopkabackend.models.KnopkaUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KnopkaUserRepository extends JpaRepository<KnopkaUser, Long> {

    @Query("SELECT s FROM knopka_user s WHERE s.nickname = ?1")
    Optional<KnopkaUser> findKnopkaUserByNickname(String nickname);
}

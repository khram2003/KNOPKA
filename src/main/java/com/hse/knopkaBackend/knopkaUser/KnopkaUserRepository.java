package com.hse.knopkaBackend.knopkaUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KnopkaUserRepository extends JpaRepository<KnopkaUser, Long> {

    @Query("SELECT s FROM KnopkaUser s WHERE s.nickname = ?1")
    Optional<KnopkaUser> findKnopkaUserByNickname(String nickname);
}

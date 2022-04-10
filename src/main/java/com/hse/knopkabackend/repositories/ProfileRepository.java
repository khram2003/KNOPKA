package com.hse.knopkabackend.repositories;

import com.hse.knopkabackend.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @Query("SELECT s FROM profile s WHERE s.nickname = ?1")
    Optional<Profile> findProfileByNickname(String nickname);
}

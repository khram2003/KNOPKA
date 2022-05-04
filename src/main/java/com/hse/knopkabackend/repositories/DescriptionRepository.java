package com.hse.knopkabackend.repositories;

import com.hse.knopkabackend.models.Description;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DescriptionRepository extends JpaRepository<Description, Long> {
//    List<Description> findDescriptionsByTags(List<String> tags);
}

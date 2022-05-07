package com.hse.knopkabackend.repositories.description;

import com.hse.knopkabackend.models.description.Description;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DescriptionRepository extends JpaRepository<Description, Long> {
//    List<Description> findDescriptionsByTags(List<String> tags);
}

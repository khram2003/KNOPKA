package com.hse.knopkabackend.repositories.description;

import com.hse.knopkabackend.additionalclasses.Tag;
import com.hse.knopkabackend.models.description.Description;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DescriptionRepository extends JpaRepository<Description, Long> {

    @Query(value = "SELECT v.description_knopka_id as descriptionId, v.tags as tags FROM tags v where v.tags = ?1", nativeQuery = true)
    List<Tag> findByTag(String tag);
}

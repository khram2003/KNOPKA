package com.hse.knopkabackend.repositories.entityforclick;

import com.hse.knopkabackend.models.entityforclick.EntityForClick;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityForClickRepository extends JpaRepository<EntityForClick, Long> {
}

package com.hse.knopkabackend.repositories.entityforclick;

import com.hse.knopkabackend.models.entityforclick.EntityForClick;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface EntityForClickRepository extends JpaRepository<EntityForClick, Long> {
    @Query(value = "EXISTS TABLE entityforclick", nativeQuery = true)
    List<Integer> checkTable();

    @Modifying
    @Transactional
    @Query(value = "create table entityforclick (click_id bigint, clicked_knopka_id bigint, region varchar(255), time_of_click varchar(255)) engine Memory as select *", nativeQuery = true)
    void createTable();

    @Query(value = "select * from entityforclick where clicked_knopka_id = :knopka_id", nativeQuery = true)
    List<?> getNumberOfClicks(@Param("knopka_id") Long knopka_id);
}

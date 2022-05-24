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
    @Modifying
    @Query(value = "INSERT INTO entityforclick(click_id, clicked_knopka_id, region, time_of_click) values (:click_id, :clicked_knopka_id, :region, :time_of_click)", nativeQuery = true)
    @Transactional
    void insertClick(@Param("click_id") Long click_id, @Param("clicked_knopka_id") Long clicked_knopka_id, @Param("region") String region, @Param("time_of_click") String time_of_click);

    @Query(value = "select * from entityforclick where clicked_knopka_id = :knopka_id", nativeQuery = true)
    List<?> getNumberOfClicks(@Param("knopka_id") Long knopka_id);

}

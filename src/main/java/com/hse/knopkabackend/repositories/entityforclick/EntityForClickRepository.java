package com.hse.knopkabackend.repositories.entityforclick;

import com.hse.knopkabackend.additionalclasses.Click;
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
    @Query(value = "EXISTS TABLE entityforbatch", nativeQuery = true)
    List<Integer> checkTable();

    @Modifying
    @Transactional
    @Query(value = "create table entityforbatch (author_id bigint, clicked_knopka_id bigint, pushes bigint, region varchar(255), time_of_click DateTime('UTC')) engine Memory as select *", nativeQuery = true)
    void createTable();

    @Query(value = "select sum(pushes) from entityforbatch where clicked_knopka_id = :knopkaId", nativeQuery = true)
    Long getNumberOfClicks(@Param("knopkaId") Long knopkaId);

    @Query(value = "select count() from (select * from entityforbatch where time_of_click = :timeOfClick AND clicked_knopka_id = :clickedKnopkaId AND author_id = :authorId) as e ", nativeQuery = true)
    Long getBatchByTime(@Param("timeOfClick") String timeOfClick, @Param("clickedKnopkaId") Long clickedKnopkaId, @Param("authorId") Long authorId);

    @Query(value = "select clicked_knopka_id from (select count(clicked_knopka_id) as val, clicked_knopka_id, region from entityforbatch group by region, clicked_knopka_id order by val desc) as e where region = :clickRegion", nativeQuery = true)
    List<Long> getTopByRegion(@Param("clickRegion") String clickRegion);
}

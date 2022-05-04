package com.hse.knopkabackend.repositories;

import com.hse.knopkabackend.models.Knopka;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface KnopkaRepository extends JpaRepository<Knopka, Long> {

}

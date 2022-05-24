package com.hse.knopkabackend.services;

import com.hse.knopkabackend.repositories.entityforclick.EntityForClickRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntityForClickService {
    private final EntityForClickRepository entityForClickRepository;

    @Autowired
    public EntityForClickService(EntityForClickRepository entityForClickRepository) {
        this.entityForClickRepository = entityForClickRepository;
    }


    public void registerNewClick(Long knopkaId) {
        entityForClickRepository.insertClick(1L, knopkaId, "bebrastan", "0000");
    }

    public int getClicks(Long knopkaId){
        return entityForClickRepository.getNumberOfClicks(knopkaId).size();
    }
}

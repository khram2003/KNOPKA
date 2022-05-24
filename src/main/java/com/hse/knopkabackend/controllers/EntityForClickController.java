package com.hse.knopkabackend.controllers;

import com.hse.knopkabackend.services.EntityForClickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/click")
public class EntityForClickController {

    private final EntityForClickService entityForClickService;

    @Autowired
    public EntityForClickController(EntityForClickService entityForClickService) {
        this.entityForClickService = entityForClickService;
    }

    @PostMapping
    public void registerNewClick(@RequestParam Long knopkaId) {
        entityForClickService.registerNewClick(knopkaId);
    }

    @GetMapping("/{knopkaId}")
    public int getKnopkasByIds(@PathVariable("knopkaId") Long knopkaId){
        return entityForClickService.getClicks(knopkaId);
    }
}

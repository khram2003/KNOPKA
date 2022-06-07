package com.hse.knopkabackend.controllers;

import com.hse.knopkabackend.DTO.BatchDTO;
import com.hse.knopkabackend.services.EntityForClickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/click")
public class EntityForClickController {

    private final EntityForClickService entityForClickService;

    @Autowired
    public EntityForClickController(EntityForClickService entityForClickService) {
        this.entityForClickService = entityForClickService;
    }


    @PostMapping("batch/{knopkaUserId}")
    public ResponseEntity<String> registerNewBatch(@PathVariable("knopkaUserId") Long knopkaUserId,
                                                   @RequestHeader String token,
                                                   @RequestBody BatchDTO batchDTO) {
        return entityForClickService.registerNewBatch(knopkaUserId, token, batchDTO);
    }

    @GetMapping("/{knopkaId}")
    public int getClicksByKnopkaId(@PathVariable("knopkaId") Long knopkaId,
                                   @RequestParam Long knopkaUserId,
                                   @RequestHeader String token) {
        return entityForClickService.getClicks(knopkaId, knopkaUserId, token);
    }

    @GetMapping("/top/{region}")
    public List<Long> getTopByRegion(@PathVariable("region") String region,
                                     @RequestParam Long knopkaUserId,
                                     @RequestHeader String token){
        return  entityForClickService.getTopByRegion(knopkaUserId, token, region);
    }
}

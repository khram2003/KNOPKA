package com.hse.knopkabackend.controllers;

import com.hse.knopkabackend.DTO.BatchDTO;
import com.hse.knopkabackend.DTO.BatchResponseDTO;
import com.hse.knopkabackend.services.EntityForClickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "api/v1/click")
public class EntityForClickController {

    private final EntityForClickService entityForClickService;

    @Autowired
    public EntityForClickController(EntityForClickService entityForClickService) {
        this.entityForClickService = entityForClickService;
    }


    @PostMapping("/batch")
    public ResponseEntity<String> registerNewBatch(
            @RequestHeader String token,
            @RequestBody BatchDTO batchDTO) {
        return entityForClickService.registerNewBatch(token, batchDTO);
    }

    @GetMapping("/{knopkaId}")
    public Long getClicksByKnopkaId(@PathVariable("knopkaId") Long knopkaId,
                                    @RequestHeader String token) {
        return entityForClickService.getClicks(knopkaId, token);
    }

    @GetMapping("/top/{region}")
    public List<Long> getTopByRegion(@PathVariable("region") String region,
                                     @RequestHeader String token) {
        return entityForClickService.getTopByRegion(token, region);
    }

    @GetMapping("/validregions")
    public Set<String> getValidRegions(@RequestHeader String token) {
        return entityForClickService.getValidRegions(token);
    }
}

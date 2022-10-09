package com.hse.knopkabackend.controllers;

import com.hse.knopkabackend.DTO.KnopkaDTO;
import com.hse.knopkabackend.models.description.Description;
import com.hse.knopkabackend.models.knopka.Knopka;
import com.hse.knopkabackend.services.KnopkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "api/v1/knopka")
public class KnopkaController {

    private final KnopkaService knopkaService;

    @Autowired
    public KnopkaController(KnopkaService knopkaService) {
        this.knopkaService = knopkaService;
    }


    //it will be removed later
    @GetMapping("/getall")
    public List<KnopkaDTO> getKnopkas(
            @RequestHeader String token) {
        return knopkaService.getKnopkas(token);
    }

    @GetMapping("/getbyids")
    public Set<KnopkaDTO> getKnopkasByIds(
            @RequestHeader String token,
            @RequestParam List<Long> ids) {
        return knopkaService.getKnopkaDTOs(token, ids);
    }

    @PostMapping
    public ResponseEntity<Long> createNewKnopka(@RequestBody KnopkaDTO knopkaDTO,
                                                @RequestHeader String token) {
        Knopka knopka = new Knopka(knopkaDTO.getName(), knopkaDTO.getPushes(), knopkaDTO.getStyle());
        Description description = new Description();
        description.setKnopka(knopka);
        knopka.setDescription(description);
        knopkaService.addNewKnopka(knopka, token, description);
        return new ResponseEntity<>(knopka.getKnopkaId(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{knopkaId}")
    public void deleteKnopka(@PathVariable("knopkaId") Long knopkaId,
                             @RequestHeader String token) {
        knopkaService.deleteKnopka(knopkaId, token);
    }


    @PutMapping(path = "/{knopkaId}")
    public void updateButton(
            @PathVariable("knopkaId") Long knopkaId,
            @RequestBody KnopkaDTO knopkaDTO,
            @RequestHeader String token) {
        if (knopkaDTO.getName() != null)
            knopkaService.updateKnopkaName(knopkaId, knopkaDTO.getName(), token);
        if (knopkaDTO.getStyle() != null)
            knopkaService.updateKnopkaStyle(knopkaId, knopkaDTO.getStyle(), token);
        if (knopkaDTO.getPushes() != null)
            knopkaService.updatePushesCount(knopkaId, knopkaDTO.getPushes(), token);
    }

}

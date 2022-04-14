package com.hse.knopkabackend.controllers;

import com.hse.knopkabackend.DTO.KnopkaDTO;
import com.hse.knopkabackend.additionalclasses.Style;
import com.hse.knopkabackend.models.Knopka;
import com.hse.knopkabackend.services.KnopkaService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping
    public List<Knopka> getKnopkas() {
        return knopkaService.getKnopkas();
    }

    @GetMapping("/getbyids")
    public Set<KnopkaDTO> getKnopkasByIds(@RequestParam Long knopkaUserId,
                                          @RequestHeader String token,
                                          @RequestParam List<Long> ids) {
        return knopkaService.getKnopkaDTOs(knopkaUserId, token, ids);
    }

    @PostMapping
    public void createNewKnopka(@RequestBody Knopka knopka,
                                @RequestHeader String token,
                                @RequestParam Long knopkaUserId) {
        knopkaService.addNewKnopka(knopka, token, knopkaUserId);
    }

    @DeleteMapping(path = "/{knopkaId}")
    public void deleteKnopkaUser(@PathVariable("knopkaId") Long knopkaId,
                                 @RequestHeader String token) {
        knopkaService.deleteKnopka(knopkaId, token);
    }

    @PutMapping(path = "/{knopkaId}")
    public void updateButton(@PathVariable("knopkaId") Long knopkaId,
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

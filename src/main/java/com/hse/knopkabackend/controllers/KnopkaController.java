package com.hse.knopkabackend.controllers;

import com.hse.knopkabackend.additionalclasses.Style;
import com.hse.knopkabackend.models.Knopka;
import com.hse.knopkabackend.services.KnopkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping(path = "api/v1/knopka")
public class KnopkaController {

    private final KnopkaService knopkaService;

    @Autowired
    public KnopkaController(KnopkaService knopkaService) {
        this.knopkaService = knopkaService;
    }

    @GetMapping
    public List<Knopka> getProfiles() {
        return knopkaService.getKnopkas();
    }

    @PostMapping
    public void createNewKnopka(@RequestBody Knopka knopka,
                                @RequestParam String token,
                                @RequestParam Long knopkaUserId) {
        knopkaService.addNewKnopka(knopka, token, knopkaUserId);
    }

    @DeleteMapping(path = "delete/{knopkaId}")
    public void deleteKnopkaUser(@PathVariable("knopkaId") Long knopkaId,
                                 @RequestParam String token) {
        knopkaService.deleteKnopka(knopkaId, token);
    }

    @PutMapping(path = "put/{knopkaId}")
    public void updateButton(@PathVariable("knopkaId") Long knopkaId,
                             @RequestParam(required = false) String name,
                             @RequestParam(required = false) Style style,
                             @RequestParam(required = false) Long n,
                             @RequestParam String token) {
        if (name != null)
            knopkaService.updateKnopkaName(knopkaId, name, token);
        if (style != null)
            knopkaService.updateKnopkaStyle(knopkaId, style, token);
        if (n != null)
            knopkaService.updatePushesCount(knopkaId, n, token);
    }

}

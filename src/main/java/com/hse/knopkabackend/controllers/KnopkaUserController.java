package com.hse.knopkabackend.controllers;

import com.hse.knopkabackend.services.KnopkaUserService;
import com.hse.knopkabackend.models.KnopkaUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/user")
public class KnopkaUserController {

    private final KnopkaUserService knopkaUserService;

    @Autowired
    public KnopkaUserController(KnopkaUserService knopkaUserService) {
        this.knopkaUserService = knopkaUserService;
    }

    @GetMapping
    public List<KnopkaUser> getUsers() {
        return knopkaUserService.getKnopkaUsers();
    }

    @PostMapping
    public void registerNewKnopkaUser(@RequestBody KnopkaUser knopkaUser) {
        knopkaUserService.addNewKnopkaUser(knopkaUser);
    }

    @DeleteMapping(path = "delete/{knopkaUserId}")
    public void deleteKnopkaUser(@PathVariable("knopkaUserId") Long knopkaUserId) {
        knopkaUserService.deleteKnopkaUser(knopkaUserId);
    }

    @PutMapping(path = "put/{knopkaUserId}")
    public void updateKnopkaUser(@PathVariable("knopkaUserId") Long knopkaUserId,
                                 @RequestParam(required = false) String nickname) {
        knopkaUserService.updateKnopkaUser(knopkaUserId, nickname);
    }
}

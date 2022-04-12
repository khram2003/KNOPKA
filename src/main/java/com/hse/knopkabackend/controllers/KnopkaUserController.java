package com.hse.knopkabackend.controllers;

import com.hse.knopkabackend.googleauth.Verifier;
import com.hse.knopkabackend.services.KnopkaUserService;
import com.hse.knopkabackend.models.KnopkaUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
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
    public ResponseEntity<Long> registerNewKnopkaUser(@RequestBody KnopkaUser knopkaUser) throws GeneralSecurityException, IOException {
        Verifier verifier = new Verifier();
        if (!verifier.isVerified(knopkaUser.getToken())){
            throw new GeneralSecurityException();
        }
        knopkaUserService.addNewKnopkaUser(knopkaUser);
        return new ResponseEntity<>(knopkaUser.getId(), HttpStatus.OK);
    }

    @DeleteMapping(path = "delete/{knopkaUserId}")
    public void deleteKnopkaUser(@PathVariable("knopkaUserId") Long knopkaUserId,
                                 @RequestParam String token) {
        knopkaUserService.deleteKnopkaUser(knopkaUserId, token);
    }

    @PutMapping(path = "put/{knopkaUserId}")
    public void updateKnopkaUser(@PathVariable("knopkaUserId") Long knopkaUserId,
                                 @RequestParam(required = false) String nickname,
                                 @RequestParam(required = false) String email,
                                 @RequestParam String token) {
        if (email != null)
            knopkaUserService.updateKnopkaUserEmail(knopkaUserId, email, token);
        if (nickname != null)
            knopkaUserService.updateKnopkaUserNickname(knopkaUserId, nickname, token);
    }
}

package com.hse.knopkabackend.controllers;

import com.hse.knopkabackend.DTO.KnopkaUserDTO;
import com.hse.knopkabackend.DTO.KnopkaUserResponseDTO;
import com.hse.knopkabackend.googleauth.Verifier;
import com.hse.knopkabackend.models.profile.Profile;
import com.hse.knopkabackend.services.KnopkaUserService;
import com.hse.knopkabackend.models.knopkauser.KnopkaUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "api/v1/user")
public class KnopkaUserController {

    private final KnopkaUserService knopkaUserService;

    @Autowired
    public KnopkaUserController(KnopkaUserService knopkaUserService) {
        this.knopkaUserService = knopkaUserService;
    }


    //it's here just for me. It will be removed later
    @GetMapping
    public List<KnopkaUser> getUsers() {
        return knopkaUserService.getKnopkaUsers();
    }

    @GetMapping("{knopkaUserId}/{friendsOfId}/friendsId")
    public Set<Long> getFriendsId(@PathVariable("knopkaUserId") Long knopkaUserId,
                                  @PathVariable("friendsOfId") Long friendsOfId,
                                  @RequestHeader String token) {
        return knopkaUserService.getKnopkaUsersFriends(knopkaUserId, friendsOfId, token);
    }

    @GetMapping("{knopkaUserId}/friends")
    public Set<KnopkaUserResponseDTO> getFriendsIdDTOs(@PathVariable("knopkaUserId") Long knopkaUserId,
                                                       @RequestHeader String token,
                                                       @RequestParam List<Long> friendsId) {
        return knopkaUserService.getKnopkaUsersFriendsDTOs(knopkaUserId, token, friendsId);
    }

    @GetMapping("{knopkaUserId}/{knopkasOfId}/knopkasId")
    public Set<Long> getKnopkasId(@PathVariable("knopkaUserId") Long knopkaUserId,
                                  @PathVariable("knopkasOfId") Long knopkasOfId,
                                  @RequestHeader String token) {
        return knopkaUserService.getKnopkaUsersKnopkaIds(knopkaUserId, knopkasOfId, token);
    }


    @PostMapping
    public ResponseEntity<Long> registerNewKnopkaUser(@RequestBody KnopkaUserDTO knopkaUserDTO) throws GeneralSecurityException,
            IOException {
        Verifier verifier = new Verifier();
        verifier.getVerifier().verify(knopkaUserDTO.getToken());
        KnopkaUser knopkaUser = new KnopkaUser(knopkaUserDTO.getEmail(), knopkaUserDTO.getToken());
        Profile profile = new Profile();
        profile.setUser(knopkaUser);
        knopkaUser.setProfile(profile);
        knopkaUserService.addNewKnopkaUser(knopkaUser, profile);
        return new ResponseEntity<>(knopkaUser.getId(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{knopkaUserId}")
    public void deleteKnopkaUser(@PathVariable("knopkaUserId") Long knopkaUserId,
                                 @RequestHeader String token) {
        knopkaUserService.deleteKnopkaUser(knopkaUserId, token);
    }

    @DeleteMapping(path = "/{knopkaUserId}/friends")
    public void deleteKnopkaUserFriend(@PathVariable("knopkaUserId") Long knopkaUserId,
                                       @RequestParam Long friendId,
                                       @RequestHeader String token) {
        knopkaUserService.deleteKnopkaUserFriend(knopkaUserId, friendId, token);
    }

    @PutMapping(path = "/{knopkaUserId}")
    public void updateKnopkaUser(@PathVariable("knopkaUserId") Long knopkaUserId,
                                 @RequestParam Long friendId,
                                 @RequestHeader String token) {
        knopkaUserService.updateKnopkaUserFriends(knopkaUserId, friendId, token);
    }
}

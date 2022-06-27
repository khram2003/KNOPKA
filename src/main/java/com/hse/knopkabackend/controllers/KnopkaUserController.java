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

    //TODO: it's here just for me. It will be removed later
    @GetMapping
    public List<KnopkaUser> getUsers() {
        return knopkaUserService.getKnopkaUsers();
    }

    @GetMapping("{friendsOfId}/friendsId")
    public Set<Long> getFriendsId(@PathVariable("friendsOfId") Long friendsOfId,
                                  @RequestHeader String token) {
        return knopkaUserService.getKnopkaUsersFriends(friendsOfId, token);
    }

    @GetMapping("/friends")
    public Set<KnopkaUserResponseDTO> getFriendsIdDTOs(@RequestHeader String token,
                                                       @RequestParam List<Long> friendsId) {
        return knopkaUserService.getKnopkaUsersFriendsDTOs(token, friendsId);
    }

    @GetMapping("{knopkasOfId}/knopkasId")
    public Set<Long> getKnopkasId(@PathVariable("knopkasOfId") Long knopkasOfId,
                                  @RequestHeader String token) {
        return knopkaUserService.getKnopkaUsersKnopkaIds(knopkasOfId, token);
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

    @DeleteMapping
    public void deleteKnopkaUser(
            @RequestHeader String token) {
        knopkaUserService.deleteKnopkaUser(token);
    }

    @DeleteMapping(path = "/friends")
    public void deleteKnopkaUserFriend(
                                       @RequestParam Long friendId,
                                       @RequestHeader String token) {
        knopkaUserService.deleteKnopkaUserFriend(friendId, token);
    }

    @PutMapping(path = "/{knopkaUserId}")
    public void updateKnopkaUser(@PathVariable("knopkaUserId") Long knopkaUserId,
                                 @RequestParam Long friendId,
                                 @RequestHeader String token) {
        knopkaUserService.updateKnopkaUserFriends(knopkaUserId, friendId, token);
    }
}

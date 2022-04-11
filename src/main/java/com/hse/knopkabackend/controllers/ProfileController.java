package com.hse.knopkabackend.controllers;

import com.hse.knopkabackend.models.Profile;
import com.hse.knopkabackend.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/profile")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public List<Profile> getProfiles() {
        return profileService.getProfiles();
    }

    @PostMapping
    public void registerNewProfile(@RequestBody Profile profile) {
        profileService.addNewProfile(profile);
    }

    @DeleteMapping(path = "delete/{profileKnopkaUserId}")
    public void deleteKnopkaUser(@PathVariable("profileKnopkaUserId") Long profileKnopkaUserId,
                                 @RequestParam String token) {
        profileService.deleteProfile(profileKnopkaUserId, token);
    }

    @PutMapping(path = "put/{profileKnopkaUserId}")
    public void updateProfileNickname(@PathVariable("profileKnopkaUserId") Long profileKnopkaUserId,
                                      @RequestParam(required = false) String nickname,
                                      @RequestParam(required = false) String bio,
                                      @RequestParam(required = false) MultipartFile photo,
                                      @RequestParam String token) {
        if (nickname != null)
            profileService.updateProfileNickname(profileKnopkaUserId, nickname, token);
        if (bio != null)
            profileService.updateProfileBio(profileKnopkaUserId, bio, token);
        if (photo != null)
            profileService.updateProfilePhoto(profileKnopkaUserId, photo, token);
    }

}

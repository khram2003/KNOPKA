package com.hse.knopkabackend.controllers;

import com.hse.knopkabackend.DTO.ProfileDTO;
import com.hse.knopkabackend.models.profile.Profile;
import com.hse.knopkabackend.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "api/v1/profile")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{profileId}")
    public ProfileDTO getProfile(@PathVariable("profileId") Long profileId,
                                 @RequestHeader String token) {
        Profile profile = profileService.getProfile(profileId, token);
        return new ProfileDTO(profile.getNickname(), profile.getBio(), profile.getEncodedPhoto());
    }


    @PutMapping(path = "/{profileKnopkaUserId}")
    public void updateProfile(@PathVariable("profileKnopkaUserId") Long profileKnopkaUserId,
                              @RequestBody ProfileDTO profileDTO,
                              @RequestHeader String token) {
        if (profileDTO.getNickname() != null)
            profileService.updateProfileNickname(profileKnopkaUserId, profileDTO.getNickname(), token);
        if (profileDTO.getBio() != null)
            profileService.updateProfileBio(profileKnopkaUserId, profileDTO.getBio(), token);
        if (profileDTO.getPhoto() != null)
            profileService.updateProfilePhoto(profileKnopkaUserId, profileDTO.getPhoto(), token);
    }

}

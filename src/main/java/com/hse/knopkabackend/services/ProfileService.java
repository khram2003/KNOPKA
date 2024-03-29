package com.hse.knopkabackend.services;

import com.hse.knopkabackend.models.knopkauser.KnopkaUser;
import com.hse.knopkabackend.models.profile.Profile;
import com.hse.knopkabackend.repositories.knopkauser.KnopkaUserRepository;
import com.hse.knopkabackend.repositories.profile.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final KnopkaUserRepository knopkaUserRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository, KnopkaUserRepository knopkaUserRepository) {
        this.profileRepository = profileRepository;
        this.knopkaUserRepository = knopkaUserRepository;
    }

    public Profile getProfile(Long profileId, String token) {

        Profile profile = profileRepository.findById(profileId).orElseThrow(
                () -> new IllegalStateException("profileKnopkaUser with id: " + profileId + " doesn't exist")
        );

        KnopkaUser knopkaUser = knopkaUserRepository.findKnopkaUserByToken(token).orElseThrow(
                () -> new IllegalStateException("token is invalid")
        );
        return profile;
    }


    @Transactional("postgresKnopkaTransactionManager")
    public void updateProfileNickname(Long profileKnopkaUserId, String nickname, String token) {
        Profile profile = profileRepository.findById(profileKnopkaUserId).orElseThrow(
                () -> new IllegalStateException("profileKnopkaUser with id: " + profileKnopkaUserId + " doesn't exist")
        );
        if (nickname != null) {
            Optional<Profile> profileByNickname = profileRepository.findProfileByNickname(nickname);
            if (profileByNickname.isPresent()) {
                throw new IllegalStateException("Oops! Nickname '" +
                        nickname + "' is already taken. Try another one."
                );
            }

            if (!nickname.isBlank() && nickname.length() <= 255 &&
                    Objects.equals(profile.getUser().getToken(), token)) {
                profile.setNickname(nickname);
                System.out.println("Changed KnopkaUser's nickname with id: " +
                        profileKnopkaUserId + " to: '" + nickname + "'"
                );
            } else {
                throw new IllegalStateException("Your new nickname '" +
                        nickname + "' is not valid or token is invalid. Please choose another one"
                );
            }

        }
    }

    @Transactional("postgresKnopkaTransactionManager")
    public void updateProfileBio(Long profileKnopkaUserId, String bio, String token) {
        Profile profile = profileRepository.findById(profileKnopkaUserId).orElseThrow(
                () -> new IllegalStateException("profileKnopkaUser with id: " + profileKnopkaUserId + " doesn't exist")
        );
        if (!Objects.equals(profile.getUser().getToken(), token))
            throw new IllegalStateException("Token is invalid");
        if (bio.length() <= 255) {
            profile.setBio(bio);
        }

    }

    @Transactional("postgresKnopkaTransactionManager")
    public void updateProfilePhoto(Long profileKnopkaUserId, String photo, String token) {
        Profile profile = profileRepository.findById(profileKnopkaUserId).orElseThrow(
                () -> new IllegalStateException("profileKnopkaUser with id: " + profileKnopkaUserId + " doesn't exist")
        );
        if (!Objects.equals(profile.getUser().getToken(), token))
            throw new IllegalStateException("Token is invalid");
        if (photo.length() <= 2097152) {
            profile.setEncodedPhoto(photo);
        } else {
            throw new IllegalStateException("Photo size is too large");
        }

    }
}


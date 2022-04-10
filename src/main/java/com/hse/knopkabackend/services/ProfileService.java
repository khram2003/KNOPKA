package com.hse.knopkabackend.services;

import com.hse.knopkabackend.models.Profile;
import com.hse.knopkabackend.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public List<Profile> getProfiles() {
        return profileRepository.findAll();
    }

    public void addNewProfile(Profile profile) {
        Optional<Profile> profileByNickname = profileRepository.findProfileByNickname(
                profile.getNickname()
        );
        // IMHO here should be any new account validation logic
        if (profileByNickname.isPresent()) {
            throw new IllegalStateException("Oops! Nickname '" +
                    profile.getNickname() + "' is already taken. Try another one."
            );
        }
        profileRepository.save(profile);
        System.out.println("Added user with unique nickname: ");
    }


    public void deleteProfile(Long knopkaUserId) {
        boolean exists = profileRepository.existsById(knopkaUserId);
        if (!exists) {
            throw new IllegalStateException("KnopkaUser with id: " + knopkaUserId + " doesn't exist");
        }
        profileRepository.deleteById(knopkaUserId);
        System.out.println("Deleted KnopkaUser with id: " + knopkaUserId);
    }

    @Transactional
    public void updateProfileNickname(Long profileKnopkaUserId, String nickname) {
        Profile profile = profileRepository.findById(profileKnopkaUserId).orElseThrow(
                () -> new IllegalStateException("profileKnopkaUser with id: " + profileKnopkaUserId + " doesn't exist")
        );
        if (nickname != null) {
            Optional<Profile> knopkaUserByNickname = profileRepository.findProfileByNickname(nickname);
            if (knopkaUserByNickname.isPresent()) {
                throw new IllegalStateException("Oops! Nickname '" +
                        nickname + "' is already taken. Try another one."
                );
            }
            if (!nickname.isBlank() && !profile.getNickname().equals(nickname) && nickname.length() <= 255) {
                profile.setNickname(nickname);
                System.out.println("Changed KnopkaUser's nickname with id: " +
                        profileKnopkaUserId + " to: '" + nickname + "'"
                );
            } else {
                throw new IllegalStateException("Your new nickname '" +
                        nickname + "' is not valid. Please chose another one"
                );
            }
        }
    }

    @Transactional
    public void updateProfileBio(Long profileKnopkaUserId, String bio) {
        Profile profile = profileRepository.findById(profileKnopkaUserId).orElseThrow(
                () -> new IllegalStateException("profileKnopkaUser with id: " + profileKnopkaUserId + " doesn't exist")
        );
        if (bio.length() <= 255) {
            profile.setBio(bio);
        }

    }

    @Transactional
    public void updateProfilePhoto(Long profileKnopkaUserId, MultipartFile photo) {
        Profile profile = profileRepository.findById(profileKnopkaUserId).orElseThrow(
                () -> new IllegalStateException("profileKnopkaUser with id: " + profileKnopkaUserId + " doesn't exist")
        );
        //photo up to 2MB
        if (photo.getSize() <= 2097152) {
            profile.setEncodedPhoto(photo);
        }

    }
}


package com.hse.knopkabackend.services;

import com.hse.knopkabackend.models.KnopkaUser;
import com.hse.knopkabackend.models.Profile;
import com.hse.knopkabackend.repositories.KnopkaUserRepository;
import com.hse.knopkabackend.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class KnopkaUserService {

    private final KnopkaUserRepository knopkaUserRepository;
    private final ProfileRepository profileKnopkaUserRepository;

    @Autowired
    public KnopkaUserService(KnopkaUserRepository knopkaUserRepository, ProfileRepository profileKnopkaUserRepository) {
        this.knopkaUserRepository = knopkaUserRepository;
        this.profileKnopkaUserRepository = profileKnopkaUserRepository;
    }

    public List<KnopkaUser> getKnopkaUsers() {
        return knopkaUserRepository.findAll();
    }

    public void addNewKnopkaUser(KnopkaUser knopkaUser) {
        Optional<Profile> profileKnopkaUserByNickname = profileKnopkaUserRepository.findProfileByNickname(
                knopkaUser.getProfile().getNickname()
        );
        if (profileKnopkaUserByNickname.isPresent()) {
            throw new IllegalStateException("Oops! Nickname '" +
                    knopkaUser.getProfile().getNickname() + "' is already taken. Try another one."
            );
        }
        Optional<KnopkaUser> knopkaUserByEmail = knopkaUserRepository.findKnopkaUserByEmail(
                knopkaUser.getEmail()
        );
        if (knopkaUserByEmail.isPresent()) {
            throw new IllegalStateException("Oops! Email '" +
                    knopkaUser.getEmail() + "' is already taken. Try another one."
            );
        }
        knopkaUserRepository.save(knopkaUser);
        System.out.println("Added user with unique nickname: " + knopkaUser);
    }


    public void deleteKnopkaUser(Long knopkaUserId) {
        boolean exists = knopkaUserRepository.existsById(knopkaUserId);
        if (!exists) {
            throw new IllegalStateException("KnopkaUser with id: " + knopkaUserId + " doesn't exist");
        }
        knopkaUserRepository.deleteById(knopkaUserId);
        System.out.println("Deleted KnopkaUser with id: " + knopkaUserId);
    }

    @Transactional
    public void updateKnopkaUserNickname(Long knopkaUserId, String nickname) {
        KnopkaUser knopkaUser = knopkaUserRepository.findById(knopkaUserId).orElseThrow(
                () -> new IllegalStateException("KnopkaUser with id: " + knopkaUserId + " doesn't exist")
        );
        if (nickname != null) {
            Optional<Profile> knopkaUserByNickname = profileKnopkaUserRepository.findProfileByNickname(nickname);
            if (knopkaUserByNickname.isPresent()) {
                throw new IllegalStateException("Oops! Nickname '" +
                        nickname + "' is already taken. Try another one."
                );
            }
            if (!nickname.isBlank() && !knopkaUser.getProfile().getNickname().equals(nickname)) {
                knopkaUser.getProfile().setNickname(nickname);
                System.out.println("Changed KnopkaUser's nickname with id: " +
                        knopkaUserId + " to: '" + nickname + "'"
                );
            } else {
                throw new IllegalStateException("Your new nickname '" +
                        nickname + "' is not valid. Please chose another one"
                );
            }
        }
    }

    @Transactional
    public void updateKnopkaUserEmail(Long knopkaUserId, String email) {
        KnopkaUser knopkaUser = knopkaUserRepository.findById(knopkaUserId).orElseThrow(
                () -> new IllegalStateException("KnopkaUser with id: " + knopkaUserId + " doesn't exist")
        );
        if (email != null) {
            Optional<KnopkaUser> knopkaUserByEmail = knopkaUserRepository.findKnopkaUserByEmail(email);
            if (knopkaUserByEmail.isPresent()) {
                throw new IllegalStateException("Oops! Email '" +
                        email + "' is already taken. Try another one."
                );
            }
            if (!email.isBlank() && !knopkaUser.getEmail().equals(email)) {
                //TODO: validate email
                knopkaUser.setEmail(email);
                System.out.println("Changed KnopkaUser's email with id: " +
                        knopkaUserId + " to: '" + email + "'"
                );
            } else {
                throw new IllegalStateException("Your new email '" +
                        email + "' is not valid. Please chose another one"
                );
            }
        }
    }
}

package com.hse.knopkabackend.services;

import com.hse.knopkabackend.DTO.KnopkaUserResponseDTO;
import com.hse.knopkabackend.models.knopkauser.KnopkaUser;
import com.hse.knopkabackend.models.profile.Profile;
import com.hse.knopkabackend.repositories.knopkauser.KnopkaUserRepository;
import com.hse.knopkabackend.repositories.profile.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    public void addNewKnopkaUser(KnopkaUser knopkaUser, Profile profile) {
        Optional<KnopkaUser> knopkaUserByEmail = knopkaUserRepository.findKnopkaUserByEmail(
                knopkaUser.getEmail()
        );
        if (knopkaUserByEmail.isPresent()) {
            throw new IllegalStateException("Oops! Email '" +
                    knopkaUser.getEmail() + "' is already taken. Try another one."
            );
        }
        knopkaUserRepository.save(knopkaUser);
        profile.setUserId(knopkaUser.getId());
        profileKnopkaUserRepository.save(profile);
        System.out.println("Added user with unique email: " + knopkaUser);
    }


    public void deleteKnopkaUser(String token) {
        KnopkaUser knopkaUserById = knopkaUserRepository.findKnopkaUserByToken(token).orElseThrow(
                () -> new IllegalStateException("Token is not valid")
        );

        knopkaUserRepository.deleteById(knopkaUserById.getId());

    }


    @Transactional("postgresKnopkaTransactionManager")
    public void updateKnopkaUserFriends(Long knopkaUserId, Long friendId, String token) {
        if (Objects.equals(friendId, knopkaUserId)) {
            throw new IllegalStateException("No");
        }
        KnopkaUser knopkaUser = knopkaUserRepository.findById(knopkaUserId).orElseThrow(
                () -> new IllegalStateException("KnopkaUser with id: " + knopkaUserId + " doesn't exist")
        );

        KnopkaUser knopkaUserFriend = knopkaUserRepository.findById(friendId).orElseThrow(
                () -> new IllegalStateException("KnopkaUser with id: " + friendId + " doesn't exist")
        );
        if (Objects.equals(token, knopkaUser.getToken())) {
            knopkaUser.getFriends().add(friendId);
            System.out.println(knopkaUserId + " added " + friendId + " as friend!"
            );
        } else {
            throw new IllegalStateException("Your token is invalid. Please chose another one");
        }
    }

    public Set<Long> getKnopkaUsersFriends(Long friendsOfId, String token) {

        KnopkaUser knopkaUser = knopkaUserRepository.findKnopkaUserByToken(token).orElseThrow(
                () -> new IllegalStateException("Your token is invalid.")
        );

        KnopkaUser friendsOfUser = knopkaUserRepository.findById(friendsOfId).orElseThrow(
                () -> new IllegalStateException("KnopkaUser with id: " + friendsOfId + " doesn't exist")
        );
        return friendsOfUser.getFriends();
    }

    public Set<Long> getKnopkaUsersKnopkaIds(Long knopkasOfId, String token) {
        KnopkaUser knopkaUser = knopkaUserRepository.findKnopkaUserByToken(token).orElseThrow(
                () -> new IllegalStateException("Token is invalid")
        );
        KnopkaUser knopkasOfUser = knopkaUserRepository.findById(knopkasOfId).orElseThrow(
                () -> new IllegalStateException("KnopkaUser with id: " + knopkasOfId + " doesn't exist")
        );
        return knopkasOfUser.getKnopkaIds();
    }

    public void deleteKnopkaUserFriend(Long friendId, String token) {
        KnopkaUser knopkaUser = knopkaUserRepository.findKnopkaUserByToken(token).orElseThrow(
                () -> new IllegalStateException("token is invalid")
        );
        KnopkaUser friend = knopkaUserRepository.findById(friendId).orElseThrow(
                () -> new IllegalStateException("KnopkaUser with id: " + friendId + " doesn't exist")
        );
        if (knopkaUser.getFriends().contains(friendId)) {
            knopkaUser.getFriends().remove(friendId);
        } else {
            throw new IllegalStateException("It is not friend");
        }

    }


    public Set<KnopkaUserResponseDTO> getKnopkaUsersFriendsDTOs(String token, List<Long> friendsId) {
        KnopkaUser knopkaUser = knopkaUserRepository.findKnopkaUserByToken(token).orElseThrow(
                () -> new IllegalStateException("Token is not valid")
        );

        Set<KnopkaUserResponseDTO> resSet = new HashSet<>();

        for (var id : friendsId) {
            if (knopkaUser.getFriends().contains(id)) {
                KnopkaUser friend = knopkaUserRepository.findById(id).orElseThrow(
                        () -> new IllegalStateException("KnopkaUser with id: " + id + " doesn't exist")
                );
                resSet.add(new KnopkaUserResponseDTO(friend.getProfile().getNickname(), friend.getProfile().getEncodedPhoto(), id));
            }
        }
        return resSet;
    }

}

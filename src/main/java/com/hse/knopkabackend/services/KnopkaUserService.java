package com.hse.knopkabackend.services;

import com.hse.knopkabackend.models.KnopkaUser;
import com.hse.knopkabackend.repositories.KnopkaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class KnopkaUserService {

    private final KnopkaUserRepository knopkaUserRepository;

    @Autowired
    public KnopkaUserService(KnopkaUserRepository knopkaUserRepository) {
        this.knopkaUserRepository = knopkaUserRepository;
    }

    public List<KnopkaUser> getKnopkaUsers() {
        return knopkaUserRepository.findAll();
    }

    public void addNewKnopkaUser(KnopkaUser knopkaUser) {
        Optional<KnopkaUser> knopkaUserByNickname = knopkaUserRepository.findKnopkaUserByNickname(
                knopkaUser.getNickname()
        );
        // IMHO here should be any new account validation logic
        if (knopkaUserByNickname.isPresent()) {
            throw new IllegalStateException("Oops! Nickname '" +
                    knopkaUser.getNickname() + "' is already taken. Try another one."
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
    public void updateKnopkaUser(Long knopkaUserId, String nickname) {
        KnopkaUser knopkaUser = knopkaUserRepository.findById(knopkaUserId).orElseThrow(
                () -> new IllegalStateException("KnopkaUser with id: " + knopkaUserId + " doesn't exist")
        );
        if (nickname != null) {
            Optional<KnopkaUser> knopkaUserByNickname = knopkaUserRepository.findKnopkaUserByNickname(nickname);
            if (knopkaUserByNickname.isPresent()) {
                throw new IllegalStateException("Oops! Nickname '" +
                        nickname + "' is already taken. Try another one."
                );
            }
            if (!nickname.isBlank() && !knopkaUser.getNickname().equals(nickname)) {
                knopkaUser.setNickname(nickname);
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
}

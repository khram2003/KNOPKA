package com.hse.knopkabackend.services;

import com.hse.knopkabackend.additionalclasses.Style;
import com.hse.knopkabackend.models.Knopka;
import com.hse.knopkabackend.models.KnopkaUser;
import com.hse.knopkabackend.repositories.KnopkaRepository;
import com.hse.knopkabackend.repositories.KnopkaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class KnopkaService {

    private final KnopkaRepository knopkaRepository;
    private final KnopkaUserRepository knopkaUserRepository;

    @Autowired
    public KnopkaService(KnopkaRepository knopkaRepository, KnopkaUserRepository knopkaUserRepository) {
        this.knopkaRepository = knopkaRepository;
        this.knopkaUserRepository = knopkaUserRepository;
    }

    public List<Knopka> getKnopkas() {
        return knopkaRepository.findAll();
    }

    public void addNewKnopka(Knopka knopka, String token, Long knopkaUserId) {
        KnopkaUser knopkaUserById = knopkaUserRepository.findById(knopkaUserId).orElseThrow(() -> {
            throw new IllegalStateException("Invalid id");
        });
        if (Objects.equals(token, knopkaUserById.getToken())) {
            knopka.setUser(knopkaUserById);
            knopkaUserById.getKnopkas().add(knopka);
            knopkaRepository.save(knopka);
            System.out.println("Added knopka to user with nickname: " + knopka.getUser().getProfile().getNickname());
        } else {
            throw new IllegalStateException("Token is invalid");
        }
    }


    public void deleteKnopka(Long knopkaId, String token) {
        boolean exists = knopkaRepository.existsById(knopkaId);
        if (!exists) {
            throw new IllegalStateException("Knopka with id: " + knopkaId + " doesn't exist");
        }
        Optional<Knopka> knopka = knopkaRepository.findById(knopkaId);
        if (knopka.isPresent() && Objects.equals(knopka.get().getUser().getToken(), token)) {
            knopkaRepository.deleteById(knopkaId);
            System.out.println("Deleted Knopka with id: " + knopkaId);
        } else {
            throw new IllegalStateException("Token is invalid");
        }
    }

    @Transactional
    public void updateKnopkaName(Long knopkaId, String name, String token) {
        Knopka knopka = knopkaRepository.findById(knopkaId).orElseThrow(
                () -> new IllegalStateException("knopka with id: " + knopkaId + " doesn't exist")
        );
        if (name != null) {
            if (!name.isBlank() && name.length() <= 255 && Objects.equals(knopka.getUser().getToken(), token)) {
                knopka.setName(name);
                System.out.println("Changed Knopka's name with id: " +
                        knopkaId + " to: '" + name + "'"
                );
            } else {
                throw new IllegalStateException("Your new name '" +
                        name + "' is not valid or token is invalid. Please chose another one"
                );
            }

        }
    }

//    @Transactional
//    public void updateDescription(Long knopkaId, Description, String token) {
//        Knopka knopka = knopkaRepository.findById(knopkaId).orElseThrow(
//                () -> new IllegalStateException("knopka with id: " + knopkaId + " doesn't exist")
//        );
//        if (name != null) {
//            if (!name.isBlank() && name.length() <= 255 && Objects.equals(knopka.getUser().getToken(), token)) {
//                knopka.setName(name);
//                System.out.println("Changed Knopka's name with id: " +
//                        knopkaId + " to: '" + name + "'"
//                );
//            } else {
//                throw new IllegalStateException("Your new name '" +
//                        name + "' is not valid or token is invalid. Please chose another one"
//                );
//            }
//
//        }
//    }

    @Transactional
    public void updateKnopkaStyle(Long knopkaId, Style style, String token) {
        Knopka knopka = knopkaRepository.findById(knopkaId).orElseThrow(
                () -> new IllegalStateException("knopka with id: " + knopkaId + " doesn't exist")
        );
        if (style != null) {
            if (Objects.equals(knopka.getUser().getToken(), token)) {
                knopka.setStyle(style);
                System.out.println("Changed Knopka's style with id: " +
                        knopkaId
                );
            } else {
                throw new IllegalStateException("Your token is invalid. Please chose another one"
                );
            }

        }
    }

    @Transactional
    public void updatePushesCount(Long knopkaId, Long n, String token) {
        Knopka knopka = knopkaRepository.findById(knopkaId).orElseThrow(
                () -> new IllegalStateException("knopka with id: " + knopkaId + " doesn't exist")
        );
        if (n != null) {
            if (Objects.equals(knopka.getUser().getToken(), token)) {
                knopka.setPushesCounter(n);
                System.out.println("Changed Knopka's pushes with id: " +
                        knopkaId + " to: '" + n + "'"
                );
            } else {
                throw new IllegalStateException("Your token is invalid. Please chose another one"
                );
            }

        }
    }
}


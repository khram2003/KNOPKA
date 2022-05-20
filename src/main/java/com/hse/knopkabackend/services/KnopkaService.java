package com.hse.knopkabackend.services;

import com.hse.knopkabackend.DTO.KnopkaDTO;
import com.hse.knopkabackend.additionalclasses.Style;
import com.hse.knopkabackend.models.description.Description;
import com.hse.knopkabackend.models.knopka.Knopka;
import com.hse.knopkabackend.models.knopkauser.KnopkaUser;
import com.hse.knopkabackend.repositories.description.DescriptionRepository;
import com.hse.knopkabackend.repositories.knopka.KnopkaRepository;
import com.hse.knopkabackend.repositories.knopkauser.KnopkaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class KnopkaService {

    private final KnopkaRepository knopkaRepository;
    private final KnopkaUserRepository knopkaUserRepository;
    private final DescriptionRepository descriptionRepository;

    @Autowired
    public KnopkaService(KnopkaRepository knopkaRepository, KnopkaUserRepository knopkaUserRepository,
                         DescriptionRepository descriptionRepository) {
        this.knopkaRepository = knopkaRepository;
        this.knopkaUserRepository = knopkaUserRepository;
        this.descriptionRepository = descriptionRepository;
    }

    public List<Knopka> getKnopkas() {
        return knopkaRepository.findAll();
    }

    public void addNewKnopka(Knopka knopka, String token, Long knopkaUserId, Description description) {
        KnopkaUser knopkaUserById = knopkaUserRepository.findById(knopkaUserId).orElseThrow(() -> {
            throw new IllegalStateException("Invalid id");
        });
        if (Objects.equals(token, knopkaUserById.getToken())) {
            knopka.setCreatedAt(LocalDateTime.now());
            knopka.setUser(knopkaUserById);
            knopkaRepository.save(knopka);
            knopkaUserById.getKnopkas().add(knopka);
            description.setKnopkaWithThisKnopkaId(knopka.getKnopkaId());
            descriptionRepository.save(description);

            System.out.println("Added knopka to user with nickname: " + knopka.getUser().getProfile().getNickname());
        } else {
            throw new IllegalStateException("Token is invalid");
        }
    }


    public void deleteKnopka(Long knopkaId, String token) {
        Knopka knopka = knopkaRepository.findById(knopkaId).orElseThrow(
                () -> new IllegalStateException("knopka with id: " + knopkaId + " doesn't exist")
        );
        if (Objects.equals(knopka.getUser().getToken(), token)) {
            knopka.getUser().getKnopkaIds().remove(knopkaId);
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
    public void updatePushesCount(Long knopkaUserId, Long knopkaId, Long n, String token) {
        Knopka knopka = knopkaRepository.findById(knopkaId).orElseThrow(
                () -> new IllegalStateException("knopka with id: " + knopkaId + " doesn't exist")
        );

        KnopkaUser knopkaUser = knopkaUserRepository.findById(knopkaUserId).orElseThrow(
                () -> new IllegalStateException("User with id: " + knopkaId + " doesn't exist")
        );

        if (n != null) {
            if (Objects.equals(knopkaUser.getToken(), token)) {
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

    public Set<KnopkaDTO> getKnopkaDTOs(Long knopkaUserId, String token, List<Long> ids) {
        KnopkaUser knopkaUser = knopkaUserRepository.findById(knopkaUserId).orElseThrow(
                () -> new IllegalStateException("KnopkaUser with id: " + knopkaUserId + " doesn't exist")
        );

        if (!Objects.equals(knopkaUser.getToken(), token)) {
            throw new IllegalStateException("Token is not valid");
        }

        Set<KnopkaDTO> resSet = new HashSet<>();

        for (var id : ids) {
            Knopka knopka = knopkaRepository.findById(id).orElseThrow(
                    () -> new IllegalStateException("knopka with id: " + id + " doesn't exist")
            );
            resSet.add(new KnopkaDTO(knopka.getName(), knopka.getStyle(), knopka.getPushesCounter(), knopka.getKnopkaId()/*, knopka.getCreatedAt()*/));
        }
        return resSet;
    }
}


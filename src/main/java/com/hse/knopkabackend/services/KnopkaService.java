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

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    public List<KnopkaDTO> getKnopkas(String token) {
        KnopkaUser knopkaUserById = knopkaUserRepository.findKnopkaUserByToken(token).orElseThrow(() -> {
            throw new IllegalStateException("Invalid token");
        });

        List<Knopka> knopkaList = knopkaRepository.findAll();
        List<KnopkaDTO> knopkaDTOList = new ArrayList<>();
        for (var knopka : knopkaList)
            knopkaDTOList.add(new KnopkaDTO(knopka.getName(), knopka.getStyle(), knopka.getPushesCounter(), knopka.getKnopkaId(), knopka.getUser().getId()));
        return knopkaDTOList;
    }

    public void addNewKnopka(Knopka knopka, String token,Description description) {
        KnopkaUser knopkaUserById = knopkaUserRepository.findKnopkaUserByToken(token).orElseThrow(() -> {
            throw new IllegalStateException("Invalid token");
        });
            knopka.setCreatedAt(LocalDateTime.now());
            knopka.setUser(knopkaUserById);
            knopkaRepository.save(knopka);
            knopkaUserById.getKnopkas().add(knopka);
            description.setKnopkaWithThisKnopkaId(knopka.getKnopkaId());
            descriptionRepository.save(description);

            System.out.println("Added knopka to user with nickname: " + knopka.getUser().getProfile().getNickname());
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

    @Transactional("postgresKnopkaTransactionManager")
    public void updatePushesCount(Long knopkaId, Long n, String token) {
        Knopka knopka = knopkaRepository.findById(knopkaId).orElseThrow(
                () -> new IllegalStateException("knopka with id: " + knopkaId + " doesn't exist")
        );

        KnopkaUser knopkaUser = knopkaUserRepository.findKnopkaUserByToken(token).orElseThrow(
                () -> new IllegalStateException("User with id: " + knopkaId + " doesn't exist")
        );

        if (n != null) {
            if (Objects.equals(knopkaUser.getToken(), token)) {
                knopka.setPushesCounter(knopka.getPushesCounter() + n);
                System.out.println("Changed Knopka's pushes with id: " +
                        knopkaId + " to: '" + knopka.getPushesCounter() + n + "'"
                );
            } else {
                throw new IllegalStateException("Your token is invalid. Please chose another one"
                );
            }

        }
    }

    public Set<KnopkaDTO> getKnopkaDTOs(String token, List<Long> ids) {
        KnopkaUser knopkaUser = knopkaUserRepository.findKnopkaUserByToken(token).orElseThrow(
                () -> new IllegalStateException("Token is not valid")
        );

        List<Knopka> knopkaList = knopkaRepository.findAllById(ids);
        return knopkaList.stream().map(x -> new KnopkaDTO(x.getName(), x.getStyle(), x.getPushesCounter(), x.getKnopkaId(), x.getUser().getId())).collect(Collectors.toSet());
    }
}


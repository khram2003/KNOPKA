package com.hse.knopkabackend.services;

import com.hse.knopkabackend.models.Description;
import com.hse.knopkabackend.models.KnopkaUser;
import com.hse.knopkabackend.repositories.DescriptionRepository;
import com.hse.knopkabackend.repositories.KnopkaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class DescriptionService {

    private final DescriptionRepository descriptionRepository;
    private final KnopkaUserRepository knopkaUserRepository;

    @Autowired
    public DescriptionService(DescriptionRepository descriptionRepository, KnopkaUserRepository knopkaUserRepository) {
        this.descriptionRepository = descriptionRepository;
        this.knopkaUserRepository = knopkaUserRepository;
    }

    public Description getDescription(Long knopkaId, String token, Long knopkaUserId) {

        Description description = descriptionRepository.findById(knopkaId).orElseThrow(
                () -> new IllegalStateException("descriptionKnopka with id: " + knopkaId + " doesn't exist")
        );

        KnopkaUser knopkaUser = knopkaUserRepository.findById(knopkaUserId).orElseThrow(
                () -> new IllegalStateException("no user with this id")
        );

        if (Objects.equals(token, knopkaUser.getToken())) {
            return description;
        } else {
            throw new IllegalStateException("Token is invalid");
        }
    }


    @Transactional
    public void updateDescriptionText(Long descriptionKnopkaId, String text, String token) {
        Description description = descriptionRepository.findById(descriptionKnopkaId).orElseThrow(
                () -> new IllegalStateException("descriptionKnopka with id: " + descriptionKnopkaId + " doesn't exist")
        );
        if (text != null) {
            if (!text.isBlank() && text.length() <= 2047 &&
                    Objects.equals(description.getKnopka().getUser().getToken(), token)) {
                description.setText(text);
                System.out.println("Changed text");
            } else {
                throw new IllegalStateException("Your new text'" +
                        text + "' is not valid or token is invalid. Please choose another one"
                );
            }

        }
    }

    @Transactional
    public void updateDescriptionTags(Long descriptionKnopkaId, List<String> tags, String token) {
        Description description = descriptionRepository.findById(descriptionKnopkaId).orElseThrow(
                () -> new IllegalStateException("descriptionKnopka with id: " + descriptionKnopkaId + " doesn't exist")
        );
        if (!Objects.equals(description.getKnopka().getUser().getToken(), token))
            throw new IllegalStateException("Token is invalid");
        if (tags.size() <= 255) {
            description.setTags(tags);
        }

    }

    @Transactional
    public void updateDescriptionImage(Long descriptionKnopkaId, byte[] image, String token) {
        Description description = descriptionRepository.findById(descriptionKnopkaId).orElseThrow(
                () -> new IllegalStateException("descriptionKnopka with id: " + descriptionKnopkaId + " doesn't exist")
        );
        if (!Objects.equals(description.getKnopka().getUser().getToken(), token))
            throw new IllegalStateException("Token is invalid");
        //photo up to 2MB
        if (image.length <= 2097152) {
            description.setEncodedImage(image);
        } else {
            throw new IllegalStateException("Photo size is too large");
        }

    }

//    public List<Description> getDescriptionsByTag(String tag, String token, Long knopkaUserId) {
//        KnopkaUser knopkaUser = knopkaUserRepository.findById(knopkaUserId).orElseThrow(
//                () -> new IllegalStateException("no user with this id")
//        );
//        if (Objects.equals(knopkaUser.getToken(), token)) {
//            return descriptionRepository.findDescriptionsByTags(List.of(tag));
//        } else {
//            throw new IllegalStateException("token is invalid");
//        }
//    }
}


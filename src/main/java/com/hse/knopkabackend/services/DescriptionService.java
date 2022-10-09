package com.hse.knopkabackend.services;

import com.hse.knopkabackend.additionalclasses.Tag;
import com.hse.knopkabackend.models.description.Description;
import com.hse.knopkabackend.models.knopkauser.KnopkaUser;
import com.hse.knopkabackend.repositories.description.DescriptionRepository;
import com.hse.knopkabackend.repositories.knopkauser.KnopkaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class DescriptionService {

    private final DescriptionRepository descriptionRepository;
    private final KnopkaUserRepository knopkaUserRepository;

    @Autowired
    public DescriptionService(DescriptionRepository descriptionRepository, KnopkaUserRepository knopkaUserRepository) {
        this.descriptionRepository = descriptionRepository;
        this.knopkaUserRepository = knopkaUserRepository;
    }

    public Description getDescription(Long knopkaId, String token) {

        Description description = descriptionRepository.findById(knopkaId).orElseThrow(
                () -> new IllegalStateException("descriptionKnopka with id: " + knopkaId + " doesn't exist")
        );

        KnopkaUser knopkaUser = knopkaUserRepository.findKnopkaUserByToken(token).orElseThrow(
                () -> new IllegalStateException("token is invalid")
        );

        return description;
    }


    @Transactional("postgresKnopkaTransactionManager")
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

    @Transactional("postgresKnopkaTransactionManager")
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

    @Transactional("postgresKnopkaTransactionManager")
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

    public List<Tag> getDescriptionsByTag(String tag, String token) {
        KnopkaUser knopkaUser = knopkaUserRepository.findKnopkaUserByToken(token).orElseThrow(
                () -> new IllegalStateException("no user with this id")
        );
        return descriptionRepository.findByTag(tag);

    }
}


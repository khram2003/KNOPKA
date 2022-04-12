package com.hse.knopkabackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Entity(name = "profile")
@Table(name = "profile",
        uniqueConstraints = {
                @UniqueConstraint(name = "profile_nickname_unique", columnNames = "profile_nickname")
        })
public class Profile {
    @Id
    @Column(
            name = "profile_user_id",
            updatable = false
    )
    private Long userWithThisProfileId;


    @Column(
            name = "profile_nickname",
            nullable = false,
            columnDefinition = "VARCHAR(256)"
    )
    private String nickname;

    @Column(
            name = "bio",
            unique = true,
            columnDefinition = "TEXT"
    )
    private String bio;

    @Column(
            name = "encoded_image",
            nullable = true
    )
    @Lob
    MultipartFile encodedPhoto; //smth strange

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "profile_user_id")
    @JsonBackReference
    private KnopkaUser user;

    public Profile(String name, String bio, MultipartFile encodedPhoto, Long userWithThisProfileId, KnopkaUser user) {
        this.nickname = name;
        this.bio = bio;
        this.encodedPhoto = encodedPhoto;
        this.userWithThisProfileId = userWithThisProfileId;
        this.user = user;
    }

    public Profile(String name, String bio, Long userWithThisProfileId) {
        this.nickname = name;
        this.bio = bio;
        this.userWithThisProfileId = userWithThisProfileId;
    }

    public Profile(String name) {
        this.nickname = name;
    }

    public Profile() {

    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public MultipartFile getEncodedPhoto() {
        return encodedPhoto;
    }

    public void setEncodedPhoto(MultipartFile encodedPhoto) {
        this.encodedPhoto = encodedPhoto;
    }

    public Long getUserWithThisProfileId() {
        return userWithThisProfileId;
    }

    public void setUserWithThisProfileId(Long userWithThisProfileId) {
        this.userWithThisProfileId = userWithThisProfileId;
    }

    public KnopkaUser getUser() {
        return user;
    }

    public void setUser(KnopkaUser user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "name='" + nickname + '\'' +
                ", bio='" + bio + '\'' +
                '}';
    }
}
package com.hse.knopkabackend.DTO;

import java.io.Serializable;

public class ProfileDTO implements Serializable {
    private String nickname;
    private String bio;
    private String photo;

    public ProfileDTO() {
    }

    public ProfileDTO(String nickname, String bio, String photo) {
        this.nickname = nickname;
        this.bio = bio;
        this.photo = photo;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}

package com.hse.knopkabackend.DTO;

import java.io.Serializable;

public class KnopkaUserResponseDTO implements Serializable {
    String nickname;
    byte[] encodedPhoto;
    Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public KnopkaUserResponseDTO(String nickname, byte[] encodedPhoto, Long id) {
        this.nickname = nickname;
        this.encodedPhoto = encodedPhoto;
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public byte[] getEncodedPhoto() {
        return encodedPhoto;
    }

    public void setEncodedPhoto(byte[] encodedPhoto) {
        this.encodedPhoto = encodedPhoto;
    }
}

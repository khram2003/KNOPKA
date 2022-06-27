package com.hse.knopkabackend.DTO;

import java.io.Serializable;

public class KnopkaUserDTO implements Serializable {
    private String email;
    private String token;

    public KnopkaUserDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

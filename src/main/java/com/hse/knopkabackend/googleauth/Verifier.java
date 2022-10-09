package com.hse.knopkabackend.googleauth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import java.util.Collections;

public class Verifier {

    private final GoogleIdTokenVerifier verifier;

    public Verifier() {

        verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(
                        "289368713312-lvfhknnkjlv886nisi9ustnoreu5cj28.apps.googleusercontent.com"
                )).build();
    }


    public GoogleIdTokenVerifier getVerifier() {
        return verifier;
    }
}

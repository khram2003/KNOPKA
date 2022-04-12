package com.hse.knopkabackend.googleauth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class Verifier {

    GoogleIdTokenVerifier verifier;

    public Verifier(){

        verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                // should be in config file
                .setAudience(Collections.singletonList(
                        "289368713312-lvfhknnkjlv886nisi9ustnoreu5cj28.apps.googleusercontent.com"
                )).build();
    }

    public boolean isVerified(String token) throws GeneralSecurityException, IOException {
        return verifier.verify(token) != null;
    }
}

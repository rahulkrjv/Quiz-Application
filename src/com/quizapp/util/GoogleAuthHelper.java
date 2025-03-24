// com.quizapp.util.GoogleAuthHelper.java
package com.quizapp.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class GoogleAuthHelper {
    private static final String CLIENT_SECRET_FILE = "client_secret.json";
    private static final String CALLBACK_URI = "http://localhost:8888/callback";
    private static final String SCOPE = "https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email";

    private GoogleAuthorizationCodeFlow flow;

    public GoogleAuthHelper() {
        try {
            GoogleClientSecrets secrets = GoogleClientSecrets.load(
                JacksonFactory.getDefaultInstance(),
                new FileReader(CLIENT_SECRET_FILE)
            );

            flow = new GoogleAuthorizationCodeFlow.Builder(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                secrets,
                Arrays.asList(SCOPE.split(" "))
            ).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getAuthorizationUrl() {
        return flow.newAuthorizationUrl()
            .setRedirectUri(CALLBACK_URI)
            .build();
    }

    public User handleCallback(String authCode) {
        try {
            GoogleTokenResponse tokenResponse = flow.newTokenRequest(authCode)
                .setRedirectUri(CALLBACK_URI)
                .execute();

            Oauth2 oauth2 = new Oauth2.Builder(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                tokenResponse.createCredential()
            ).build();

            Userinfo userInfo = oauth2.userinfo().get().execute();

            // Create or update user in our system
            String email = userInfo.getEmail();
            User user = UserAuthentication.getUserByEmail(email);
            if (user == null) {
                UserAuthentication.registerUser(
                    userInfo.getName(),
                    email,
                    SecurityUtil.generateSecureToken() // Generate random password for Google users
                );
                user = UserAuthentication.getUserByEmail(email);
            }
            return user;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
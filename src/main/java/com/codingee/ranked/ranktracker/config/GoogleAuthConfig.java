package com.codingee.ranked.ranktracker.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class GoogleAuthConfig {

    private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static final String[] SCOPES = { MESSAGING_SCOPE };

    @Value("${google.auth.web.clientId}")
    private String googleAuthWebClientId;

    @Value("${google.auth.ios.clientId}")
    private String googleAuthIOSClientId;

    @Value("${google.auth.android.clientId")
    private String googleAuthAndroidClientId;

    @Bean(name = "googleIdTokenVerifier")
    public GoogleIdTokenVerifier getGoogleTokernVerifier() {
        List<String> allowedClientIds = new ArrayList<>();
        allowedClientIds.add(googleAuthWebClientId);
        allowedClientIds.add(googleAuthIOSClientId);
        allowedClientIds.add(googleAuthAndroidClientId);
        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(allowedClientIds)
                .build();
    }


    @Bean(name = "googleCredential")
    public GoogleCredential googleCredentials() throws IOException {
        ClassPathResource resource = new ClassPathResource("service-account.json");
        InputStream inputStream = resource.getInputStream();
        return GoogleCredential.fromStream(inputStream)
                .createScoped(Arrays.asList(SCOPES));
    }



}

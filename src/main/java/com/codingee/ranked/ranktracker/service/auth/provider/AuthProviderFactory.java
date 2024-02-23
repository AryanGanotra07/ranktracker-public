package com.codingee.ranked.ranktracker.service.auth.provider;

import com.codingee.ranked.ranktracker.dto.auth.AuthRequestDTO;
import com.codingee.ranked.ranktracker.service.client.IClientService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;


public class AuthProviderFactory {
    public static IAuthProvider create(AuthRequestDTO.AuthRequestIssuer issuer, IClientService clientService, PasswordEncoder passwordEncoder, GoogleIdTokenVerifier googleIdTokenVerifier) {
        if (Objects.requireNonNull(issuer) == AuthRequestDTO.AuthRequestIssuer.GOOGLE) {
            return new GoogleAuthProvider(clientService, googleIdTokenVerifier);
        }
        return new EmailAuthProvider(clientService, passwordEncoder);
    }
}

package com.codingee.ranked.ranktracker.service.auth.provider;

import com.codingee.ranked.ranktracker.dto.auth.AuthRequestDTO;
import com.codingee.ranked.ranktracker.dto.client.AddClientRequestDTO;
import com.codingee.ranked.ranktracker.model.client.Client;
import com.codingee.ranked.ranktracker.service.client.IClientService;
import com.codingee.ranked.ranktracker.util.ValidationUtil;
import com.codingee.ranked.ranktracker.util.exceptions.UnauthenticatedException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;
import java.security.GeneralSecurityException;

@AllArgsConstructor
public class GoogleAuthProvider implements IAuthProvider{

    private IClientService clientService;

    private GoogleIdTokenVerifier googleIdTokenVerifier;

    @Override
    public ClientAuth login(AuthRequestDTO authRequestDTO) {


        ValidationUtil.ensureNotEmpty(authRequestDTO.getGoogleTokenId(), "Google token id");

        GoogleIdToken idToken = null;
        try {
            idToken = googleIdTokenVerifier.verify(authRequestDTO.getGoogleTokenId());

        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String userId = payload.getSubject();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            Client client = null;
            try {
                client = this.clientService.getClientByEmail(email);
            } catch (Exception e) {

            }
            if (client == null) {
                // create client
                AddClientRequestDTO clientRequestDTO = new AddClientRequestDTO(name, email, null, null);
                client = this.clientService.addClient(clientRequestDTO);
            }
            return new ClientAuth(client,  new UsernamePasswordAuthenticationToken(client.getId(), authRequestDTO.getGoogleTokenId()));
        }
            throw new UnauthenticatedException("Incorrect google token");
        } catch (GeneralSecurityException e) {
            throw new UnauthenticatedException("Incorrect google token");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

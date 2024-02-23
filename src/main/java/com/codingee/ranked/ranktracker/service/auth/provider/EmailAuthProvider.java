package com.codingee.ranked.ranktracker.service.auth.provider;

import com.codingee.ranked.ranktracker.dto.auth.AuthRequestDTO;
import com.codingee.ranked.ranktracker.model.client.Client;
import com.codingee.ranked.ranktracker.service.client.IClientService;
import com.codingee.ranked.ranktracker.util.exceptions.ResourceNotFoundException;
import com.codingee.ranked.ranktracker.util.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

@AllArgsConstructor
public class EmailAuthProvider implements IAuthProvider{
    private IClientService clientService;
    private PasswordEncoder passwordEncoder;

    @Override
    public ClientAuth login(AuthRequestDTO authRequestDTO) {
        Client client = this.clientService.getClientByEmail(authRequestDTO.getEmail());
        if (Objects.isNull(client)) {
            throw new ResourceNotFoundException("User not found");
        }
        if (!passwordEncoder.matches(authRequestDTO.getPassword(), client.getPassword())) {
            throw new ValidationException("Incorrect password");
        }
        return new ClientAuth(client, new UsernamePasswordAuthenticationToken(client.getId(), client.getPassword()));
    }
}

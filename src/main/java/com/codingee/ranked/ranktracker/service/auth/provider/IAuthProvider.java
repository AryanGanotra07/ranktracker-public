package com.codingee.ranked.ranktracker.service.auth.provider;

import com.codingee.ranked.ranktracker.dto.auth.AuthRequestDTO;
import com.codingee.ranked.ranktracker.model.client.Client;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;


public interface IAuthProvider {

    @AllArgsConstructor
    class ClientAuth {
        @Getter @Setter
        private Client client;
        @Getter @Setter
        private Authentication authentication;
    }
    ClientAuth login(AuthRequestDTO authRequestDTO);
}

package com.codingee.ranked.ranktracker.service.auth;

import com.codingee.ranked.ranktracker.dto.auth.AuthRequestDTO;
import com.codingee.ranked.ranktracker.dto.auth.ResetPasswordDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;

public interface IAuthService {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class AuthResponse {
        private Long clientId;
        private String accessToken;
        private String refreshToken;
    }

    Authentication getAuthentication();

    void setAuthentication(Authentication token);

    AuthService.AuthResponse login(AuthRequestDTO authRequestDTO, AuthRequestDTO.AuthRequestIssuer issuer);

    Boolean forgotPassword(String email);

    Boolean resetPassword(ResetPasswordDTO resetPasswordDTO);

}

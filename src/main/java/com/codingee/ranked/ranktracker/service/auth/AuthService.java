package com.codingee.ranked.ranktracker.service.auth;

import com.codingee.ranked.ranktracker.dto.auth.AuthRequestDTO;
import com.codingee.ranked.ranktracker.dto.auth.ResetPasswordDTO;
import com.codingee.ranked.ranktracker.dto.client.UpdateClientRequestDTO;
import com.codingee.ranked.ranktracker.dto.email.PasswordResetEmailDTO;
import com.codingee.ranked.ranktracker.model.auth.PasswordResetToken;
import com.codingee.ranked.ranktracker.model.client.Client;
import com.codingee.ranked.ranktracker.repo.auth.IPasswordResetTokenRepo;
import com.codingee.ranked.ranktracker.security.JWTHelper;
import com.codingee.ranked.ranktracker.service.auth.provider.AuthProviderFactory;
import com.codingee.ranked.ranktracker.service.auth.provider.IAuthProvider;
import com.codingee.ranked.ranktracker.service.client.IClientService;
import com.codingee.ranked.ranktracker.service.email.EmailTemplateService;
import com.codingee.ranked.ranktracker.util.ValidationUtil;
import com.codingee.ranked.ranktracker.util.exceptions.ResourceNotFoundException;
import com.codingee.ranked.ranktracker.util.exceptions.ValidationException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final IClientService clientService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;
    private final IPasswordResetTokenRepo passwordResetTokenRepo;
    private final EmailTemplateService emailTemplateService;

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public void setAuthentication(Authentication token) {
        SecurityContextHolder.getContext().setAuthentication(token);
    }


    @Override
    public AuthResponse login(AuthRequestDTO authRequestDTO, AuthRequestDTO.AuthRequestIssuer issuer) {
        IAuthProvider.ClientAuth clientAuth = AuthProviderFactory.create(issuer, clientService, passwordEncoder, googleIdTokenVerifier).login(authRequestDTO);
        this.setAuthentication(clientAuth.getAuthentication());
        String access_token = JWTHelper.getAccessToken(issuer.name(), clientAuth.getClient());
        String refresh_token = JWTHelper.getRefreshToken(issuer.name(), clientAuth.getClient());
        return new AuthResponse(clientAuth.getClient().getId(), access_token, refresh_token);
    }

    @Override
    public Boolean forgotPassword(String email) {
        ValidationUtil.ensureNotEmpty(email, "Email");
        Client client = this.clientService.getClientByEmail(email);
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken(client, token);
        passwordResetTokenRepo.save(passwordResetToken);
        this.emailTemplateService.sendForgetPasswordEmail(client.getEmail(), client.getFullName(), new PasswordResetEmailDTO(token, passwordResetToken.getExpiryDate().format(DateTimeFormatter.ISO_DATE_TIME)));
        return true;
    }

    @Override
    public Boolean resetPassword(ResetPasswordDTO resetPasswordDTO) {
        PasswordResetToken passwordResetToken = this.passwordResetTokenRepo.findByToken(resetPasswordDTO.getToken()).orElse(null);
        if (passwordResetToken == null) {
            throw new ResourceNotFoundException("Invalid token");
        }
        if (passwordResetToken.isExpired()) {
            throw new ValidationException("Token expired. Please reset your password again.");
        }
        ValidationUtil.ensureEqual(resetPasswordDTO.getNewPassword(), resetPasswordDTO.getConfirmNewPassword(), "Passwords not matching");
        Client client = passwordResetToken.getClient();
        this.clientService.updateClient(client.getId(), new UpdateClientRequestDTO(null, resetPasswordDTO.getNewPassword(), null));
        return true;
    }

}

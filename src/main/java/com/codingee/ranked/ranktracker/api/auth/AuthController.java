package com.codingee.ranked.ranktracker.api.auth;


import com.codingee.ranked.ranktracker.dto.auth.AuthRequestDTO;
import com.codingee.ranked.ranktracker.dto.auth.ResetPasswordDTO;
import com.codingee.ranked.ranktracker.service.auth.IAuthService;
import com.codingee.ranked.ranktracker.util.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final IAuthService authService;
    @PostMapping("/login")
    public BaseResponse<IAuthService.AuthResponse> login(@Valid  @RequestBody AuthRequestDTO authRequestDTO, HttpServletRequest request) {
        IAuthService.AuthResponse authResponse =  this.authService.login(authRequestDTO, authRequestDTO.getIssuer());
        return BaseResponse.success(authResponse);
    }
    @PostMapping("/forgot-password")
    public BaseResponse<Boolean> forgotPassword(@RequestParam("email") String email) {
        return BaseResponse.success(this.authService.forgotPassword(email));
    }

    @PostMapping("/reset-password")
    public BaseResponse<Boolean> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        return BaseResponse.success(this.authService.resetPassword(resetPasswordDTO));
    }
}

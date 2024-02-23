package com.codingee.ranked.ranktracker.dto.auth;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ResetPasswordDTO {

    @NotEmpty(message = "Token is required")
    @NotNull(message = "Token is required")
    private String token;

    @NotEmpty(message = "Password is required")
    @NotNull(message = "Password is required")
    private String newPassword;

    @NotEmpty(message = "Password is required")
    @NotNull(message = "Password is required")
    private String confirmNewPassword;
}

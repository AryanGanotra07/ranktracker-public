package com.codingee.ranked.ranktracker.dto.auth;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class AuthRequestDTO {

    public enum AuthRequestIssuer {
        GOOGLE, EMAIL
    }

    @Email(message = "The email address is invalid.", flags = { Pattern.Flag.CASE_INSENSITIVE })
    private String email;

    private String password;

    private String googleTokenId;

    @NotNull()
    private AuthRequestIssuer issuer;

}


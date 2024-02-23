package com.codingee.ranked.ranktracker.dto.email;

public record PasswordResetEmailDTO(String token, String expiryTime) {
}

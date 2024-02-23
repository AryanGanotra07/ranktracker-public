package com.codingee.ranked.ranktracker.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class AddClientRequestDTO {
    @NotEmpty(message = "The name is required.")
    @Size(min = 2, max = 128, message = "The length of name must be between 2 and 100 characters.")
    private String fullName;

    @NotEmpty(message = "The email address is required.")
    @Email(message = "The email address is invalid.", flags = { Pattern.Flag.CASE_INSENSITIVE })
    private String email;

    @NotEmpty(message = "Password can't be empty")
    private String password;

    private String company;

}


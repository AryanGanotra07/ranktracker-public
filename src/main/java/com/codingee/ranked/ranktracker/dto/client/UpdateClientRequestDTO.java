package com.codingee.ranked.ranktracker.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Size;

@AllArgsConstructor
@Data
public class UpdateClientRequestDTO {
    @Size(min = 2, max = 128, message = "The length of name must be between 2 and 100 characters.")
    private String fullName;
    private String password;
    private String company;
}


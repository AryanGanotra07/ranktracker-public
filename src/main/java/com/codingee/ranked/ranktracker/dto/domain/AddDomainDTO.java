package com.codingee.ranked.ranktracker.dto.domain;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AddDomainDTO {
    @NotEmpty(message = "Domain name can't be null")
    private String name;
    private List<String> keywords;
    @NotNull(message = "Location can't be null")
    private String location;
    @NotNull(message = "Device type can't be null")
    private String deviceType;

    @NotNull(message = "Language code can't be null")
    private String languageCode;
}

package com.codingee.ranked.ranktracker.dto.keyword;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class AddKeywordDTO {
    @NotNull(message = "Keyword name is required")
    @NotEmpty(message = "Keyword name is required")
    private String name;
    @NotNull(message = "Keyword cannot be created without a domain Id")
    private Long domainId;
    private String location;
    private String deviceType;
}

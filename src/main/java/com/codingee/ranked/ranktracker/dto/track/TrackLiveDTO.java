package com.codingee.ranked.ranktracker.dto.track;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class TrackLiveDTO {
    @NotNull(message = "Keyword name is required")
    @NotEmpty(message = "Keyword name is required")
    private String keywordName;
    @NotNull(message = "Domain name is required")
    @NotEmpty(message = "Keyword name is required")
    private String domainName;
    @NotNull(message = "Location is required")
    @NotEmpty(message = "Location is required")
    private String location;
    @NotNull(message = "Device type is required")
    @NotEmpty(message = "Location is required")
    private String deviceType;
    @NotNull(message = "language")
    @NotEmpty(message = "Location is required")
    private String languageCode;

}

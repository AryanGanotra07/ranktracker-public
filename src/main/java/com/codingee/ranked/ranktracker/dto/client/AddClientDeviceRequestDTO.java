package com.codingee.ranked.ranktracker.dto.client;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class AddClientDeviceRequestDTO {

    @NotNull(message = "Device IDs cannot be null")
    @NotEmpty(message = "At least one device ID required")
    private Set<String> deviceIds;

}

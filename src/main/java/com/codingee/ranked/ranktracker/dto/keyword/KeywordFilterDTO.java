package com.codingee.ranked.ranktracker.dto.keyword;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
public class KeywordFilterDTO {

    enum KeywordSort {
        RANK, CHANGE_IN_RANK
    }

    @NotNull(message = "Domain id is required")
    private Long domainId;

    private Integer limit;

    private KeywordSort sortBy;

}
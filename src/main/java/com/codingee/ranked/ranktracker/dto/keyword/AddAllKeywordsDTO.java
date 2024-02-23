package com.codingee.ranked.ranktracker.dto.keyword;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class AddAllKeywordsDTO {

    @NotNull(message = "Keyword cannot be created without a domain Id")
    private Long domainId;

    @NotNull(message = "Keywords list cannot be null or empty")
    @NotEmpty(message = "Keywords list cannot be null or empty")
    private List<AddKeywordDTO> keywordsList;

}

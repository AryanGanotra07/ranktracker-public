package com.codingee.ranked.ranktracker.service.keyword;

import com.codingee.ranked.ranktracker.dto.keyword.AddAllKeywordsDTO;
import com.codingee.ranked.ranktracker.dto.keyword.AddKeywordDTO;
import com.codingee.ranked.ranktracker.dto.keyword.KeywordFilterDTO;
import com.codingee.ranked.ranktracker.dto.keyword.UpdateKeywordDTO;
import com.codingee.ranked.ranktracker.model.keyword.Keyword;

import java.util.List;

public interface IKeywordService {
    Keyword addKeyword(AddKeywordDTO addKeywordDTO);
    Keyword updateKeyword(Long id, UpdateKeywordDTO updateKeywordDTO);
    void deleteKeyword(Long id);
    Keyword getKeyword(Long id);

    List<Keyword> getKeywordsByFilter(KeywordFilterDTO keywordFilterDTO);

    List<Keyword> addAllKeywords(AddAllKeywordsDTO addAllKeywordsDTO);

    Double getAvgLatestRankByDomainId(Long domainId);
}

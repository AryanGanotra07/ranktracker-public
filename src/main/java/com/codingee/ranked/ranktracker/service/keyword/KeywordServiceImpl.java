package com.codingee.ranked.ranktracker.service.keyword;

import com.codingee.ranked.ranktracker.dto.keyword.AddAllKeywordsDTO;
import com.codingee.ranked.ranktracker.dto.keyword.AddKeywordDTO;
import com.codingee.ranked.ranktracker.dto.keyword.KeywordFilterDTO;
import com.codingee.ranked.ranktracker.dto.keyword.UpdateKeywordDTO;
import com.codingee.ranked.ranktracker.model.domain.Domain;
import com.codingee.ranked.ranktracker.model.keyword.Keyword;
import com.codingee.ranked.ranktracker.repo.keyword.IKeywordRepo;
import com.codingee.ranked.ranktracker.service.domain.IDomainService;
import com.codingee.ranked.ranktracker.util.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class KeywordServiceImpl implements IKeywordService {

    private final IKeywordRepo keywordRepo;
    private final IDomainService domainService;

    private static final String KEYWORD_NOT_FOUND_MSG = "Domain with name %s not found!";
    private static final String KEYWORD_WITH_ID_NOT_FOUND_MSG = "Domain with id %s not found!";

    @Override
    public Keyword addKeyword(AddKeywordDTO addKeywordDTO) {
        Domain domain = this.domainService.getDomainById(addKeywordDTO.getDomainId());
        Keyword keyword = new Keyword(addKeywordDTO, domain);
        return this.keywordRepo.save(keyword);
    }

    @Override
    public Keyword updateKeyword(Long id, UpdateKeywordDTO updateKeywordDTO) {
        return null;
    }

    @Override
    public void deleteKeyword(Long id) {
        Keyword keyword = this.getKeyword(id);
        this.keywordRepo.delete(keyword);
    }

    @Override
    public Keyword getKeyword(Long id) {
        Optional<Keyword> keywordOptional = this.keywordRepo.findById(id);
        if (keywordOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format(KEYWORD_WITH_ID_NOT_FOUND_MSG, id));
        }
        return keywordOptional.get();
    }
    @Override
    public List<Keyword> getKeywordsByFilter(KeywordFilterDTO keywordFilterDTO) {
        List<Keyword> keywords = this.keywordRepo.findByDomainId(keywordFilterDTO.getDomainId());
        return keywords;
    }

    @Override
    @Transactional
    public List<Keyword> addAllKeywords(AddAllKeywordsDTO addAllKeywordsDTO) {
        Domain domain = this.domainService.getDomainById(addAllKeywordsDTO.getDomainId());
        Set<String> keywords = this.getKeywordsByFilter(KeywordFilterDTO.builder().domainId(domain.getId()).build()).stream().map(Keyword::getName).collect(Collectors.toSet());
        List<Keyword> toSave = addAllKeywordsDTO.getKeywordsList().stream().filter(v -> !keywords.contains(v.getName())).map(v -> new Keyword(v, domain)).toList();
        if (keywords.size() > 0 || toSave.size() > 0) {
            domain.setIsDraft(false);
        }
        return keywordRepo.saveAll(toSave);
    }

    @Override
    public Double getAvgLatestRankByDomainId(Long domainId) {
        return this.keywordRepo.getAverageRankingByDomainId(domainId);
    }
}

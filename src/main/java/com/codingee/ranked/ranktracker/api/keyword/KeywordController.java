package com.codingee.ranked.ranktracker.api.keyword;

import com.codingee.ranked.ranktracker.dto.keyword.AddAllKeywordsDTO;
import com.codingee.ranked.ranktracker.dto.keyword.AddKeywordDTO;
import com.codingee.ranked.ranktracker.dto.keyword.KeywordFilterDTO;
import com.codingee.ranked.ranktracker.model.keyword.Keyword;
import com.codingee.ranked.ranktracker.service.keyword.IKeywordService;
import com.codingee.ranked.ranktracker.util.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import java.util.List;

@RestController
@RequestMapping("/api/keyword")
@RequiredArgsConstructor
@Slf4j
public class KeywordController {
    private final IKeywordService keywordService;

    @PostMapping("")
    private BaseResponse<Keyword> addKeyword(@Valid @RequestBody AddKeywordDTO addKeywordDTO) {
        return BaseResponse.success(this.keywordService.addKeyword(addKeywordDTO));
    }

    @PostMapping("/bulk")
    private BaseResponse<List<Keyword>> addAllKeyword(@Valid @RequestBody AddAllKeywordsDTO addAllKeywordsDTO) {
        return BaseResponse.success(this.keywordService.addAllKeywords(addAllKeywordsDTO));
    }

    @GetMapping("")
    private BaseResponse<List<Keyword>> getKeywords(@Valid @ModelAttribute KeywordFilterDTO keywordFilterDTO) {
        return BaseResponse.success(this.keywordService.getKeywordsByFilter(keywordFilterDTO));
    }

    @GetMapping("/{id}")
    private BaseResponse<Keyword> getKeyword(@PathVariable Long id) {
        return BaseResponse.success(this.keywordService.getKeyword(id));
    }

    @DeleteMapping("/{id}")
    private BaseResponse<Null> deleteKeyword(@PathVariable Long id) {
        this.keywordService.deleteKeyword(id);
        return BaseResponse.success(null);
    }

}

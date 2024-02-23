package com.codingee.ranked.ranktracker.api.ranking;


import com.codingee.ranked.ranktracker.dto.ranking.RankingFiltersDTO;
import com.codingee.ranked.ranktracker.model.rank.Ranking;
import com.codingee.ranked.ranktracker.service.ranking.IRankingService;
import com.codingee.ranked.ranktracker.util.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/api/ranking")
@RequiredArgsConstructor
@Slf4j
public class RankingController {

    private final IRankingService rankingService;

    @GetMapping("/{id}")
    public BaseResponse<Ranking> getRanking(@PathVariable Long id) {
        return BaseResponse.success(this.rankingService.getRankingById(id));
    }

    @GetMapping("")
    public BaseResponse<Set<Ranking>> filterRankings(@Valid @ModelAttribute RankingFiltersDTO rankingFilters) {
        return BaseResponse.success(this.rankingService.filterRankings(rankingFilters));
    }



}

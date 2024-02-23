package com.codingee.ranked.ranktracker.service.ranking;


import com.codingee.ranked.ranktracker.dto.ranking.RankingFiltersDTO;
import com.codingee.ranked.ranktracker.model.rank.Ranking;

import java.util.Set;

public interface IRankingService {
    Ranking getRankingById(Long id);
    Set<Ranking> filterRankings(RankingFiltersDTO rankingFilters);
    Ranking addRanking(Ranking ranking);
}

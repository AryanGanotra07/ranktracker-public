package com.codingee.ranked.ranktracker.service.ranking;

import com.codingee.ranked.ranktracker.dto.ranking.RankingFiltersDTO;
import com.codingee.ranked.ranktracker.model.rank.Ranking;
import com.codingee.ranked.ranktracker.repo.ranking.IRankingRepo;
import com.codingee.ranked.ranktracker.util.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements IRankingService {

    private final IRankingRepo rankingRepo;
    private static final String RANKING_WITH_ID_NOT_FOUND_MSG = "Ranking with id %s not found!";

    @Override
    public Ranking getRankingById(Long id) {
        Optional<Ranking> optionalRanking = this.rankingRepo.findById(id);
        if (optionalRanking.isEmpty()) {
            throw new ResourceNotFoundException(String.format(RANKING_WITH_ID_NOT_FOUND_MSG, id));
        }
        return optionalRanking.get();
    }

    @Override
    public Set<Ranking> filterRankings(RankingFiltersDTO rankingFilters) {
        Long keywordId = rankingFilters.getKeywordId();
//        Date startDate = Objects.nonNull(rankingFilters.getStartDate()) ? rankingFilters.getStartDate() : Date.from(LocalDate.of(2023, 3, 31).atStartOfDay(ZoneId.systemDefault()).toInstant());
//        Date endDate = Objects.nonNull(rankingFilters.getEndDate()) ? rankingFilters.getEndDate() : Date.from(LocalDate.now().atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
        return this.rankingRepo.findByKeywordId(keywordId).orElse(new HashSet<>());
    }

    @Override
    public Ranking addRanking(Ranking ranking) {
        return this.rankingRepo.save(ranking);
    }
}

package com.codingee.ranked.ranktracker.repo.ranking;

import com.codingee.ranked.ranktracker.model.rank.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

public interface IRankingRepo extends JpaRepository<Ranking, Long> {
    Optional<Ranking> findById(Long id);
    Optional<Set<Ranking>> findByKeywordId(Long id);

    Optional<Set<Ranking>> findByKeywordIdAndCreatedAtBetween(Long keywordId, Date startDate, Date endDate);
}

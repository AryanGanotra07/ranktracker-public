package com.codingee.ranked.ranktracker.repo.keyword;

import com.codingee.ranked.ranktracker.model.keyword.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IKeywordRepo extends JpaRepository<Keyword, Long> {
    Optional<Keyword> findById(Long id);
    List<Keyword> findByDomainId(Long id);

    @Query("SELECT AVG(k.rank) FROM Keyword k WHERE k.domain.id = :domainId")
    Double getAverageRankingByDomainId(@Param("domainId") Long domainId);

    @Query("SELECT k FROM Keyword k WHERE k.domain.id = :domainId ORDER BY k.rank ASC")
    List<Keyword> findTopKByDomainIdOrderByRankAsc(@Param("domainId") Long domainId);

    @Query("SELECT k FROM Keyword k WHERE k.domain.id = :domainId ORDER BY COALESCE(k.rank, 0) - COALESCE(k.lastRank, 0) DESC")
    List<Keyword> findTopKByDomainIdOrderByRankMinusLastRankDesc(@Param("domainId") Long domainId);

}

package com.codingee.ranked.ranktracker.repo.ranking;


import com.codingee.ranked.ranktracker.dto.ranking.LiveRankingSearch;
import com.codingee.ranked.ranktracker.model.rank.LiveRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ILiveRankingRepo extends JpaRepository<LiveRanking, Long> {



    @Query("SELECT DISTINCT NEW com.codingee.ranked.ranktracker.dto.ranking.LiveRankingSearch(e.domain, e.keywordName, e.location,  e.deviceType, e.languageCode) " +
            "FROM LiveRanking e " +
            "WHERE e.domain LIKE %:domain% " +
            "AND e.client.id = :clientId")
    List<LiveRankingSearch> findDistinctByDomainAndClientId(@Param("domain") String domain, @Param("clientId") Long clientId);


}

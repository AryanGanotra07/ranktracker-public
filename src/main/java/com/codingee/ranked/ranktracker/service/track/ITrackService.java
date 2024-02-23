package com.codingee.ranked.ranktracker.service.track;

import com.codingee.ranked.ranktracker.dto.ranking.LiveRankingSearch;
import com.codingee.ranked.ranktracker.dto.track.TrackLiveDTO;
import com.codingee.ranked.ranktracker.model.rank.LiveRanking;

import java.util.List;

public interface ITrackService {

    LiveRanking trackLive(TrackLiveDTO trackLiveDTO, Long clientId);

    List<LiveRankingSearch> getResults(Long clientId, String domainName);
}

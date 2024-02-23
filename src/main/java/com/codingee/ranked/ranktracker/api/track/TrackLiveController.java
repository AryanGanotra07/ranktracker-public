package com.codingee.ranked.ranktracker.api.track;

import com.codingee.ranked.ranktracker.dto.ranking.LiveRankingSearch;
import com.codingee.ranked.ranktracker.dto.track.TrackLiveDTO;
import com.codingee.ranked.ranktracker.model.rank.LiveRanking;
import com.codingee.ranked.ranktracker.service.track.ITrackService;
import com.codingee.ranked.ranktracker.util.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/track")
@RequiredArgsConstructor
@Slf4j
public class TrackLiveController {

    private final ITrackService trackService;

    @PostMapping("/live")
    @Cacheable(value = "liveTrackCache", cacheManager = "dayTTLCacheManager")
    public BaseResponse<LiveRanking> trackLive(@Valid @RequestBody TrackLiveDTO trackLiveDTO, @RequestAttribute(name = "clientId") Long clientId) {
        return BaseResponse.success(this.trackService.trackLive(trackLiveDTO, clientId));
    }

    @GetMapping("")
    public BaseResponse<List<LiveRankingSearch>> getRankings(@RequestAttribute(name = "clientId") Long clientId, @RequestParam("domainName") String domainName) {
        return BaseResponse.success(this.trackService.getResults(clientId, domainName));
    }

}

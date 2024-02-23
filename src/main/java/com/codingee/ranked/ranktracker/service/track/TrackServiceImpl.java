package com.codingee.ranked.ranktracker.service.track;

import com.codingee.ranked.ranktracker.dto.ranking.LiveRankingSearch;
import com.codingee.ranked.ranktracker.dto.ranking.SeoRankingLiveRequest;
import com.codingee.ranked.ranktracker.dto.ranking.SeoRankingResponse;
import com.codingee.ranked.ranktracker.dto.track.TrackLiveDTO;
import com.codingee.ranked.ranktracker.model.client.Client;
import com.codingee.ranked.ranktracker.model.rank.LiveRanking;
import com.codingee.ranked.ranktracker.model.rank.Ranking;
import com.codingee.ranked.ranktracker.repo.ranking.ILiveRankingRepo;
import com.codingee.ranked.ranktracker.service.client.IClientService;
import com.codingee.ranked.ranktracker.service.seo.ISeoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@AllArgsConstructor
public class TrackServiceImpl implements ITrackService{

    private final ISeoService seoService;
    private final ILiveRankingRepo liveRankingRepo;
    protected final IClientService clientService;
    private final ObjectMapper objectMapper;
    private final static int NO_OF_COMPETITOR_RATINGS = 3;

    private List<Ranking.CompetitorRanking> getCompetitorRankings(List<SeoRankingResponse.Task.TaskResult.TaskItem> taskItems) {
        return taskItems.stream().map(v -> new Ranking.CompetitorRanking(v.domain(), v.url(), v.breadcrumb(), v.rank_absolute(), v.title(), v.description())).collect(Collectors.toList());
    }

    @Override
    public LiveRanking trackLive(TrackLiveDTO trackLiveDTO, Long clientId) {
        log.info("Tracking live {}", trackLiveDTO.getDomainName());
        Client client = this.clientService.getClient(clientId);
        List<SeoRankingLiveRequest> requests = new ArrayList<>();
        requests.add(new SeoRankingLiveRequest(trackLiveDTO.getKeywordName(), trackLiveDTO.getLocation(), trackLiveDTO.getDeviceType(),trackLiveDTO.getLanguageCode()));
        SeoRankingResponse seoRankingResponse = this.seoService.getRankingForKeyword(requests);
        List<SeoRankingResponse.Task.TaskResult.TaskItem> taskItems = seoRankingResponse.tasks().get(0).result().get(0).items();
        OptionalInt firstIndex = IntStream.range(0, taskItems.size())
                .filter(i -> taskItems.get(i).domain().toLowerCase().contains(trackLiveDTO.getDomainName().toLowerCase()))
                .findFirst();
        LiveRanking ranking;
        if (firstIndex.isPresent()) {
            int index = firstIndex.getAsInt();
            SeoRankingResponse.Task.TaskResult.TaskItem taskItem = taskItems.get(index);
            ranking = new LiveRanking(trackLiveDTO.getKeywordName(), taskItem.rank_absolute(), trackLiveDTO.getDomainName(), taskItem.title(), taskItem.description(), taskItem.url(), taskItem.breadcrumb(), client, trackLiveDTO.getLocation(), trackLiveDTO.getDeviceType(), trackLiveDTO.getLanguageCode());
           // ranking.setLowCompetitorRankings(getCompetitorRankings(taskItems.subList(Math.max(0, index - NO_OF_COMPETITOR_RATINGS), index)));

        } else {
            ranking = new LiveRanking(trackLiveDTO.getKeywordName(), null, trackLiveDTO.getDomainName(), null, null, null, null,client,  trackLiveDTO.getLocation(), trackLiveDTO.getDeviceType(), trackLiveDTO.getLanguageCode());
        }
        ranking.setHighCompetitorRankings(getCompetitorRankings(taskItems.subList(0, 5)));
        return liveRankingRepo.save(ranking);
    }

    @Override
    public List<LiveRankingSearch> getResults(Long clientId, String domainName) {

        return liveRankingRepo.findDistinctByDomainAndClientId(domainName, clientId);

    }
}

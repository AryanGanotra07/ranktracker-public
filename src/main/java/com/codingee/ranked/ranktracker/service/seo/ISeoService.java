package com.codingee.ranked.ranktracker.service.seo;

import com.codingee.ranked.ranktracker.dto.ranking.*;

import java.util.List;

public interface ISeoService {
    SeoRankingTaskResponse trackKeywords(List<SeoRankingRequest> seoRankingRequests);

    SeoRankingResponse getRankingFromTrackJobId(String trackJobId);

    SeoRankingResponse getRankingForKeyword(List<SeoRankingLiveRequest> seoRankingRequests);


    SeoTasksCompletedResponse getCompletedTasks();


}

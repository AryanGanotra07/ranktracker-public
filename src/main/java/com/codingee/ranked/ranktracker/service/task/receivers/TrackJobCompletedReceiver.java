package com.codingee.ranked.ranktracker.service.task.receivers;

import com.codingee.ranked.ranktracker.config.RabbitMqConfig;
import com.codingee.ranked.ranktracker.dto.cache.TrackJobCacheDTO;
import com.codingee.ranked.ranktracker.dto.ranking.SeoRankingResponse;
import com.codingee.ranked.ranktracker.dto.task.NotificationQueueTask;
import com.codingee.ranked.ranktracker.dto.task.TrackJobCompletedQueueTask;
import com.codingee.ranked.ranktracker.model.domain.Domain;
import com.codingee.ranked.ranktracker.model.keyword.Keyword;
import com.codingee.ranked.ranktracker.model.rank.Ranking;
import com.codingee.ranked.ranktracker.model.track_request.TrackRequest;
import com.codingee.ranked.ranktracker.service.cache.ICacheService;
import com.codingee.ranked.ranktracker.service.domain.IDomainService;
import com.codingee.ranked.ranktracker.service.keyword.IKeywordService;
import com.codingee.ranked.ranktracker.service.ranking.IRankingService;
import com.codingee.ranked.ranktracker.service.seo.ISeoService;
import com.codingee.ranked.ranktracker.service.task.ITaskSchedularService;
import com.codingee.ranked.ranktracker.service.track_request.ITrackRequestService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@AllArgsConstructor
public class TrackJobCompletedReceiver {

    private final ICacheService cacheService;
    private final IKeywordService keywordService;
    private final ISeoService seoService;
    private final IDomainService domainService;
    private final IRankingService rankingService;

    private final ITaskSchedularService taskSchedulerService;
    private final ITrackRequestService trackRequestService;

    private final static int NO_OF_COMPETITOR_RATINGS = 3;

    @RabbitListener(queues = RabbitMqConfig.TRACK_JOB_COMPLETED_QUEUE)
    @Transactional
    public void receiveMessage(final TrackJobCompletedQueueTask task) {
        TrackJobCacheDTO cachedJob = this.cacheService.getTrackJob(task.getTrackJobId());
//        SeoRankingResponse seoRankingResponse1 = this.seoService.getRankingFromTrackJobId(task.getTrackJobId());
        if (Objects.isNull(cachedJob)) {
            log.info(String.format("Job with id %s is null", task.getTrackJobId()));
            return;
        }
        log.info("Received track completed request domain {} keyword {}", cachedJob.domainId(), cachedJob.keywordId());
        Keyword keyword = this.keywordService.getKeyword(cachedJob.keywordId());
        Domain domain = this.domainService.getDomainById(cachedJob.domainId());
        SeoRankingResponse seoRankingResponse = this.seoService.getRankingFromTrackJobId(task.getTrackJobId());
        List<SeoRankingResponse.Task.TaskResult.TaskItem> taskItems =  seoRankingResponse.tasks().get(0).result().get(0).items();
        OptionalInt firstIndex = IntStream.range(0, taskItems.size())
                .filter(i -> seoRankingResponse.tasks().get(0).result().get(0).items().get(i).domain().toLowerCase().contains(domain.getName().toLowerCase()))
                .findFirst();
        Ranking ranking;
        if (firstIndex.isPresent()) {
            int index = firstIndex.getAsInt();
            SeoRankingResponse.Task.TaskResult.TaskItem taskItem = taskItems.get(index);
            ranking = new Ranking(keyword, taskItem.rank_absolute(), domain.getName(), taskItem.title(), taskItem.description(), taskItem.url(), taskItem.breadcrumb());
           // ranking.setLowCompetitorRankings(getCompetitorRankings(taskItems.subList(Math.max(0, index - NO_OF_COMPETITOR_RATINGS), index)));

        } else {
            ranking = new Ranking(keyword, null, domain.getName(), null, null, null, null);
        }
        ranking.setHighCompetitorRankings(getCompetitorRankings(taskItems.subList(0, 5)));
        this.rankingService.addRanking(ranking);
        keyword.updateRanking(ranking.getRank());
        this.cacheService.deleteTrackJob(cachedJob.taskId());
        boolean subTasksCompleted = this.cacheService.getTrackJobIdsByParentTaskId(cachedJob.parentTaskId()).isEmpty();
        if (subTasksCompleted) {
            List<TrackRequest> completedTrackRequests = trackRequestService.getTrackRequestsByDomainId(domain.getId(),
                    PageRequest.of(0, 1),
                    new HashSet<>(Collections.singletonList(TrackRequest.TrackRequestStatus.COMPLETED)));
            TrackRequest trackRequest = trackRequestService.getTrackRequestById(cachedJob.trackRequestId());
            trackRequest.setStatus(TrackRequest.TrackRequestStatus.COMPLETED);
            Double currentAvgRank = this.keywordService.getAvgLatestRankByDomainId(domain.getId());
            Double prevAvgRank = completedTrackRequests.isEmpty() ? Objects.isNull(currentAvgRank) ? null : Double.valueOf(currentAvgRank.doubleValue()) : Double.valueOf(completedTrackRequests.get(0).getAverageRank());
            trackRequest.setAverageRank(currentAvgRank);
            trackRequest.setLastAverageRank(prevAvgRank);
            trackRequest.setScannedDate(new Date());
            this.taskSchedulerService.sendNotification(new NotificationQueueTask(NotificationQueueTask.ENotificationType.TRACK_JOB_COMPLETED, trackRequest.getId()));
        }
    }

    private List<Ranking.CompetitorRanking> getCompetitorRankings(List<SeoRankingResponse.Task.TaskResult.TaskItem> taskItems) {
        return taskItems.stream().map(v -> new Ranking.CompetitorRanking(v.domain(), v.url(), v.breadcrumb(), v.rank_absolute(), v.title(), v.description())).collect(Collectors.toList());
    }
}

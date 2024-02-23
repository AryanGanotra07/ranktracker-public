package com.codingee.ranked.ranktracker.service.task.receivers;

import com.codingee.ranked.ranktracker.config.RabbitMqConfig;
import com.codingee.ranked.ranktracker.dto.cache.TrackJobCacheDTO;
import com.codingee.ranked.ranktracker.dto.keyword.KeywordFilterDTO;
import com.codingee.ranked.ranktracker.dto.ranking.SeoRankingRequest;
import com.codingee.ranked.ranktracker.dto.ranking.SeoRankingTaskResponse;
import com.codingee.ranked.ranktracker.dto.task.TrackRequestQueueTask;
import com.codingee.ranked.ranktracker.dto.task.TrackRequestSubTask;
import com.codingee.ranked.ranktracker.model.domain.Domain;
import com.codingee.ranked.ranktracker.model.keyword.Keyword;
import com.codingee.ranked.ranktracker.model.track_request.TrackRequest;
import com.codingee.ranked.ranktracker.service.cache.ICacheService;
import com.codingee.ranked.ranktracker.service.domain.IDomainService;
import com.codingee.ranked.ranktracker.service.keyword.IKeywordService;
import com.codingee.ranked.ranktracker.service.seo.ISeoService;
import com.codingee.ranked.ranktracker.service.track_request.ITrackRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class TrackRequestReceiver {


    private final IDomainService domainService;
    private final ISeoService seoService;

    private final ICacheService cacheService;
    private final IKeywordService keywordService;

    private final ITrackRequestService trackRequestService;



    @RabbitListener(queues = RabbitMqConfig.TRACK_REQUEST_QUEUE)
    @Transactional
    public void receiveMessage(final TrackRequestQueueTask task) {
        log.info("Received track request for domain {}", task.getDomainId());
        Domain domain = this.domainService.getDomainById(task.getDomainId());
        TrackRequest trackRequest = this.trackRequestService.getTrackRequestById(task.getTrackRequestId());
        List<Keyword> keywords = this.keywordService.getKeywordsByFilter(new KeywordFilterDTO(domain.getId(), null, null));
        List<TrackRequestSubTask> subTaskList = getTrackRequestSubTasks(keywords, task.getId());
        try {
            subTaskList.forEach(subTask -> {
                List<SeoRankingRequest> seoRankingRequests = subTask.getKeywords().stream().map(st -> new SeoRankingRequest(st.getName(), st.getLocation(), st.getDeviceType(), st.getId().toString(), null, st.getLanguageCode())).toList();
                SeoRankingTaskResponse response = this.seoService.trackKeywords(seoRankingRequests);
                if (response != null && response.tasks_error() == 0) {
                    response.tasks().forEach(createdTask -> {

                            this.cacheService.putTrackJob(createdTask.id(), new TrackJobCacheDTO(task.getId(), createdTask.id(), task.getClientId(), task.getDomainId(), Long.valueOf(createdTask.data().tag()), trackRequest.getId()));

                    });
                }
            });
            trackRequest.setStatus(TrackRequest.TrackRequestStatus.SCANNING);
        } catch (Exception e) {
            trackRequest.setStatus(TrackRequest.TrackRequestStatus.FAILED);
        }
    }

    private List<TrackRequestSubTask> getTrackRequestSubTasks(final List<Keyword> keywords, final String parentTaskId) {
        final List<TrackRequestSubTask> subTasks = new ArrayList<>();

        if (keywords.isEmpty()) {
            return subTasks;
        }

        final List<Keyword> keywordList = new ArrayList<>(keywords);
        final int MAX_KEYWORDS_PER_BATCH = 100;
        final int numSubTasks = (int) Math.ceil(keywordList.size() / (double) MAX_KEYWORDS_PER_BATCH);

        for (int i = 0; i < numSubTasks; i++) {
            final int startIndex = i * MAX_KEYWORDS_PER_BATCH;
            final int endIndex = Math.min(startIndex + MAX_KEYWORDS_PER_BATCH, keywordList.size());
            final List<Keyword> subList = keywordList.subList(startIndex, endIndex);
            subTasks.add(new TrackRequestSubTask(parentTaskId, new HashSet<>(subList)));
        }

        return subTasks;
    }

}
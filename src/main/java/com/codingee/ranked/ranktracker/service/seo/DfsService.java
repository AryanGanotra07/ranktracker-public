package com.codingee.ranked.ranktracker.service.seo;

import com.codingee.ranked.ranktracker.dto.ranking.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class DfsService implements ISeoService {

    private final RestTemplate restTemplate;

    @Value("${dfs.api.baseUrl}")
    private String DFS_BASE_URL;
    private final ObjectMapper objectMapper;



    public DfsService(@Qualifier("dfs") final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public SeoRankingTaskResponse trackKeywords(List<SeoRankingRequest> data) {
        String endpoint = String.format("%s/v3/serp/google/organic/task_post", DFS_BASE_URL);
        try {
            String requestBody = objectMapper.writeValueAsString(data);
            ResponseEntity<SeoRankingTaskResponse> responseEntity = restTemplate.exchange(endpoint, HttpMethod.POST, new HttpEntity<>(requestBody), SeoRankingTaskResponse.class);
            return responseEntity.getBody();
        } catch (Exception e) {
           log.info(e.getMessage());
        }
        return null;
    }

    @Override
    public SeoRankingResponse getRankingFromTrackJobId(String trackJobId) {
        String endpoint = String.format("%s/v3/serp/google/organic/task_get/regular/%s", DFS_BASE_URL, trackJobId);
        try {
            ResponseEntity<SeoRankingResponse> responseEntity = restTemplate.exchange(endpoint, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), SeoRankingResponse.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }


    @Override
    public SeoRankingResponse getRankingForKeyword(List<SeoRankingLiveRequest> seoRankingRequests) {
        String endpoint = String.format("%s/v3/serp/google/organic/live/regular", DFS_BASE_URL);
        try {
            String requestBody = objectMapper.writeValueAsString(seoRankingRequests);
            ResponseEntity<SeoRankingResponse> responseEntity = restTemplate.exchange(endpoint, HttpMethod.POST, new HttpEntity<>(requestBody), SeoRankingResponse.class);
            return responseEntity.getBody();
        } catch (JsonProcessingException e) {
           throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public SeoTasksCompletedResponse getCompletedTasks() {
        String endpoint = String.format("%s/v3/serp/google/organic/tasks_ready", DFS_BASE_URL);
        try {
            ResponseEntity<SeoTasksCompletedResponse> responseEntity = restTemplate.exchange(endpoint, HttpMethod.GET,new HttpEntity<>(new HttpHeaders()), SeoTasksCompletedResponse.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }


}

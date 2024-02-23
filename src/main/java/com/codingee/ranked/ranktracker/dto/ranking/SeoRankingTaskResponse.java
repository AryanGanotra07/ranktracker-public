package com.codingee.ranked.ranktracker.dto.ranking;

import java.util.List;

public record SeoRankingTaskResponse(String status_message, int status_code, double cost, List<Task> tasks, int tasks_error) {
    public record Task(String id, int status_code, SeoRankingRequest data) {
    }

}

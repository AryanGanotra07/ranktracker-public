package com.codingee.ranked.ranktracker.dto.ranking;

import java.util.List;

public record SeoTasksCompletedResponse(int tasks_count, List<TaskResponse> tasks) {

    public record TaskResponse(int result_count, List<ResultResponse> result) {

        public record ResultResponse(String id, String tag) {}

    }

}

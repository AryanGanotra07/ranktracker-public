package com.codingee.ranked.ranktracker.dto.ranking;
import java.util.List;

public record SeoRankingResponse(List<Task> tasks) {
    public record Task(String id, int status_code, String status_message, String time, int cost, List<TaskResult> result) {
        public record TaskResult(String keyword, List<TaskItem> items) {
            public record TaskItem(String type, int rank_group, int rank_absolute, String domain, String title,
                            String description,
                            String url, String breadcrumb) {
            }
        }
    }

}

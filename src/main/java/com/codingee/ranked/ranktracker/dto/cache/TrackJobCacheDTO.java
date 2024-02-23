package com.codingee.ranked.ranktracker.dto.cache;

public record TrackJobCacheDTO(String parentTaskId, String taskId, Long clientId, Long domainId, Long keywordId, Long trackRequestId) {
}

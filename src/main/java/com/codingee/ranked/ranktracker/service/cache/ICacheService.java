package com.codingee.ranked.ranktracker.service.cache;

import com.codingee.ranked.ranktracker.dto.cache.TrackJobCacheDTO;

import java.util.Set;

public interface ICacheService {
    <T> T get(String key, Class<T> type);
    <T> void put(String key, T value);

    void putTrackJob(String taskId, TrackJobCacheDTO trackJobCacheDTO);

    TrackJobCacheDTO getTrackJob(String taskId);

    Set<String> getTrackJobIdsByParentTaskId(String parentTaskId);

    void deleteTrackJob(String taskJobId);
}

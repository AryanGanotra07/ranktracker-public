package com.codingee.ranked.ranktracker.service.cache;

import com.codingee.ranked.ranktracker.dto.cache.TrackJobCacheDTO;
import com.google.gson.Gson;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RedisCacheServiceImpl implements ICacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final Gson gson = new Gson();

    public RedisCacheServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public <T> T get(String key, Class<T> type) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return gson.fromJson(gson.toJsonTree(value), type);
    }

    public <T> void put(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void putTrackJob(String trackJobId, TrackJobCacheDTO trackJobCacheDTO) {
        put(getTrackJobKey(trackJobId), trackJobCacheDTO);
        redisTemplate.opsForSet().add(getParentTaskKey(trackJobCacheDTO.parentTaskId()), trackJobCacheDTO.taskId());
    }

    @Override
    public TrackJobCacheDTO getTrackJob(String taskId) {
        return get(getTrackJobKey(taskId), TrackJobCacheDTO.class);
    }

    @Override
    public Set<String> getTrackJobIdsByParentTaskId(String parentTaskId) {
        Set<Object> objects = redisTemplate.opsForSet().members(getParentTaskKey(parentTaskId));
        if (Objects.isNull(objects)) {
            return new HashSet<>();
        }
        return objects.stream().map(Object::toString).collect(Collectors.toSet());
    }

    @Override
    public void deleteTrackJob(String trackJobId) {
        TrackJobCacheDTO trackJobCacheDTO = getTrackJob(trackJobId);
        redisTemplate.opsForValue().getAndDelete(getTrackJobKey(trackJobId));
        redisTemplate.opsForSet().remove(getParentTaskKey(trackJobCacheDTO.parentTaskId()), trackJobId);
    }

    private String getTrackJobKey(String taskId) {
        return String.format("task_%s", taskId);
    }

    private String getParentTaskKey(String parentTaskId) {
        return String.format("process_%s", parentTaskId);
    }
}
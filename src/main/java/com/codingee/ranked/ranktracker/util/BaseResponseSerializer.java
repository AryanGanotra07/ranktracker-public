package com.codingee.ranked.ranktracker.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.IOException;

public class BaseResponseSerializer implements RedisSerializer<BaseResponse<?>> {

    private final ObjectMapper objectMapper;

    public BaseResponseSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public byte[] serialize(BaseResponse<?> baseResponse) throws SerializationException {
        try {
            return objectMapper.writeValueAsBytes(baseResponse);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error serializing BaseResponse", e);
        }
    }

    @Override
    public BaseResponse<?> deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        try {
            return objectMapper.readValue(bytes, BaseResponse.class);
        } catch (IOException e) {
            throw new SerializationException("Error deserializing BaseResponse", e);
        }
    }
}
package com.hisham.HomeCentre.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public <T> T get(String key, Class<T> clazz) {
        log.debug("Retrieving the value of key: {} from cache", key);
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            log.debug("Value not available for key: {} in cache", key);
            return null;
        }
        try {
            return objectMapper.readValue(value, clazz);
        } catch (IOException e) {
            log.error("Exception thrown in RedisService::get() while retrieving key: {}", key, e);
            throw new RuntimeException("Failed to retrieve value from cache for key: " + key, e);
        }
    }

    public void set(String key, Object value, Long ttl) {
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
            log.debug("Set key: {} in cache with TTL: {} seconds", key, ttl);
        } catch (Exception e) {
            log.error("Exception thrown in RedisService::set() while setting key: {}", key, e);
            throw new RuntimeException("Failed to set value in cache for key: " + key, e);
        }
    }

    public void delete(String key) {
        try {
            log.debug("Deleting the value of key: {} from cache", key);
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("Exception thrown in RedisService::delete() while deleting key: {}", key, e);
            throw new RuntimeException("Failed to delete value from cache for key: " + key, e);
        }
    }
}

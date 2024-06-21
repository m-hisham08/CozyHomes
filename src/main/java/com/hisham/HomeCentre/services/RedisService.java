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
    private RedisTemplate redisTemplate;

    public <T> T get(String key, Class<T> T){
        Object object = redisTemplate.opsForValue().get(key);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.debug("Retrieving the value of key: {} from cache", key);
           return objectMapper.reader().readValue(object.toString(), T);
        } catch (Exception e) {
            log.error("Exception thrown in RedisService::get() while retrieving key: {}", key, e);
            throw new RuntimeException("Failed to retrieve value from cache for key: " + key, e);
        }
    }

    public void set(String key, Object value, Long ttl){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
            log.debug("Set key: {} in cache with TTL: {} seconds", key, ttl);
        }catch (Exception e){
            log.error("Exception thrown in RedisService::set() while setting key: {}", key, e);
            throw new RuntimeException("Failed to set value in cache for key: " + key, e);
        }

    }
}

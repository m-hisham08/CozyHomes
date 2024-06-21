package com.hisham.HomeCentre.services;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;


    @Disabled
    @Test
    public void redisTest(){
        redisTemplate.opsForValue().set("CTC", "10k");
        Object email = redisTemplate.opsForValue().get("status");
        int i = 1;
    }
}

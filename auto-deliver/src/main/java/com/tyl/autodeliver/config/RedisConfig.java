package com.tyl.autodeliver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Configuration;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-06-15 9:43
 **/
@Configuration
public class RedisConfig extends CachingConfigurerSupport {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.timeout}")
    private int timeout;

    @Override
    public CacheManager cacheManager() {
        return super.cacheManager();
    }

    @Override
    public KeyGenerator keyGenerator() {
        return super.keyGenerator();
    }
}

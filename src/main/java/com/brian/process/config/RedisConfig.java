package com.brian.process.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author brian
 * @date 2024/10/3 22:22
 */
// 配置键值对的序列化方式
@Configuration
public class RedisConfig<T> {

    @Bean
    public RedisTemplate<String, T> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置 String key 的序列化规则
        redisTemplate.setKeySerializer(RedisSerializer.string());
        // 设置 value 的序列化方式
        redisTemplate.setValueSerializer(RedisSerializer.json());
        // 设置 hash key 的序列化规则
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        // 设置 hash value 的序列化规则
        redisTemplate.setHashValueSerializer(RedisSerializer.json());
        return redisTemplate;
    }
}

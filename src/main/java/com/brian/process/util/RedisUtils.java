package com.brian.process.util;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @author brian
 * @date 2024/10/3 21:27
 */
@Slf4j
@Component
public class RedisUtils<T> {

    @Resource
    private RedisTemplate<String, T> redisTemplate;

    /**
     * 删除缓存
     * @param key: 可以传一个或多个值
     */
    public void delete(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 获取存储数据结构
     * @param key
     */
    public T get(String key) {
        return key != null? redisTemplate.opsForValue().get(key): null;
    }

    /**
     * 设置指定 key 的值
     * @param key
     * @param value
     * @return boolean: 人工判定是否存储成功
     */
    public boolean set(String key, T value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            Throwable t = e.getCause();
            log.error("设置redisKey:{}, value:{} 时失败", key, value);
            return false;
        }
    }

    /**
     * 设置指定 key 的值(带过期时间, 秒为单位)
     * @param key
     * @param value
     * @return boolean: 人工判定是否存储成功
     */
    public boolean setEx(String key, T value, long expires) {
        try {
            redisTemplate.opsForValue().set(key, value, expires, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error("设置redisKey(过期时间):{}, value:{} 时失败", key, value);
            return false;
        }
    }
}

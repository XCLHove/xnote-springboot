package com.xclhove.xnote.tool;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * redis工具类
 *
 * @author xclhove
 */
@Component
@RequiredArgsConstructor
public class RedisTool {
    private final RedisTemplate<String, Object> redisTemplate;
    
    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }
    
    public void setValue(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }
    
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    
    public <T> T getValue(String key, Class<T> resultType) {
        return (T) getValue(key);
    }
    
    public Boolean deleteValue(String key) {
        return redisTemplate.delete(key);
    }
    
    public void setHashValue(String key, Object hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }
    
    public Object getHashValue(String key, Object hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }
    
    public <T> T getHashValue(String key, Object hashKey, Class<T> resultType) {
        return (T) getHashValue(key, hashKey);
    }
    
    public Long deleteHashValue(String key, Object... hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }
}
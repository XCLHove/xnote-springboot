package com.xclhove.xnote.tool;

import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * redis工具类
 *
 * @author xclhove
 */
@Component
@RequiredArgsConstructor
public class RedisTool {
    private final RedisTemplate<String, String> redisTemplate;
    
    public boolean connected() {
        try {
            redisTemplate.opsForValue().get("*");
            return true;
        } catch (RedisConnectionFailureException e) {
            return false;
        }
    }
    
    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, JSON.toJSONString(value));
    }
    
    public void setValue(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, JSON.toJSONString(value), timeout, unit);
    }
    
    public Object getValue(String key) {
        String value = redisTemplate.opsForValue().get(key);
        return JSON.parse(value);
    }
    
    public <T> T getValue(String key, Class<T> resultType) {
        return (T) getValue(key);
    }
    
    public Boolean deleteValue(String key) {
        return redisTemplate.delete(key);
    }
    
    public void setHashValue(String key, Object hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey.toString(), value);
    }
    
    public Object getHashValue(String key, Object hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey.toString());
    }
    
    public <T> T getHashValue(String key, Object hashKey, Class<T> resultType) {
        return (T) getHashValue(key, hashKey.toString());
    }
    
    public Long deleteHashValue(String key, Object... hashKeys) {
        return redisTemplate.opsForHash().delete(key, Arrays.toString(hashKeys));
    }
}
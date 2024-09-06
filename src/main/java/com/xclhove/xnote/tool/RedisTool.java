package com.xclhove.xnote.tool;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author xclhove
 */
@Component
@RequiredArgsConstructor
public final class RedisTool {
    private final StringRedisTemplate stringRedisTemplate;
    
    @FunctionalInterface
    public interface Getter<R> {
        /**
         * 从数据库中查询数据
         */
        R getFormDatabase();
    }
    
    @FunctionalInterface
    public interface Serializer<T, R> {
        /**
         * 序列化以便存到 redis
         */
        R serialize(T dataFormDatabase);
    }
    
    @FunctionalInterface
    public interface DeSerializer<T, R> {
        /**
         * 反序列化 redis 中存储的数据
         */
        R deserialize(T dataFormRedis);
    }
    
    /**
     * 防缓存穿透
     */
    public <R> R getUseStringAntiCachePassThrough(
            String redisKey,
            Getter<R> getter,
            Serializer<R, String> serializer,
            DeSerializer<String, R> deSerializer
    ) {
        String valueInRedis = stringRedisTemplate.opsForValue().get(redisKey);
        if (valueInRedis == null) {
            R objectInDatabase = getter.getFormDatabase();
            
            String value = "";
            if (objectInDatabase != null) {
                value = serializer.serialize(objectInDatabase);
            }
            stringRedisTemplate.opsForValue().set(redisKey, value);
            
            return objectInDatabase;
        }
        
        if (valueInRedis.isEmpty()) {
            return null;
        }
        
        R objectInRedis = deSerializer.deserialize(valueInRedis);
        return objectInRedis;
    }
    
    /**
     * 防缓存穿透
     */
    public <R> R getUseStringAntiCachePassThrough(
            String redisKey,
            Getter<R> getter,
            Serializer<R, String> serializer,
            DeSerializer<String, R> deSerializer,
            long timeout,
            TimeUnit timeUnit
    ) {
        String valueInRedis = stringRedisTemplate.opsForValue().get(redisKey);
        if (valueInRedis == null) {
            R objectInDatabase = getter.getFormDatabase();
            
            String value = "";
            if (objectInDatabase != null) {
                value = serializer.serialize(objectInDatabase);
            }
            stringRedisTemplate.opsForValue().set(redisKey, value, timeout, timeUnit);
            
            return objectInDatabase;
        }
        
        if (valueInRedis.isEmpty()) {
            return null;
        }
        
        R objectInRedis = deSerializer.deserialize(valueInRedis);
        return objectInRedis;
    }
    
    public <R> R getUseString(
            String redisKey,
            Getter<R> getter,
            Serializer<R, String> serializer,
            DeSerializer<String, R> deSerializer
    ) {
        String valueInRedis = stringRedisTemplate.opsForValue().get(redisKey);
        if (valueInRedis == null || valueInRedis.isEmpty()) {
            R objectInDatabase = getter.getFormDatabase();
            
            if (objectInDatabase != null) {
                String value = serializer.serialize(objectInDatabase);
                stringRedisTemplate.opsForValue().set(redisKey, value);
            }
            
            return objectInDatabase;
        }
        
        R objectInRedis = deSerializer.deserialize(valueInRedis);
        return objectInRedis;
    }
    
    public <R> R getUseString(
            String redisKey,
            Getter<R> getter,
            Serializer<R, String> serializer,
            DeSerializer<String, R> deSerializer,
            long timeout,
            TimeUnit timeUnit
    ) {
        String valueInRedis = stringRedisTemplate.opsForValue().get(redisKey);
        if (valueInRedis == null || valueInRedis.isEmpty()) {
            R objectInDatabase = getter.getFormDatabase();
            
            if (objectInDatabase != null) {
                String value = serializer.serialize(objectInDatabase);
                stringRedisTemplate.opsForValue().set(redisKey, value, timeout, timeUnit);
            }
            
            return objectInDatabase;
        }
        
        R objectInRedis = deSerializer.deserialize(valueInRedis);
        return objectInRedis;
    }
}

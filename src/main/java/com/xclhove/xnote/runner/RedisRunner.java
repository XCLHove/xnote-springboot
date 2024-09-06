package com.xclhove.xnote.runner;

import com.xclhove.xnote.config.XnoteConfig;
import com.xclhove.xnote.constant.RedisKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 启动时检查redis是否连接成功
 *
 * @author xclhove
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class RedisRunner implements ApplicationRunner {
    private final StringRedisTemplate stringRedisTemplate;
    private final XnoteConfig xnoteConfig;
    
    @Override
    public void run(ApplicationArguments args) {
        checkRedisStatus();
    }
    
    /**
     * 检查redis是否连接成功
     */
    private void checkRedisStatus() {
        if (!xnoteConfig.runner.getEnableCheckRedisStatus()) {
            log.info("跳过检查 redis 服务状态");
            return;
        }

        if (!connected()) {
            log.error("redis 连接失败！请检查配置文件和redis是否启动。");
            System.exit(1);
            return;
        }
        log.info("redis 连接成功！");
    }
    
    private boolean connected() {
        try {
            String redisKey = RedisKey.join(RedisKey.PROJECT, RedisKey.REDIS_CONNECT_TEST);
            stringRedisTemplate.opsForValue().set(redisKey, "test", 1, TimeUnit.SECONDS);
            return true;
        } catch (RedisConnectionFailureException e) {
            return false;
        }
    }
}

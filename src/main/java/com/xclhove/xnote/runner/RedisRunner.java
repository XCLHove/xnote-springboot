package com.xclhove.xnote.runner;

import com.xclhove.xnote.tool.RedisTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 启动时检查redis是否连接成功
 *
 * @author xclhove
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RedisRunner implements ApplicationRunner {
    private final RedisTool redisTool;
    
    @Override
    public void run(ApplicationArguments args) {
        checkRedis();
    }
    
    /**
     * 检查redis是否连接成功
     */
    public void checkRedis() {
        if (!redisTool.connected()) {
            log.error("redis连接失败！请检查配置文件和redis是否启动。");
            System.exit(1);
        }
    }
}

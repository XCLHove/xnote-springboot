package com.xclhove.xnote.runner;

import com.xclhove.xnote.tool.RedisTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 启动时检查redis是否连接成功
 *
 * @author xclhove
 */
@Component
@Slf4j
@Order(1)
public class RedisRunner extends AbstractRunner {
    @Resource
    private RedisTool redisTool;
    
    public RedisRunner(ApplicationContext applicationContext) {
        super(applicationContext);
    }
    
    @Override
    public void doRun(ApplicationArguments args) {
        checkRedisStatus();
    }
    
    /**
     * 检查redis是否连接成功
     */
    private void checkRedisStatus() {
        if (!runnerConfig.getEnableRedisStatusCheckRunner()) {
            return;
        }

        if (!redisTool.connected()) {
            log.error("redis连接失败！请检查配置文件和redis是否启动。");
            System.exit(1);
            return;
        }
        log.info("redis连接成功！");
    }
}

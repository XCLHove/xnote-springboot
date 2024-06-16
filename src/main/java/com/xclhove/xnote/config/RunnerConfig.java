package com.xclhove.xnote.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author xclhove
 * runner配置
 */
@Configuration
@ConfigurationProperties(prefix = "xnote.runner")
@Data
public class RunnerConfig {
    /**
     * 是否需要替换笔记中的图片地址
     */
    private Boolean enableImageUrlReplaceRunner = false;
    /**
     * 是否在启动后检查Minio的状态
     */
    private Boolean enableMinioStatusCheckRunner = true;
    /**
     * 是否在启动后检查Redis的状态
     */
    private Boolean enableRedisStatusCheckRunner = true;
    /**
     * 是否需要对笔记中的关键字存储格式进行转换
     */
    private Boolean enableKeywordsConvertRunner = false;
}

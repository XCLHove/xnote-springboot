package com.xclhove.xnote.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Minio配置类
 *
 * @author xclhove
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {
    /**
     * Minio服务所在地址
     */
    private String endpoint;
    /**
     * Minio服务外网访问地址
     */
    private String remoteEndpoint;
    /**
     * 存储桶名称
     */
    private String bucketName;
    /**
     * 访问的key
     */
    private String accessKey;
    /**
     * 访问的秘钥
     */
    private String secretKey;
    
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}

package com.xclhove.xnote.runner;

import com.xclhove.xnote.config.MinioConfig;
import com.xclhove.xnote.tool.MinioTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 启动时检查minio是否连接成功
 *
 * @author xclhove
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MinioRunner implements ApplicationRunner {
    private final MinioTool minioTool;
    
    @Override
    public void run(ApplicationArguments args) {
        checkMinio();
    }
    
    /**
     * 检查minio是否连接成功
     */
    public void checkMinio() {
        try {
            boolean bucketExist = minioTool.bucketExist();
            if (bucketExist) {
                return;
            }
            
            log.info("minio存储桶未创建，创建存储桶……");
            bucketExist = minioTool.creatBucket();
            
            if (!bucketExist) {
                log.error("创建存储桶失败！");
                System.exit(1);
                return;
            }
            
            log.info("创建存储桶成功！");
        } catch (Exception e) {
            log.error("minio连接失败！请检查配置文件和minio是否启动。");
            System.exit(1);
        }
    }
}

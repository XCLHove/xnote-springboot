package com.xclhove.xnote.runner;

import com.xclhove.xnote.tool.MinioTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 启动时检查minio是否连接成功
 *
 * @author xclhove
 */
@Component
@Slf4j
public class MinioRunner extends AbstractRunner  {
    @Resource
    private MinioTool minioTool;
    
    public MinioRunner(ApplicationContext applicationContext) {
        super(applicationContext);
    }
    
    @Override
    public void doRun(ApplicationArguments args) {
        checkMinio();
    }
    
    /**
     * 检查minio是否连接成功
     */
    private void checkMinio() {
        if (!runnerConfig.getEnableMinioStatusCheckRunner()) {
            return;
        }
        
        try {
            boolean bucketExist = minioTool.bucketExist();
            if (bucketExist) {
                log.info("minio连接成功！");
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

package com.xclhove.xnote.runner;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xclhove.xnote.config.XnoteConfig;
import com.xclhove.xnote.pojo.table.Image;
import com.xclhove.xnote.service.ImageService;
import com.xclhove.xnote.tool.MinioTool;
import io.minio.StatObjectResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xclhove
 */
@Component
@Slf4j
@RequiredArgsConstructor
@Order
public final class ImageRunner implements ApplicationRunner {
    private final XnoteConfig xnoteConfig;
    private final MinioTool minioTool;
    private final ImageService imageService;
    
    @Override
    public void run(ApplicationArguments args) {
        if (!xnoteConfig.runner.getUpdateImageSize()) {
            log.info("已关闭启动时更新图片大小");
        } else {
            log.info("开始更新图片大小...");
            updateImageSize();
            log.info("更新图片大小完成！");
        }
    }
    
    private void updateImageSize() {
        long total = 0;
        long current = 1;
        long pageSize = 1000L;
        do {
            Page<Image> page = imageService.page(new Page<>(current, pageSize));
            List<Image> deleteImages = new ArrayList<>();
            List<Image> updateImages = page
                    .getRecords()
                    .stream()
                    .filter(image -> image.getSize() != null)
                    .filter(image -> {
                        try {
                            StatObjectResponse file = minioTool.getFile(image.getName());
                            if (file == null) {
                                deleteImages.add(image);
                                return false;
                            }
                            image.setSize(file.size());
                        } catch (Exception e) {
                            log.error("获取图片 {} 大小失败", image.getName(), e);
                        }
                        return true;
                    }).collect(Collectors.toList());
            imageService.updateBatchByIdWithRedis(updateImages);
            imageService.removeBatchByIdsWithRedis(deleteImages);
            total = page.getTotal();
            current++;
        } while ((current - 1) * pageSize < total);
    }
}

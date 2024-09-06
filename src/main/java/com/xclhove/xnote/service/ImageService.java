package com.xclhove.xnote.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xclhove.xnote.config.XnoteConfig;
import com.xclhove.xnote.constant.RedisKey;
import com.xclhove.xnote.exception.ImageServiceException;
import com.xclhove.xnote.mapper.ImageMapper;
import com.xclhove.xnote.pojo.table.Image;
import com.xclhove.xnote.pojo.table.User;
import com.xclhove.xnote.pojo.table.UserImage;
import com.xclhove.xnote.tool.MinioTool;
import com.xclhove.xnote.tool.RedisTool;
import com.xclhove.xnote.util.ByteSizeUtil;
import com.xclhove.xnote.util.Md5Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author xclhove
 */
@Service
@RequiredArgsConstructor
public class ImageService extends ServiceImpl<ImageMapper, Image> {
    private final static int IMAGE_ALIAS_MAX_LENGTH = 30;
    private final static String IMAGE_CONTENT_TYPE = "^image/(png|jpg|jpeg|svg|ico|gif|bmp)+$";
    
    private final MinioTool minioTool;
    private final StringRedisTemplate stringRedisTemplate;
    private final XnoteConfig xnoteConfig;
    private final RedisTool redisTool;
    private final ImageMapper imageMapper;
    
    @Resource
    private UserImageService userImageService;
    
    /**
     * 防缓存穿透
     */
    private Image getWithRedis(String redisKey, RedisTool.Getter<Image> getter) {
        return redisTool.getUseStringAntiCachePassThrough(
                redisKey,
                getter,
                JSON::toJSONString,
                jsonString -> JSON.parseObject(jsonString, Image.class),
                30,
                TimeUnit.MINUTES
        );
    }
    
    public String getImageRedisKeyByName(String imageName) {
        return RedisKey.join(RedisKey.Image.NAME, imageName);
    }
    
    public String getImageUrlRedisKeyByName(String imageName) {
        return RedisKey.join(RedisKey.Image.URL, imageName);
    }
    
    public String getUserImageTotalSizeRedisKey(int userId) {
        return RedisKey.join(RedisKey.Image.USER_TOTAL_SIZE, String.valueOf(userId));
    }
    
    public long countUserImageTotalSize(int userId) {
        List<Integer> imageIds = userImageService.listByUserId(userId)
                .stream()
                .map(UserImage::getImageId)
                .collect(Collectors.toList());
        if (imageIds.isEmpty()) {
            return 0;
        }
        
        LambdaQueryWrapper<Image> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Image::getSize);
        queryWrapper.in(Image::getId, imageIds);
        List<Image> images = this.list(queryWrapper);
        long totalSize = images.stream().mapToLong(Image::getSize).sum();
        return totalSize;
    }
    
    public long countUserImageTotalSizeWithRedis(int userId) {
        return redisTool.getUseString(
                getUserImageTotalSizeRedisKey(userId),
                () -> countUserImageTotalSize(userId),
                String::valueOf,
                Long::parseLong,
                30,
                TimeUnit.MINUTES
                );
    }
    
    public String upload(User user, MultipartFile file) {
        long imageSize = file.getSize();
        final Long allowMaxSizeOfByte = xnoteConfig.image.getAllowSizeOfByte();
        if (imageSize > allowMaxSizeOfByte) {
            throw new ImageServiceException("图片大小不能超过" + ByteSizeUtil.parseSizeWithUnit(allowMaxSizeOfByte));
        }
        
        String contentType = file.getContentType();
        if (StrUtil.isBlank(contentType) || !contentType.matches(IMAGE_CONTENT_TYPE)) {
            throw new ImageServiceException("图片格式错误");
        }
        
        StringBuilder imageName = new StringBuilder();
        imageName.append(user.getId());
        imageName.append("_");
        try {
            imageName.append(Md5Util.getMd5(file.getBytes()));
        } catch (Exception e) {
            throw new ImageServiceException("系统异常，图片上传失败");
        }
        imageName.append(".");
        imageName.append(contentType.substring(contentType.lastIndexOf("/") + 1));
        
        // 最后需要清除防缓存穿透产生的空数据
        Image existedImage = getByNameWithRedis(imageName.toString());
        if (existedImage != null) {
            if (userImageService.getByUserAndImageIdWithRedis(user.getId(), existedImage.getId()) != null) {
                return imageName.toString();
            }
            
            UserImage userImage = new UserImage();
            userImage.setImageId(existedImage.getId());
            userImage.setUserId(user.getId());
            userImage.setAlias(file.getOriginalFilename());
            boolean saveSuccess = userImageService.saveWithRedis(userImage);
            if (!saveSuccess) {
                throw new ImageServiceException("图片上传失败");
            }
            imageMapper.incrementImageOwnerCount(Collections.singletonList(existedImage.getId()), 1);
            return imageName.toString();
        }
        
        long totalSize = countUserImageTotalSizeWithRedis(user.getId()) + imageSize;
        if (totalSize > user.getImageStorageSize()) {
            throw new ImageServiceException("图片存储空间不足");
        }
        
        try {
            minioTool.upload(file, imageName.toString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ImageServiceException("图片上传失败");
        }
        
        Image image = new Image();
        image.setName(imageName.toString());
        image.setSize(imageSize);
        boolean saveSuccess = this.save(image);
        if (!saveSuccess) {
            try {
                minioTool.deleteFile(imageName.toString());
            } catch (Exception e) {
                log.error("删除 minio 中的图片失败", e);
            }
            throw new ImageServiceException("图片信息保存失败");
        }
        
        UserImage userImage = new UserImage();
        userImage.setImageId(image.getId());
        userImage.setUserId(user.getId());
        userImage.setAlias(file.getOriginalFilename());
        boolean userImageSaveSuccess = userImageService.save(userImage);
        if (!userImageSaveSuccess) {
            throw new ImageServiceException("用户图片上传失败");
        }
        
        // 更新 redis 中的用户图片总大小
        stringRedisTemplate.opsForValue().increment(getUserImageTotalSizeRedisKey(user.getId()), imageSize);
        // 清除防缓存穿透的空数据
        stringRedisTemplate.delete(getImageRedisKeyByName(imageName.toString()));
        return imageName.toString();
    }
    
    @Nullable
    public Image getByName(String imageName) {
        Image image = getOne(new LambdaQueryWrapper<Image>().eq(Image::getName, imageName));
        if (image == null) {
            return null;
        }
        image.setLastDownloadTime(new Timestamp(System.currentTimeMillis()));
        updateById(image);
        return image;
    }
    
    /**
     * 防缓存穿透
     */
    @Nullable
    public Image getByNameWithRedis(String imageName) {
        return getWithRedis(getImageRedisKeyByName(imageName), () -> getByName(imageName));
    }
    
    @Nullable
    public String getImageUrlByName(String imageName) {
        Image image = getByNameWithRedis(imageName);
        if (image == null) {
            return null;
        }
        return minioTool.getFileUrl(image.getName());
    }
    
    @Nullable
    public String getImageUrlByNameWithRedis(String imageName) {
        return redisTool.getUseStringAntiCachePassThrough(
                getImageUrlRedisKeyByName(imageName),
                () -> getImageUrlByName(imageName),
                (value) -> value,
                (value) -> value,
                30,
                TimeUnit.MINUTES
        );
    }
    
    public boolean updateBatchByIdWithRedis(List<Image> images) {
        boolean updateSuccess = this.updateBatchById(images);
        if (updateSuccess) {
            stringRedisTemplate.delete(images.stream()
                    .map(image -> getImageRedisKeyByName(image.getName()))
                    .collect(Collectors.toList())
            );
        }
        return updateSuccess;
    }
    
    public boolean removeBatchByIdsWithRedis(Collection<Image> images) {
        boolean removeSuccess = removeBatchByIds(images);
        if (removeSuccess) {
            Set<String> keys = new HashSet<>();
            images.forEach(image -> {
                keys.add(getImageRedisKeyByName(image.getName()));
                keys.add(getImageUrlRedisKeyByName(image.getName()));
            });
            stringRedisTemplate.delete(keys);
        }
        return removeSuccess;
    }
}

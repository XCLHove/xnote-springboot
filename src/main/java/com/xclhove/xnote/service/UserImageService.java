package com.xclhove.xnote.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xclhove.xnote.constant.RedisKey;
import com.xclhove.xnote.exception.UserImageServiceException;
import com.xclhove.xnote.mapper.ImageMapper;
import com.xclhove.xnote.mapper.UserImageMapper;
import com.xclhove.xnote.pojo.table.Image;
import com.xclhove.xnote.pojo.table.User;
import com.xclhove.xnote.pojo.table.UserImage;
import com.xclhove.xnote.pojo.vo.SearchUserImageVO;
import com.xclhove.xnote.tool.RedisTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author xclhove
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserImageService extends ServiceImpl<UserImageMapper, UserImage> {
    private final RedisTool redisTool;
    private final StringRedisTemplate stringRedisTemplate;
    private final ImageService imageService;
    private final ImageMapper imageMapper;
    
    public UserImage getWithRedis(String redisKey, RedisTool.Getter<UserImage> getter) {
        return redisTool.getUseStringAntiCachePassThrough(
                redisKey,
                getter,
                JSON::toJSONString,
                json -> JSON.parseObject(json, UserImage.class),
                30,
                TimeUnit.MINUTES
        );
    }
    
    public String getRedisKeyByUserAndImageId(Integer userId, Integer imageId) {
        return RedisKey.join(RedisKey.UserImage.USER_AND_IMAGE_ID, userId.toString(), imageId.toString());
    }
    
    @Nullable
    public UserImage getByUserAndImageId(Integer userId,Integer imageId) {
        LambdaQueryWrapper<UserImage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserImage::getUserId, userId);
        queryWrapper.eq(UserImage::getImageId, imageId);
        return this.getOne(queryWrapper);
    }
    
    @Nullable
    public UserImage getByUserAndImageIdWithRedis(Integer userId, Integer imageId) {
        return getWithRedis(
                getRedisKeyByUserAndImageId(userId, imageId),
                () -> getByUserAndImageId(userId, imageId)
        );
    }
    
    public List<UserImage> listByUserId(Integer userId) {
        LambdaQueryWrapper<UserImage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserImage::getUserId, userId);
        return this.list(queryWrapper);
    }
    
    public boolean saveWithRedis(UserImage userImage) {
        boolean saveSuccess = this.save(userImage);
        stringRedisTemplate.delete(getRedisKeyByUserAndImageId(userImage.getUserId(), userImage.getImageId()));
        return saveSuccess;
    }
    
    public void removeUserImageByIds(@NonNull User user, @NonNull List<Integer> userImageIds) {
        LambdaQueryWrapper<UserImage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserImage::getUserId, user.getId());
        queryWrapper.in(UserImage::getId, userImageIds);
        List<UserImage> removeUserImages = list(queryWrapper);
        if (removeUserImages.isEmpty()) {
            return;
        }
        boolean removeSuccess = removeBatchByIds(removeUserImages);
        if (!removeSuccess) {
            throw new UserImageServiceException("系统异常，删除失败");
        }
        
        List<Integer> removeImageIds = removeUserImages.stream().map(UserImage::getImageId).collect(Collectors.toList());
        imageMapper.incrementImageOwnerCount(removeImageIds, -1);
        
        Set<String> redisKeys = new HashSet<>();
        removeUserImages.forEach(userImage -> redisKeys.add(getRedisKeyByUserAndImageId(
                userImage.getUserId(),
                userImage.getImageId()
        )));
        redisKeys.add(imageService.getUserImageTotalSizeRedisKey(user.getId()));
        stringRedisTemplate.delete(redisKeys);
    }
    
    public Page<SearchUserImageVO> searchUserImage(@NonNull Page<UserImage> page, @NonNull User user, @Nullable String search) {
        LambdaQueryWrapper<UserImage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserImage::getUserId, user.getId());
        queryWrapper.like(StrUtil.isNotBlank(search), UserImage::getAlias, search);
        Page<UserImage> pageResult = this.page(page, queryWrapper);
        List<SearchUserImageVO> searchUserImageVOList = BeanUtil.copyToList(
                pageResult.getRecords(),
                SearchUserImageVO.class
        );
        
        Set<Integer> imageIds = new HashSet<>();
        searchUserImageVOList.forEach(searchUserImageVO -> imageIds.add(searchUserImageVO.getImageId()));
        
        if (!imageIds.isEmpty()) {
            LambdaQueryWrapper<Image> imageLambdaQueryWrapper = new LambdaQueryWrapper<>();
            imageLambdaQueryWrapper.in(Image::getId, imageIds);
            List<Image> images = imageService.list(imageLambdaQueryWrapper);
            Map<Integer, Timestamp> imageLastDownloadTimeMap = new HashMap<>(images.size());
            images.forEach(image -> imageLastDownloadTimeMap.put(image.getId(), image.getLastDownloadTime()));
            searchUserImageVOList.forEach(searchUserImageVO -> {
                searchUserImageVO.setLastDownloadTime(imageLastDownloadTimeMap.get(searchUserImageVO.getImageId()));
            });
        }
        
        Page<SearchUserImageVO> pageVO = new Page<>();
        BeanUtil.copyProperties(pageResult, pageVO);
        pageVO.setRecords(searchUserImageVOList);
        return pageVO;
    }
}

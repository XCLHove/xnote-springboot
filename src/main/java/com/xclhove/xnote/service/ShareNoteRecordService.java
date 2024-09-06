package com.xclhove.xnote.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xclhove.xnote.constant.RedisKey;
import com.xclhove.xnote.exception.ShareNoteRecordServiceException;
import com.xclhove.xnote.mapper.ShareNoteRecordMapper;
import com.xclhove.xnote.pojo.form.shareNoteRecord.ShareNoteRecordCreateForm;
import com.xclhove.xnote.pojo.form.shareNoteRecord.ShareNoteRecordUpdateForm;
import com.xclhove.xnote.pojo.table.ShareNoteRecord;
import com.xclhove.xnote.pojo.table.User;
import com.xclhove.xnote.pojo.vo.ShareNoteRecordVO;
import com.xclhove.xnote.tool.RedisTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author xclhove
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ShareNoteRecordService extends ServiceImpl<ShareNoteRecordMapper, ShareNoteRecord> {
    private final StringRedisTemplate stringRedisTemplate;
    private final ShareNoteRecordMapper shareNoteRecordMapper;
    private final RedisTool redisTool;
    
    public ShareNoteRecord getWithRedis(String redisKey, RedisTool.Getter<ShareNoteRecord> getter) {
        return redisTool.getUseStringAntiCachePassThrough(
                redisKey,
                getter,
                JSON::toJSONString,
                jsonString -> JSON.parseObject(jsonString, ShareNoteRecord.class),
                30,
                TimeUnit.MINUTES
        );
    }
    
    public String generateCode() {
        return UUID.randomUUID().toString(true);
    }
    
    public String getRedisKeyByCode(String code) {
        return RedisKey.join(RedisKey.ShareNote.CODE, code);
    }
    
    public String getRedisKeyById(Integer id) {
        return RedisKey.join(RedisKey.ShareNote.ID, String.valueOf(id));
    }
    
    public String share(User noteOwner, ShareNoteRecordCreateForm shareNoteRecordCreateForm) {
        String code = generateCode();
        
        ShareNoteRecord shareNoteRecord = BeanUtil.copyProperties(shareNoteRecordCreateForm, ShareNoteRecord.class);
        shareNoteRecord.setCode(code);
        shareNoteRecord.setUserId(noteOwner.getId());
        boolean saveSuccess = this.save(shareNoteRecord);
        if (!saveSuccess) {
            throw new ShareNoteRecordServiceException();
        }
        return code;
    }
    
    public ShareNoteRecord getByCode(String code) {
        LambdaQueryWrapper<ShareNoteRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShareNoteRecord::getCode, code);
        return this.getOne(queryWrapper);
    }
    
    public ShareNoteRecord getByCodeWithRedis(String code) {
        return getWithRedis(getRedisKeyByCode(code), () -> getByCode(code));
    }
    
    public void deleteByCodeInRedis(String code) {
        String redisKey = getRedisKeyByCode(code);
        stringRedisTemplate.delete(redisKey);
    }
    
    public void deleteShareNotes(User user, List<Integer> shareNoteIds) {
        LambdaQueryWrapper<ShareNoteRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShareNoteRecord::getUserId, user.getId());
        queryWrapper.in(ShareNoteRecord::getId, shareNoteIds);
        List<ShareNoteRecord> deleteRecords = this.list(queryWrapper);
        boolean removeSuccess = this.removeBatchByIds(deleteRecords);
        if (!removeSuccess) {
            throw new ShareNoteRecordServiceException();
        }
        
        Set<String> redisKeys = new HashSet<>();
        deleteRecords.forEach(shareNoteRecord -> {
            redisKeys.add(getRedisKeyByCode(shareNoteRecord.getCode()));
            redisKeys.add(getRedisKeyById(shareNoteRecord.getId()));
        });
        stringRedisTemplate.delete(redisKeys);
    }
    
    public Page<ShareNoteRecordVO> getShareNoteList(User user, Page<ShareNoteRecordVO> page) {
        LambdaQueryWrapper<ShareNoteRecordVO> queryWrapper = new LambdaQueryWrapper<>();
        Page<ShareNoteRecordVO> pageResult = shareNoteRecordMapper.pageUserShareNote(user.getId(), page, queryWrapper);
        return pageResult;
    }
    
    public void deleteById(ShareNoteRecord shareNoteRecord) {
        this.removeById(shareNoteRecord.getId());
        deleteByCodeInRedis(shareNoteRecord.getCode());
    }
    
    public ShareNoteRecord getByIdWithRedis(Integer id) {
        return getWithRedis(getRedisKeyById(id), () -> this.getById(id));
    }
    
    public void deleteByIdInRedis(Integer id) {
        String redisKey = getRedisKeyById(id);
        stringRedisTemplate.delete(redisKey);
    }
    
    public void update(User user, ShareNoteRecordUpdateForm shareNoteRecordUpdateForm) {
        ShareNoteRecord shareNoteRecord = this.getById(shareNoteRecordUpdateForm.getId());
        if (shareNoteRecord == null || !shareNoteRecord.getUserId().equals(user.getId())) {
            throw new ShareNoteRecordServiceException("分享记录不存在");
        }
        BeanUtil.copyProperties(shareNoteRecordUpdateForm, shareNoteRecord);
        boolean updateSuccess = this.updateById(shareNoteRecord);
        if (!updateSuccess) {
            throw new ShareNoteRecordServiceException("系统异常，更新失败");
        }
        
        // 删除 redis 中的缓存
        deleteByCodeInRedis(shareNoteRecord.getCode());
        deleteByIdInRedis(shareNoteRecord.getId());
    }
}

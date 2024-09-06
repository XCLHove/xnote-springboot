package com.xclhove.xnote.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xclhove.xnote.constant.RedisKey;
import com.xclhove.xnote.exception.NoteTypeServiceException;
import com.xclhove.xnote.mapper.NoteTypeMapper;
import com.xclhove.xnote.pojo.form.noteType.NoteTypeAddForm;
import com.xclhove.xnote.pojo.form.noteType.NoteTypeUpdateForm;
import com.xclhove.xnote.pojo.table.Note;
import com.xclhove.xnote.pojo.table.NoteType;
import com.xclhove.xnote.pojo.table.User;
import com.xclhove.xnote.tool.RedisTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author xclhove
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NoteTypeService extends ServiceImpl<NoteTypeMapper, NoteType> {
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTool redisTool;
    @Autowired
    private NoteService noteService;
    
    public NoteType getWithRedis(String redisKey, RedisTool.Getter<NoteType> getter) {
        return redisTool.getUseStringAntiCachePassThrough(
                redisKey,
                getter,
                JSON::toJSONString,
                jsonString -> JSON.parseObject(jsonString, NoteType.class),
                30,
                TimeUnit.MINUTES
        );
    }
    
    public String getUserAllNoteTypeRedisKey(int userId) {
        return RedisKey.join(RedisKey.NoteType.USER_ALL, String.valueOf(userId));
    }
    
    public String getNoteTypeRedisKeyById(int noteTypeId) {
        return RedisKey.join(RedisKey.NoteType.ID, String.valueOf(noteTypeId));
    }
    
    /**
     * 获取笔记类型（使用redis做缓存，有防缓存击穿）
     */
    public NoteType getByIdWithRedis(int noteTypeId) {
        return getWithRedis(getNoteTypeRedisKeyById(noteTypeId), () -> getById(noteTypeId));
    }
    
    public void deleteByIdInRedis(int noteTypeId) {
        stringRedisTemplate.delete(getNoteTypeRedisKeyById(noteTypeId));
    }
    
    /**
     * 新增笔记类型
     */
    public void addNoteType(User user, NoteTypeAddForm noteTypeAddForm) {
        NoteType noteType = BeanUtil.copyProperties(noteTypeAddForm, NoteType.class);
        noteType.setUserId(user.getId());
        boolean success = this.save(noteType);
        if (!success) {
            throw new NoteTypeServiceException();
        }
        
        deleteUserAllNoteTypeInRedis(user.getId());
    }
    
    /**
     * 删除笔记类型
     */
    public void deleteNoteTypeByIds(User user, List<Integer> noteTypeIds) {
        LambdaQueryWrapper<NoteType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(NoteType::getId, noteTypeIds);
        queryWrapper.eq(NoteType::getUserId, user.getId());
        List<NoteType> noteTypeList = this.list(queryWrapper);
        
        noteTypeList.forEach(noteType -> {
            LambdaQueryWrapper<Note> noteQueryWrapper = new LambdaQueryWrapper<>();
            noteQueryWrapper.eq(Note::getTypeId, noteType.getId());
            long count = noteService.count(noteQueryWrapper);
            if (count > 0) {
                throw new NoteTypeServiceException(String.format("请先删除或转移<%s>类型下的笔记", noteType.getName()));
            }
        });
        this.removeBatchByIds(noteTypeList);
        
        // 清除 redis 中的缓存
        noteTypeList.forEach(noteType -> deleteByIdInRedis(noteType.getId()));
        deleteUserAllNoteTypeInRedis(user.getId());
    }
    
    /**
     * 更新笔记类型
     */
    public void updateNoteType(User user, NoteTypeUpdateForm noteTypeUpdateForm) {
        NoteType noteType = BeanUtil.copyProperties(noteTypeUpdateForm, NoteType.class);
        noteType.setUserId(user.getId());
        boolean updateSuccess = this.updateById(noteType);
        if (!updateSuccess) {
            throw new NoteTypeServiceException("更新失败");
        }
        
        // 清除 redis 中的缓存
        deleteByIdInRedis(noteType.getId());
        deleteUserAllNoteTypeInRedis(user.getId());
    }
    
    /**
     * 获取用户所有笔记类型
     */
    public List<NoteType> getUserAllNoteType(int userId) {
        LambdaQueryWrapper<NoteType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(NoteType::getUserId, userId);
        List<NoteType> listInDatabase = this.list(queryWrapper);
        
        if (listInDatabase.isEmpty()) {
            NoteType defaultNoteType = createDefaultAndSetToNoteOfNoneType(userId);
            return Collections.singletonList(defaultNoteType);
        }
        
        return listInDatabase;
    }
    
    public NoteType createDefaultAndSetToNoteOfNoneType(int userId) {
        NoteType defaultNoteType = new NoteType();
        defaultNoteType.setUserId(userId);
        defaultNoteType.setName("默认");
        this.save(defaultNoteType);
        
        LambdaQueryWrapper<Note> queryNoteWrapper = new LambdaQueryWrapper<>();
        queryNoteWrapper.eq(Note::getUserId, userId);
        queryNoteWrapper.isNull(Note::getTypeId);
        List<Note> notesOfNoneType = noteService.list(queryNoteWrapper);
        notesOfNoneType.forEach(note -> note.setTypeId(defaultNoteType.getId()));
        noteService.updateBatchByIdWithRedisAndEs(notesOfNoneType);
        return defaultNoteType;
    }
    
    /**
     * 获取用户所有笔记类型（使用redis做缓存，有防缓存击穿）
     */
    public List<NoteType> getUserAllNoteTypeWithRedis(int userId) {
        return redisTool.getUseStringAntiCachePassThrough(
                getUserAllNoteTypeRedisKey(userId),
                () -> getUserAllNoteType(userId),
                JSON::toJSONString,
                jsonString -> JSON.parseArray(jsonString, NoteType.class),
                30,
                TimeUnit.MINUTES
        );
    }
    
    /**
     * 删除 redis 中用户所有笔记类型
     */
    public void deleteUserAllNoteTypeInRedis(int userId) {
        stringRedisTemplate.delete(getUserAllNoteTypeRedisKey(userId));
    }
}

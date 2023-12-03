package com.xclhove.xnote.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xclhove.xnote.entity.dto.NotePageDTO;
import com.xclhove.xnote.entity.table.Note;
import com.xclhove.xnote.exception.NoteServiceException;
import com.xclhove.xnote.mapper.NoteMapper;
import com.xclhove.xnote.service.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author xclhove
 */
@Service
@Slf4j
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note> implements NoteService {
    @Override
    public Note getNoteById(Integer noteId) {
        LambdaQueryWrapper<Note> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Note::getId, noteId);
        Note note = this.getOne(queryWrapper);
        if (note == null) throw new NoteServiceException("没有该笔记！");
        return note;
    }
    
    @Override
    public Note addNote(Note note) {
        String title = note.getTitle();
        if (StrUtil.isBlank(title)) title = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        note.setTitle(title);
        boolean saveSuccess = false;
        try {
            saveSuccess = this.save(note);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new NoteServiceException("出现异常，保存失败！");
        }
        if (!saveSuccess) throw new NoteServiceException("保存失败！");
        note.setTitle(null);
        note.setKeywords(null);
        note.setContent(null);
        return note;
    }
    
    @Override
    public Note updateNote(Note note) {
        boolean updateSuccess = false;
        try {
            LambdaUpdateWrapper<Note> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Note::getId, note.getId());
            updateWrapper.eq(Note::getUserId, note.getUserId());
            updateSuccess = this.update(note, updateWrapper);
        } catch (Exception e) {
            log.error(e.toString());
            throw new NoteServiceException("出现异常，更新失败！");
        }
        if (!updateSuccess) throw new NoteServiceException("更新失败！");
        note.setTitle(null);
        note.setKeywords(null);
        note.setContent(null);
        return note;
    }
    
    @Override
    public boolean deleteNote(Integer userId, Integer noteId) {
        boolean deleteSuccess = false;
        try {
            LambdaQueryWrapper<Note> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Note::getId, noteId);
            queryWrapper.eq(userId != null, Note::getUserId, userId);
            deleteSuccess = this.removeById(queryWrapper);
        } catch (Exception e) {
            log.error(e.toString());
            throw new NoteServiceException("出现异常，删除失败！");
        }
        if (!deleteSuccess) throw new NoteServiceException("删除失败！");
        return true;
    }
    
    @Override
    public NotePageDTO pageNote(NotePageDTO notePageDTO) {
        try {
            Page<Note> page = new Page<>(notePageDTO.getCurrent(), notePageDTO.getSize());
            LambdaQueryWrapper<Note> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.select(Note::getId, Note::getTitle);
            queryWrapper.eq(notePageDTO.getUserId() != null, Note::getUserId, notePageDTO.getUserId());
            queryWrapper.like(StrUtil.isNotBlank(notePageDTO.getSearchTitle()), Note::getTitle, notePageDTO.getSearchTitle())
                    .and(StrUtil.isNotBlank(notePageDTO.getSearchContent()), qw -> qw.like(Note::getContent, notePageDTO.getSearchContent()))
                    .and(StrUtil.isNotBlank(notePageDTO.getSearchKeyword()), qw -> qw.like(Note::getKeywords, notePageDTO.getSearchKeyword()));
            List<Note> notes = this.page(page, queryWrapper).getRecords();
            Integer total = this.list(queryWrapper).size();
            notePageDTO.setList(notes);
            notePageDTO.setTotal(total);
            return notePageDTO;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new NoteServiceException("出现异常，分页失败！");
        }
    }
}

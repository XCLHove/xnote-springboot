package com.xclhove.xnote.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
    public Note getNote(Integer noteId) {
        LambdaQueryWrapper<Note> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Note::getId, noteId);
        Note note = this.getOne(queryWrapper);
        if (note == null) throw new NoteServiceException("没有该笔记！");
        return note;
    }
    
    @Override
    public boolean addNote(Note note) {
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
        return true;
    }
    
    @Override
    public boolean updateNote(Note newNote) {
        
        Note oldNote = this.getById(newNote.getId());
        if (oldNote == null) throw new NoteServiceException("笔记不存在!");
        if (!Objects.equals(newNote.getUserId(), oldNote.getUserId()))
            throw new NoteServiceException("你没有修改该笔记的权限！");
        boolean updateSuccess = false;
        try {
            updateSuccess = this.updateById(newNote);
        } catch (Exception e) {
            log.error(e.toString());
            throw new NoteServiceException("出现异常，更新失败！");
        }
        if (!updateSuccess) throw new NoteServiceException("更新失败！");
        return true;
    }
    
    @Override
    public boolean deleteNote(Integer noteId) {
        boolean deleteSuccess = false;
        try {
            deleteSuccess = this.removeById(noteId);
        } catch (Exception e) {
            log.error(e.toString());
            throw new NoteServiceException("出现异常，删除失败！");
        }
        if (!deleteSuccess) throw new NoteServiceException("删除失败！");
        return true;
    }
    
    @Override
    public List<Note> getUserAllNote(Integer userId) {
        LambdaQueryWrapper<Note> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Note::getUserId, userId);
        try {
            List<Note> userNotes = this.list(queryWrapper);
            return userNotes;
        } catch (Exception e) {
            log.error(e.toString());
            throw new NoteServiceException("出现异常，获取失败！");
        }
    }
    
    @Override
    public List<Note> searchNote(String text) {
        LambdaQueryWrapper<Note> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Note::getTitle, text)
                .or().like(Note::getContent, text)
                .or().like(Note::getKeywords, text);
        try {
            List<Note> notes = this.list(queryWrapper);
            return notes;
        } catch (Exception e) {
            log.error(e.toString());
            throw new NoteServiceException("出现异常，搜索失败！");
        }
    }
    
    @Override
    public List<Note> getAllNote() {
        LambdaQueryWrapper<Note> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Note::getId, Note::getTitle, Note::getUserId);
        try {
            List<Note> notes = this.list(queryWrapper);
            return notes;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new NoteServiceException("出现异常，获取失败！");
        }
    }
}

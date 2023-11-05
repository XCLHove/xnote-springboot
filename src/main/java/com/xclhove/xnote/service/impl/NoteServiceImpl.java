package com.xclhove.xnote.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xclhove.xnote.entity.table.Note;
import com.xclhove.xnote.mapper.NoteMapper;
import com.xclhove.xnote.service.NoteService;
import com.xclhove.xnote.util.Result;
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
    public Result<Note> getNote(Integer noteId) {
        LambdaQueryWrapper<Note> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Note::getId, noteId);
        Note note = this.getOne(queryWrapper);
        if (note == null) {
            return Result.error("没有该笔记！");
        }
        return Result.success(note);
    }
    
    @Override
    public Result<Integer> addNote(Note note) {
        try {
            String title = note.getTitle();
            if (StrUtil.isBlank(title)) {
                title = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            }
            note.setTitle(title);
            boolean saveSuccess = this.save(note);
            if (!saveSuccess) {
                return Result.error("保存失败！");
            }
            LambdaQueryWrapper<Note> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Note::getTitle, note.getTitle())
                    .eq(Note::getUserId, note.getUserId());
            note = this.getOne(queryWrapper);
            if (note == null) {
                return Result.error("保存失败！");
            }
            return Result.success(note.getId());
        } catch (Exception e) {
            log.error(e.toString());
            return Result.error("保存失败！");
        }
    }
    
    @Override
    public Result<Integer> updateNote(Note newNote) {
        try {
            Note oldNote = this.getById(newNote.getId());
            if (oldNote == null) {
                return Result.error("笔记不存在!");
            }
            if (!Objects.equals(newNote.getUserId(), oldNote.getUserId())) {
                return Result.error("你没有修改该笔记的权限！");
            }
            boolean updateSuccess = this.updateById(newNote);
            if (!updateSuccess) {
                return Result.error("更新失败！");
            }
            return Result.success(newNote.getId());
        } catch (Exception e) {
            log.error(e.toString());
            return Result.error("更新失败！");
        }
    }
    
    @Override
    public Result<Integer> deleteNote(Integer noteId) {
        try {
            boolean deleteSuccess = this.removeById(noteId);
            if (!deleteSuccess) {
                return Result.error("删除失败！");
            }
            return Result.success(noteId);
        } catch (Exception e) {
            log.error(e.toString());
            return Result.error("删除失败！");
        }
    }
    
    @Override
    public Result<List<Note>> getUserAllNote(Integer userId) {
        try {
            LambdaQueryWrapper<Note> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Note::getUserId, userId);
            List<Note> userNotes = this.list(queryWrapper);
            return Result.success(userNotes);
        } catch (Exception e) {
            log.error(e.toString());
            return Result.error("获取失败！");
        }
    }
    
    @Override
    public Result<List<Note>> searchNote(String text) {
        try {
            LambdaQueryWrapper<Note> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.like(Note::getTitle, text)
                    .or().like(Note::getContent, text)
                    .or().like(Note::getKeywords, text);
            List<Note> notes = this.list(queryWrapper);
            return Result.success(notes);
        } catch (Exception e) {
            log.error(e.toString());
            return Result.error("搜索失败！");
        }
    }
    
    @Override
    public Result<List<Note>> getAllNote() {
        LambdaQueryWrapper<Note> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Note::getId,Note::getTitle,Note::getUserId);
        List<Note> notes = this.list(queryWrapper);
        return Result.success(notes);
    }
}

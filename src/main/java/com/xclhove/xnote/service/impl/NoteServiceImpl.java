package com.xclhove.xnote.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xclhove.xnote.entity.dto.NotePageDTO;
import com.xclhove.xnote.entity.table.Note;
import com.xclhove.xnote.enums.entityattribute.NoteIsPublic;
import com.xclhove.xnote.exception.NoteAccessCodeException;
import com.xclhove.xnote.exception.NoteServiceException;
import com.xclhove.xnote.mapper.NoteMapper;
import com.xclhove.xnote.service.NoteService;
import com.xclhove.xnote.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author xclhove
 */
@Service
@Slf4j
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note> implements NoteService {
    @Value("${xnote.page-request.max-page-size: 200}")
    private Integer maxPageSize;
    
    @Override
    public Note getNoteById(Integer userId, Integer noteId, String accessCode) {
        Note note = this.getById(noteId);
        if (note == null) {
            throw new NoteServiceException("没有该笔记！");
        }
        
        // 用户自己的笔记无需访问码
        if (note.getUserId().equals(userId)) {
            return note;
        }
        
        // 笔记是公开的可以访问
        if (note.getIsPublic() == NoteIsPublic.YES) {
            return note;
        }
        
        // 笔记是非公开的且无访问码，说明该笔记是不让其他人访问
        if (StrUtil.isBlank(note.getAccessCode())) {
            throw new NoteServiceException("无权访问该笔记！");
        }
        
        // 笔记是非公开的但有访问码，检验访问码
        if (!note.getAccessCode().equals(accessCode)) {
            throw new NoteAccessCodeException();
        }
        
        return note;
    }
    
    @Override
    public Note addNote(Note note) {
        LambdaQueryWrapper<Note> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Note::getUserId, note.getUserId())
                .eq(Note::getTitle, note.getTitle());
        Note existNote = this.getOne(queryWrapper);
        if (existNote != null) throw new NoteServiceException("该标题的笔记已存在！");
        
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
            log.error(ExceptionUtil.getMessage(e));
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
            deleteSuccess = this.remove(queryWrapper);
        } catch (Exception e) {
            log.error(e.toString());
            throw new NoteServiceException("出现异常，删除失败！");
        }
        if (!deleteSuccess) throw new NoteServiceException("删除失败！");
        return true;
    }
    
    @Override
    public NotePageDTO pageNote(NotePageDTO notePageDTO, Integer requestUserId, boolean isGetSelf) {
        try {
            Page<Note> page = new Page<>(notePageDTO.getCurrent(), Math.min(notePageDTO.getSize(), maxPageSize));
            
            LambdaQueryWrapper<Note> queryWrapper = new LambdaQueryWrapper<Note>()
                    .select(Note::getId, Note::getTitle, Note::getIsPublic)
                    .eq(notePageDTO.getUserId() != null, Note::getUserId, notePageDTO.getUserId())
                    .like(StrUtil.isNotBlank(notePageDTO.getSearchTitle()), Note::getTitle, notePageDTO.getSearchTitle())
                    .like(StrUtil.isNotBlank(notePageDTO.getSearchContent()), Note::getContent, notePageDTO.getSearchContent())
                    .like(StrUtil.isNotBlank(notePageDTO.getSearchKeyword()), Note::getKeywords, notePageDTO.getSearchKeyword())
                    .and(!isGetSelf, qw -> qw
                            .eq(Note::getIsPublic, NoteIsPublic.YES)
                            .or(q -> q.ne(Note::getAccessCode, ""))
                            .or(requestUserId != null, q -> q.eq(Note::getUserId, requestUserId))
                    );
            
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
    
    @Override
    public NotePageDTO pageNote(NotePageDTO notePageDTO, Integer requestUserId) {
        return pageNote(notePageDTO, requestUserId, false);
    }
}

package com.xclhove.xnote.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xclhove.xnote.interceptor.annotations.UserTokenIntercept;
import com.xclhove.xnote.resolver.annotations.UserInfoFormToken;
import com.xclhove.xnote.exception.ParameterValidateException;
import com.xclhove.xnote.interceptor.UserTokenInterceptor;
import com.xclhove.xnote.pojo.form.note.NoteAddForm;
import com.xclhove.xnote.pojo.form.note.NoteUpdateForm;
import com.xclhove.xnote.pojo.form.note.NoteUpdateTypeForm;
import com.xclhove.xnote.pojo.table.Note;
import com.xclhove.xnote.pojo.table.User;
import com.xclhove.xnote.pojo.vo.PageVO;
import com.xclhove.xnote.pojo.vo.SearchNoteVO;
import com.xclhove.xnote.service.NoteService;
import com.xclhove.xnote.tool.Result;
import com.xclhove.xnote.tool.ThreadLocalTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import java.util.List;

/**
 * 笔记相关接口
 *
 * @author xclhove
 */
@RestController
@RequestMapping("/note")
@RequiredArgsConstructor
@Slf4j
@Validated
public class NoteController {
    private final NoteService noteService;
    
    private void checkSearchTextLength(String searchText) throws ParameterValidateException {
        if (searchText != null && searchText.length() > 500) {
            throw new ParameterValidateException("搜索内容过长");
        }
    }
    
    /**
     * 增加笔记
     */
    @PostMapping
    @UserTokenIntercept
    public Result<Integer> addNote(
            @RequestBody @Validated NoteAddForm noteAddForm,
            @UserInfoFormToken User user
    ) {
        int noteId = noteService.add(user, noteAddForm);
        return Result.success(noteId);
    }
    
    /**
     * 批量删除笔记
     */
    @DeleteMapping
    @UserTokenIntercept
    public Result<?> deleteNoteBatchByIds(
            @RequestParam List<Integer> noteIds,
            @UserInfoFormToken User user
    ) {
        noteService.deleteBatchByIds(user, noteIds);
        return Result.success();
    }
    
    /**
     * 更新笔记
     */
    @PutMapping
    @UserTokenIntercept
    public Result<?> updateNote(
            @RequestBody @Validated NoteUpdateForm noteUpdateForm,
            @UserInfoFormToken User user
    ) {
        noteService.update(user, noteUpdateForm);
        return Result.success();
    }
    
    /**
     * 批量更新笔记类型
     */
    @PutMapping("types")
    @UserTokenIntercept
    public Result<?> updateBatchType(
            @RequestBody @Validated NoteUpdateTypeForm noteUpdateTypeForm,
            @UserInfoFormToken User user
    ) {
        noteService.updateBatchUserNoteType(user, noteUpdateTypeForm);
        return Result.success();
    }
    
    /**
     * 查看笔记
     */
    @GetMapping("{noteId}")
    public Result<Note> previewNote(
            @PathVariable Integer noteId,
            @RequestParam(required = false) String shareCode,
            @UserInfoFormToken User user
    ) {
        Note note = noteService.previewNote(user, noteId, shareCode);
        return Result.success(note);
    }
    
    /**
     * 搜索笔记
     */
    @GetMapping("search")
    public Result<PageVO<Note>> searchNote(
            @RequestParam(required = false, defaultValue = "1") Integer current,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false, defaultValue = "") String heightLightPreTag,
            @RequestParam(required = false, defaultValue = "") String heightLightPostTag,
            @UserInfoFormToken User user
    ) {
        checkSearchTextLength(search);
        PageVO<Note> pageVO = noteService.search(new Page<>(current, size), user, search, heightLightPreTag, heightLightPostTag);
        return Result.success(pageVO);
    }
    
    /**
     * 搜索用户笔记
     */
    @GetMapping("search/{userId}")
    public Result<PageVO<SearchNoteVO>> searchUserNote(
            @PathVariable Integer userId,
            @RequestParam(required = false, defaultValue = "1") Integer current,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false) Integer typeId,
            @UserInfoFormToken User user
    ) {
        checkSearchTextLength(search);
        Page<Note> pageResult = noteService.searchUserNote(new Page<>(current, size), user, userId, typeId, search);
        PageVO<SearchNoteVO> pageVO = BeanUtil.copyProperties(pageResult, PageVO.class);
        return Result.success(pageVO);
    }
}

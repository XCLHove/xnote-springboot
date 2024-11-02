package com.xclhove.xnote.controller;


import com.xclhove.xnote.interceptor.UserTokenInterceptor;
import com.xclhove.xnote.interceptor.annotations.UserTokenIntercept;
import com.xclhove.xnote.pojo.form.noteType.NoteTypeAddForm;
import com.xclhove.xnote.pojo.form.noteType.NoteTypeUpdateForm;
import com.xclhove.xnote.pojo.table.NoteType;
import com.xclhove.xnote.pojo.table.User;
import com.xclhove.xnote.resolver.annotations.UserInfoFormToken;
import com.xclhove.xnote.service.NoteTypeService;
import com.xclhove.xnote.tool.Result;
import com.xclhove.xnote.tool.ThreadLocalTool;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 笔记分类相关接口
 *
 * @author xclhove
 */
@RestController
@RequestMapping("/note-type")
@RequiredArgsConstructor
public class NoteTypeController {
    private final NoteTypeService noteTypeService;
    
    /**
     * 添加笔记分类
     */
    @PostMapping
    @UserTokenIntercept
    public Result<?> addNoteType(
            @RequestBody @Validated NoteTypeAddForm noteTypeAddForm,
            @UserInfoFormToken User user
    ) {
        noteTypeService.addNoteType(user, noteTypeAddForm);
        return Result.success();
    }
    
    /**
     * 获取用户所有笔记分类
     */
    @GetMapping("/user/{userId}")
    public Result<List<NoteType>> getUserAllNoteType(@PathVariable Integer userId) {
        List<NoteType> list = noteTypeService.getUserAllNoteTypeWithRedis(userId);
        return Result.success(list);
    }
    
    /**
     * 删除笔记分类
     */
    @DeleteMapping
    @UserTokenIntercept
    public Result<?> deleteNoteType(
            @RequestParam List<Integer> ids,
            @UserInfoFormToken User user
    ) {
        noteTypeService.deleteNoteTypeByIds(user, ids);
        return Result.success();
    }
    
    /**
     * 修改笔记分类
     */
    @PutMapping
    @UserTokenIntercept
    public Result<?> updateNoteType(
            @RequestBody @Validated NoteTypeUpdateForm noteTypeUpdateForm,
            @UserInfoFormToken User user
    ) {
        noteTypeService.updateNoteType(user, noteTypeUpdateForm);
        return Result.success();
    }
}

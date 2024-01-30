package com.xclhove.xnote.controller;

import com.xclhove.xnote.Interceptor.UserJwtInterceptor;
import com.xclhove.xnote.Interceptor.validator.UserJwtValidator;
import com.xclhove.xnote.constant.TreadLocalKey;
import com.xclhove.xnote.entity.dto.NotePageDTO;
import com.xclhove.xnote.entity.table.Note;
import com.xclhove.xnote.service.NoteService;
import com.xclhove.xnote.util.Result;
import com.xclhove.xnote.util.ThreadLocalUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;

/**
 * 笔记相关接口
 *
 * @author xclhove
 */
@RestController
@RequestMapping("/notes")
@Api(tags = "笔记相关接口")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;
    
    @PostMapping("/page")
    @ApiOperation(value = "分页获取笔记（不包含内容）")
    @UserJwtValidator.UserJwtValidate
    public Result<NotePageDTO> pageAllNote(@RequestBody NotePageDTO notePageDTO) {
        Integer userId = (Integer) ThreadLocalUtil.get(TreadLocalKey.ID);
        notePageDTO = noteService.pageNote(notePageDTO, userId);
        return Result.success(notePageDTO);
    }
    
    @PostMapping("/page/me")
    @ApiOperation(value = "分页获取自己的笔记（不包含内容）")
    @UserJwtInterceptor.UserJwtIntercept
    public Result<NotePageDTO> pageUserNote(@RequestBody NotePageDTO notePageDTO) {
        Integer userId = (Integer) ThreadLocalUtil.get(TreadLocalKey.ID);
        notePageDTO.setUserId(userId);
        notePageDTO = noteService.pageNote(notePageDTO, userId, true);
        return Result.success(notePageDTO);
    }
    
    @GetMapping("/{noteId}")
    @ApiOperation(value = "获取一篇笔记")
    @UserJwtValidator.UserJwtValidate
    public Result<Note> getOneNote(
            @PathVariable
            @ApiParam(value = "笔记id", example = "1")
            @Pattern(regexp = "^\\d+$", message = "笔记id只能是数字！")
            Integer noteId,
            
            @RequestParam(required = false)
            @ApiParam(value = "访问码", example = "123456")
            @Pattern(regexp = "^[a-zA-z1-9]*$", message = "访问码只支持数字和字母！")
            String accessCode) {
        Integer userId = (Integer) ThreadLocalUtil.get(TreadLocalKey.ID);
        Note note = noteService.getNoteById(userId, noteId, accessCode);
        return Result.success(note);
    }
    
    @PutMapping
    @UserJwtInterceptor.UserJwtIntercept
    @ApiOperation(value = "添加笔记")
    public Result<Note> addNote(@RequestBody @ApiParam(value = "笔记信息") Note note) {
        Integer userId = (Integer) ThreadLocalUtil.get("id");
        note.setUserId(userId);
        note = noteService.addNote(note);
        return Result.success(note);
    }
    
    @DeleteMapping("/{noteId}")
    @UserJwtInterceptor.UserJwtIntercept
    @ApiOperation(value = "删除笔记")
    public Result<Object> deleteNote(@PathVariable @ApiParam(value = "笔记id", example = "1") Integer noteId) {
        Integer userId = (Integer) ThreadLocalUtil.get("id");
        noteService.deleteNote(userId, noteId);
        return Result.success();
    }
    
    @PostMapping
    @UserJwtInterceptor.UserJwtIntercept
    @ApiOperation(value = "更新笔记")
    public Result<Note> updateNote(@RequestBody @ApiParam(value = "笔记信息") Note note) {
        Integer userId = (Integer) ThreadLocalUtil.get("id");
        note.setUserId(userId);
        note = noteService.updateNote(note);
        return Result.success(note);
    }
}

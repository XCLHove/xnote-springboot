package com.xclhove.xnote.controller;

import com.xclhove.xnote.annotations.UserJwtIntercept;
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
    @ApiOperation(value = "分页获取所有笔记（不包括内容）")
    public Result<NotePageDTO> pageAllNote(@RequestBody NotePageDTO notePageDTO) {
        notePageDTO = noteService.pageNote(notePageDTO);
        return Result.success(notePageDTO);
    }
    
    @GetMapping("/{noteId}")
    @ApiOperation(value = "获取一篇笔记")
    public Result<Note> getOneNote(@PathVariable @ApiParam(value = "笔记id", example = "1") Integer noteId) {
        Note note = noteService.getNoteById(noteId);
        return Result.success(note);
    }
    
    @PutMapping
    @UserJwtIntercept
    @ApiOperation(value = "添加笔记")
    public Result<Note> addNote(@RequestBody @ApiParam(value = "笔记信息") Note note) {
        Integer userId = (Integer) ThreadLocalUtil.get("id");
        note.setUserId(userId);
        note = noteService.addNote(note);
        return Result.success(note);
    }
    
    @DeleteMapping("/{noteId}")
    @UserJwtIntercept
    @ApiOperation(value = "删除笔记")
    public Result<Object> deleteNote(@PathVariable @ApiParam(value = "笔记id", example = "1") Integer noteId) {
        noteService.deleteNote(noteId);
        return Result.success();
    }
    
    @PostMapping
    @UserJwtIntercept
    @ApiOperation(value = "更新笔记")
    public Result<Note> updateNote(@RequestBody @ApiParam(value = "笔记信息") Note note) {
        Integer userId = (Integer) ThreadLocalUtil.get("id");
        note.setUserId(userId);
        note = noteService.updateNote(note);
        return Result.success(note);
    }
}

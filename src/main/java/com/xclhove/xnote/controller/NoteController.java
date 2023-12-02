package com.xclhove.xnote.controller;

import com.xclhove.xnote.annotations.UserJwtIntercept;
import com.xclhove.xnote.entity.table.Note;
import com.xclhove.xnote.service.NoteService;
import com.xclhove.xnote.util.Result;
import com.xclhove.xnote.util.TokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author xclhove
 */
@RestController
@RequestMapping("/notes")
@Api(tags = "笔记相关接口")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;
    
    @GetMapping
    @ApiOperation(value = "获取所有笔记（不包括内容）")
    public Result<List<Note>> getAllNote() {
        List<Note> allNote = noteService.getAllNote();
        return Result.success(allNote);
    }
    
    @GetMapping("/user/{userId}")
    @ApiOperation(value = "获取某个用户的全部笔记（不包括内容）")
    public Result<List<Note>> getUserAllNote(@PathVariable
                                             @ApiParam(value = "用户id", example = "1")
                                             Integer userId) {
        List<Note> userNotes = noteService.getUserAllNote(userId);
        return Result.success(userNotes);
    }
    
    @GetMapping("/user/self")
    @UserJwtIntercept
    @ApiOperation(value = "获取自己的全部笔记（不包括内容）")
    public Result<List<Note>> getSelfNote(HttpServletRequest request) {
        String token = request.getHeader("token");
        int userId = TokenUtil.getId(token);
        List<Note> userAllNote = noteService.getUserAllNote(userId);
        return Result.success(userAllNote);
    }
    
    @GetMapping("/{noteId}")
    @ApiOperation(value = "获取一篇笔记")
    public Result<Note> getOneNote(@PathVariable
                                   @ApiParam(value = "笔记id", example = "1")
                                   Integer noteId) {
        Note note = noteService.getNote(noteId);
        return Result.success(note);
    }
    
    @PutMapping
    @UserJwtIntercept
    @ApiOperation(value = "添加笔记")
    public Result<Object> addNote(HttpServletRequest request,
                                @RequestBody
                                @ApiParam(value = "笔记信息")
                                Note note) {
        String token = request.getHeader("token");
        Integer userId = TokenUtil.getId(token);
        note.setUserId(userId);
        noteService.addNote(note);
        return Result.success();
    }
    
    @DeleteMapping("/{noteId}")
    @UserJwtIntercept
    @ApiOperation(value = "删除笔记")
    public Result<Object> deleteNote(@PathVariable
                                      @ApiParam(value = "笔记id", example = "1")
                                      Integer noteId) {
        noteService.deleteNote(noteId);
        return Result.success();
    }
    
    @PostMapping
    @UserJwtIntercept
    @ApiOperation(value = "更新笔记")
    public Result<Integer> updateNote(HttpServletRequest request,
                                      @RequestBody
                                      @ApiParam(value = "笔记信息")
                                      Note note) {
        String token = request.getHeader("token");
        Integer userId = TokenUtil.getId(token);
        note.setUserId(userId);
        noteService.updateNote(note);
        return Result.success();
    }
    
    @GetMapping("/search/{text}")
    @ApiOperation(value = "搜索笔记")
    public Result<List<Note>> searchNote(@PathVariable
                                         @ApiParam(value = "搜索内容")
                                         String text) {
        List<Note> notes = noteService.searchNote(text);
        return Result.success(notes);
    }
}

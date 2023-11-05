package com.xclhove.xnote.controller;

import com.xclhove.xnote.annotations.UserJwtIntercept;
import com.xclhove.xnote.annotations.UserStatusIntercept;
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
    @ApiOperation(value = "获取所有笔记")
    public Result<List<Note>> getAllNote() {
        return noteService.getAllNote();
    }
    
    @GetMapping("/user/{userId}")
    @ApiOperation(value = "获取某个用户的全部笔记")
    public Result<List<Note>> getUserAllNote(@PathVariable
                                             @ApiParam(value = "用户id", example = "1")
                                             Integer userId) {
        return noteService.getUserAllNote(userId);
    }
    
    @GetMapping("/user/self")
    @UserJwtIntercept
    @UserStatusIntercept
    @ApiOperation(value = "获取自己的全部笔记")
    public Result<List<Note>> getSelfNote(HttpServletRequest request) {
        String token = request.getHeader("token");
        int userId = TokenUtil.getId(token);
        return noteService.getUserAllNote(userId);
    }
    
    @GetMapping("/{noteId}")
    @ApiOperation(value = "获取一篇笔记")
    public Result<Note> getOneNote(@PathVariable
                                   @ApiParam(value = "笔记id", example = "1")
                                   Integer noteId) {
        return noteService.getNote(noteId);
    }
    
    @PutMapping
    @UserJwtIntercept
    @UserStatusIntercept
    @ApiOperation(value = "添加笔记")
    public Result<Integer> addNote(HttpServletRequest request,
                                @RequestBody
                                @ApiParam(value = "笔记信息")
                                Note note) {
        String token = request.getHeader("token");
        Integer userId = TokenUtil.getId(token);
        note.setUserId(userId);
        return noteService.addNote(note);
    }
    
    @DeleteMapping("/{noteId}")
    @UserJwtIntercept
    @UserStatusIntercept
    @ApiOperation(value = "删除笔记")
    public Result<Integer> deleteNote(@PathVariable
                                      @ApiParam(value = "笔记id", example = "1")
                                      Integer noteId) {
        return noteService.deleteNote(noteId);
    }
    
    @PostMapping
    @UserJwtIntercept
    @UserStatusIntercept
    @ApiOperation(value = "更新笔记")
    public Result<Integer> updateNote(HttpServletRequest request,
                                      @RequestBody
                                      @ApiParam(value = "笔记信息")
                                      Note note) {
        String token = request.getHeader("token");
        Integer userId = TokenUtil.getId(token);
        note.setUserId(userId);
        return noteService.updateNote(note);
    }
    
    @GetMapping("/search/{text}")
    @ApiOperation(value = "搜索笔记")
    public Result<List<Note>> searchNote(@PathVariable
                                         @ApiParam(value = "搜索内容")
                                         String text) {
        return noteService.searchNote(text);
    }
}

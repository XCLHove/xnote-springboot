package com.xclhove.xnote.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xclhove.xnote.entity.table.Note;
import com.xclhove.xnote.util.Result;

import java.util.List;

/**
 * @author xclhove
 */
public interface NoteService extends IService<Note> {
    /**
     * 获取笔记
     * @param noteId 笔记id
     * @return 笔记信息
     */
    public Result<Note> getNote(Integer noteId);
    
    /**
     * 添加笔记
     * @param note 笔记信息
     * @return 笔记id
     */
    public Result<Integer> addNote(Note note);
    
    /**
     * 更新笔记
     * @param note 笔记信息
     * @return 笔记信息
     */
    public Result<Integer> updateNote(Note note);
    
    /**
     * 删除笔记
     * @param noteId 笔记id
     * @return 笔记id
     */
    public Result<Integer> deleteNote(Integer noteId);
    
    /**
     * 获取某个用户的全部笔记
     * @param userId 用户id
     * @return 笔记列表（不包含内容）
     */
    public Result<List<Note>> getUserAllNote(Integer userId);
    
    /**
     * 搜索笔记
     * @param text 搜索内容
     * @return 笔记列表（不包含内容）
     */
    public Result<List<Note>> searchNote(String text);
    
    /**
     * 列出所有的笔记
     * @return 笔记列表
     */
    public Result<List<Note>> getAllNote();
}

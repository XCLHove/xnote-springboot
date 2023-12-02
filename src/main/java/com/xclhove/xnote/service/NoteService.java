package com.xclhove.xnote.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xclhove.xnote.entity.table.Note;

import java.util.List;

/**
 * @author xclhove
 */
public interface NoteService extends IService<Note> {
    /**
     * 获取笔记
     * @param noteId 笔记id
     * @return Note对象
     */
    Note getNote(Integer noteId);
    
    /**
     * 添加笔记
     * @param note 笔记信息
     * @return 是否添加成功
     */
    boolean addNote(Note note);
    
    /**
     * 更新笔记
     * @param note 笔记信息
     * @return 是否更新成功
     */
    boolean updateNote(Note note);
    
    /**
     * 删除笔记
     * @param noteId 笔记id
     * @return 是否删除成功
     */
    boolean deleteNote(Integer noteId);
    
    /**
     * 获取某个用户的全部笔记
     * @param userId 用户id
     * @return Note对象列表
     */
    List<Note> getUserAllNote(Integer userId);
    
    /**
     * 搜索笔记
     * @param text 搜索内容
     * @return Note对象列表（不包含内容）
     */
    List<Note> searchNote(String text);
    
    /**
     * 列出所有的笔记
     * @return Note对象列表（不包含内容）
     */
    List<Note> getAllNote();
}

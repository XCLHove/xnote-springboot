package com.xclhove.xnote.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xclhove.xnote.entity.dto.NotePageDTO;
import com.xclhove.xnote.entity.table.Note;

/**
 * @author xclhove
 */
public interface NoteService extends IService<Note> {
    /**
     * 获取笔记
     * @param noteId 笔记id
     * @return Note对象
     */
    Note getNoteById(Integer noteId);
    
    /**
     * 添加笔记
     * @param note 笔记信息
     * @return Note对象（不包含笔记内容）
     */
    Note addNote(Note note);
    
    /**
     * 更新笔记
     * @param note 笔记信息
     * @return Note对象（不包含笔记内容）
     */
    Note updateNote(Note note);
    
    /**
     * 删除笔记
     * @param noteId 笔记id
     * @return 是否删除成功
     */
    boolean deleteNote(Integer noteId);
    
    /**
     * 分页获取所有的笔记
     * @param notePageDTO 分页数据传输对象
     * @return Note对象列表（不包含笔记内容）
     */
    NotePageDTO pageNote(NotePageDTO notePageDTO);
}

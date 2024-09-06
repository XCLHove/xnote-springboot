package com.xclhove.xnote.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xclhove.xnote.pojo.table.Note;
import org.apache.ibatis.annotations.Param;

/**
 * @author xclhove
 */
public interface NoteMapper extends BaseMapper<Note> {
    Page<Note> search(
            @Param("userId") Integer userId,
            @Param("search") String search,
            @Param("page") Page<Note> page,
            @Param(Constants.WRAPPER) Wrapper<Note> queryWrapper
    );
}

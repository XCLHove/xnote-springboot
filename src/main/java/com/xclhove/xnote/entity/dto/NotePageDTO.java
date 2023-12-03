package com.xclhove.xnote.entity.dto;

import com.xclhove.xnote.entity.table.Note;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Note分页数据传输对象
 *
 * @author xclhove
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Note分页数据传输对象")
public class NotePageDTO extends PageDTO<Note> {
    
    @ApiParam(value = "搜索标题")
    private String searchTitle;
    
    
    @ApiParam(value = "搜索内容")
    private String searchContent;
    
    
    @ApiParam(value = "搜索关键词")
    private String searchKeyword;
    
    
    @ApiParam(value = "用户id")
    private Integer userId;
}
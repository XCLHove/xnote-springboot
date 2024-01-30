package com.xclhove.xnote.entity.table;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.xclhove.xnote.enums.entityattribute.NoteIsPublic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

/**
 * 笔记
 *
 * @author xclhove
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "笔记")
@TableName(autoResultMap = true)
public class Note {
    
    @TableId
    @ApiModelProperty(value = "笔记id", example = "1")
    private Integer id;
    
    @ApiModelProperty(value = "笔记标题", example = "标题")
    private String title;
    
    @ApiModelProperty(value = "笔记内容", example = "笔记内容")
    private String content;
    
    @ApiModelProperty(value = "关键词")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> keywords;
    
    @ApiModelProperty(value = "笔记所属用户id", example = "1")
    private Integer userId;
    
    @ApiModelProperty(value = "笔记发布时间", example = "2020-01-01 00:00:00")
    private Timestamp releaseTime;
    
    @ApiModelProperty(value = "笔记修改时间", example = "2020-01-01 00:00:00")
    private Timestamp updateTime;
    
    @ApiModelProperty(value = "笔记是否公开，未公开时需要访问码才能查看")
    private NoteIsPublic  isPublic;
    
    @ApiModelProperty(value = "笔记访问码" , example = "123456")
    private String accessCode;
}
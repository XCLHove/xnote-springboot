package com.xclhove.xnote.entity.table;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xclhove
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "笔记信息")
public class Note {
    @TableId
    @ApiModelProperty(value = "笔记id", example = "1")
    private Integer id;
    @ApiModelProperty(value = "笔记标题", example = "标题")
    private String title;
    @ApiModelProperty(value = "笔记内容", example = "笔记内容")
    private String content;
    @ApiModelProperty(value = "关键词", example = "[\"keywords1\", \"keywords2\"]")
    private String keywords;
    @ApiModelProperty(value = "笔记所属用户id", example = "1")
    private Integer userId;
}
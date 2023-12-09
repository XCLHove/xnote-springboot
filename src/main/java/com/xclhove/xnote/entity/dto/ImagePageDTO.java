package com.xclhove.xnote.entity.dto;

import com.xclhove.xnote.entity.table.Image;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Image分页数据传输对象
 * @author xclhove
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImagePageDTO extends PageDTO<Image>{
    @ApiParam(value = "搜索图片别名")
    private String searchAlias;
    
    
    @ApiParam(value = "用户id")
    private Integer userId;
}
package com.xclhove.xnote.entity.dto;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页数据传输对象
 *
 * @author xclhove
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO<T> {
    
    @ApiParam(value = "页码")
    private Integer current;
    
    @ApiParam(value = "页大小")
    private Integer size;
    
    @ApiParam(value = "总数")
    private Integer total;
    
    @ApiParam(value = "数据列表")
    private List<T> list;
}
package com.xclhove.xnote.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xclhove.xnote.entity.table.Image;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 图片表 Mapper 接口
 * </p>
 *
 * @author xclhove
 * @since 2023-12-09
 */
@Mapper
public interface ImageMapper extends BaseMapper<Image> {

}

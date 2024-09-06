package com.xclhove.xnote.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xclhove.xnote.pojo.table.Image;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xclhove
 */
public interface ImageMapper extends BaseMapper<Image> {
    boolean incrementImageOwnerCount(@Param("userImageIds") List<Integer> userImageIds, @Param("incrementStep") Integer incrementStep);
}

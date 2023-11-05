package com.xclhove.xnote.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xclhove.xnote.entity.table.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xclhove
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}

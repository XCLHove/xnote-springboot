package com.xclhove.xnote.entity.table;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 管理员
 *
 * @author xclhove
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "管理员")
public class Admin {
    
    @TableId
    @ApiParam(value = "管理员id")
    private Integer id;
    
    @ApiParam(value = "管理员名")
    private String name;
    
    @ApiParam(value = "管理员账号")
    private String account;
    
    @JsonIgnore
    @ApiParam(value = "管理员密码")
    private String password;
    
    @ApiParam(value = "管理员邮箱")
    private String email;
}

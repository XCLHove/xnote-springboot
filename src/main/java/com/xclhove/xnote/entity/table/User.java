package com.xclhove.xnote.entity.table;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xclhove.xnote.enums.entityattribute.UserStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户
 *
 * @author xclhove
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户")
public class User {
    
    @TableId
    @ApiModelProperty(value = "用户id", example = "1")
    private Integer id;
    
    @ApiModelProperty(value = "用户名", example = "user123")
    private String name;
    
    @ApiModelProperty(value = "账号", example = "user123")
    private String account;
    
    @JsonIgnore
    @ApiModelProperty(value = "密码", example = "123456")
    private String password;
    
    @ApiModelProperty(value = "邮箱", example = "123@example.com")
    private String email;
    
    @ApiModelProperty(value = "用户状态", example = "正常")
    private UserStatus status;
}
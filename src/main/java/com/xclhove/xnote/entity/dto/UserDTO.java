package com.xclhove.xnote.entity.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.xclhove.xnote.enums.entityattribute.UserStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

/**
 * @author xclhove
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "User数据传输对象")
public class UserDTO {
    
    @TableId
    @ApiModelProperty(value = "用户id", example = "1")
    private Integer id;
    
    @ApiModelProperty(value = "用户名", example = "user123")
    private String name;
    
    @ApiModelProperty(value = "账号", example = "user123")
    private String account;
    
    @ApiModelProperty(value = "密码", example = "123456")
    private String password;
    
    @ApiModelProperty(value = "邮箱", example = "123@example.com")
    @Email(message = "邮箱格式不正确！")
    private String email;
    
    @ApiModelProperty(value = "用户状态", example = "正常")
    private UserStatus status;
    
    @ApiModelProperty(value = "验证码")
    private String verificationCode;
}

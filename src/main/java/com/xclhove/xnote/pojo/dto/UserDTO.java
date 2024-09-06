package com.xclhove.xnote.pojo.dto;

import com.xclhove.xnote.pojo.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.io.Serializable;

/**
 * @author xclhove
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Positive
    private Integer id;
    @Pattern(regexp = "^\\S{2,30}$", message = "用户名只能为2-30位的非空字符")
    private String name;
    @Pattern(regexp = "^[a-zA-Z0-9]{2,30}$", message = "账号只能为2-30位的字母和数字")
    private String account;
    @Pattern(regexp = "^[a-zA-Z\\d._*]{6,30}$", message = "密码只能为6-30位的数字、字母、下划线、点和星号")
    private String password;
    @Email(message = "email格式不正确")
    private String email;
    private UserStatus status;
    @Positive
    private Integer homePageNoteId;
    @Positive
    private Long imageStorageSize;
}

package com.xclhove.xnote.pojo.form.user;

import com.xclhove.xnote.pojo.form.rule.UserFormRule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author xclhove
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateEmailForm implements Serializable {
    @NotBlank(message = "邮箱不能为空")
    @Pattern(regexp = UserFormRule.Email.REGEXP, message = UserFormRule.Email.MESSAGE)
    private String email;
    @NotBlank(message = "验证码不能为空")
    private String verificationCode;
}

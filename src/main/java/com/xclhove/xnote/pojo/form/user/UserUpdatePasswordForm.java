package com.xclhove.xnote.pojo.form.user;

import com.xclhove.xnote.pojo.form.rule.UserFormRule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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
public class UserUpdatePasswordForm implements Serializable {
    @Pattern(regexp = UserFormRule.Password.REGEXP, message = UserFormRule.Password.MESSAGE)
    private String password;
}

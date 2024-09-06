package com.xclhove.xnote.pojo.form.user;

import com.xclhove.xnote.pojo.form.rule.UserFormRule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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
public class UserUpdateForm implements Serializable {
    @Pattern(regexp = UserFormRule.Name.REGEXP, message = UserFormRule.Name.MESSAGE)
    private String name;
    @Positive(message = "homePageNoteId必须为正整数")
    private Integer homePageNoteId;
}

package com.xclhove.xnote.pojo.form.noteType;

import com.xclhove.xnote.pojo.form.rule.NoteTypeFormRule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class NoteTypeUpdateForm implements Serializable {
    @NotNull(message = "类型ID不能为空")
    @Positive(message = "类型ID必须为正整数")
    private Integer id;
    @NotBlank(message = "类型名称不能为空")
    @Pattern(regexp = NoteTypeFormRule.Name.REGEXP, message = NoteTypeFormRule.Name.MESSAGE)
    private String name;
}

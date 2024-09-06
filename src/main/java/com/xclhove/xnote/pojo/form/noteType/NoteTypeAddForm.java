package com.xclhove.xnote.pojo.form.noteType;

import com.xclhove.xnote.pojo.form.rule.NoteTypeFormRule;
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
public class NoteTypeAddForm implements Serializable {
    @NotBlank(message = "类型名称不能为空")
    @Pattern(regexp = NoteTypeFormRule.Name.REGEXP, message = NoteTypeFormRule.Name.MESSAGE)
    private String name;
}

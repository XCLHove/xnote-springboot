package com.xclhove.xnote.pojo.form.note;

import com.xclhove.xnote.pojo.enums.NoteIsPublic;
import com.xclhove.xnote.pojo.form.rule.NoteFormRule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author xclhove
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class NoteUpdateForm implements Serializable {
    @NotNull(message = "笔记ID不能为空")
    @Positive(message = "笔记ID必须为正整数")
    private Integer id;
    @Size(min = NoteFormRule.Title.MIN, max = NoteFormRule.Title.MAX, message = NoteFormRule.Title.MESSAGE)
    private String title;
    private String content;
    private NoteIsPublic isPublic;
    private Integer typeId;
}
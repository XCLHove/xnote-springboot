package com.xclhove.xnote.pojo.form.note;

import com.xclhove.xnote.pojo.enums.NoteIsPublic;
import com.xclhove.xnote.pojo.form.rule.NoteFormRule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
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
public class NoteAddForm implements Serializable {
    @NotBlank(message = "标题不能为空")
    @Size(min = NoteFormRule.Title.MIN, max = NoteFormRule.Title.MAX, message = NoteFormRule.Title.MESSAGE)
    private String title;
    @NotBlank(message = "内容不能为空")
    private String content;
    @NotNull(message = "请选择是否公开笔记")
    private NoteIsPublic isPublic;
    @Positive(message = "笔记类型ID必须为正整数")
    private Integer typeId;
}
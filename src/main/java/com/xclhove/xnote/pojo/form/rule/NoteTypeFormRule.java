package com.xclhove.xnote.pojo.form.rule;

/**
 * @author xclhove
 */
public interface NoteTypeFormRule {
    interface Name {
        String REGEXP = "^\\S{1,10}$";
        String MESSAGE = "类型名称只能为1-10位的非空字符";
    }
}

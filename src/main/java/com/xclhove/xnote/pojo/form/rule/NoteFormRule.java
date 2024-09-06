package com.xclhove.xnote.pojo.form.rule;

/**
 * @author xclhove
 */
public interface NoteFormRule {
    interface Title {
        int MIN = 1;
        int MAX = 200;
        String MESSAGE = "标题长度必须在" + MIN + "-" + MAX + "个字符之间";
    }
}

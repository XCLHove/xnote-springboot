package com.xclhove.xnote.pojo.form.rule;

/**
 * @author xclhove
 */
public interface UserFormRule {
    interface Name {
        String REGEXP = "^\\S{2,30}$";
        String MESSAGE = "用户名只能为2-30位的非空字符";
    }
    interface Account {
        String REGEXP = "^[a-zA-Z0-9]{2,30}$";
        String MESSAGE = "账号只能为2-30位的字母和数字";
    }
    interface Password {
        String REGEXP = "^[a-zA-Z\\d._*]{6,30}$";
        String MESSAGE = "密码只能为6-30位的数字、字母、下划线、点和星号";
    }
    interface Email {
        String REGEXP = "^[\\w-]+@((\\w+)\\.)+([a-zA-Z]{2,4})$";
        String MESSAGE = "邮箱格式不正确";
    }
}
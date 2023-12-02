package com.xclhove.xnote.enums.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xclhove
 */

@Getter
@AllArgsConstructor
public enum ResultType {
    SUCCESS(200, "操作成功！"),
    ERROR(500, "操作失败！"),
    USER_SERVICE_EXCEPTION(600, "用户业务异常!"),
    USER_TOKEN_EXCEPTION(601, "用户token异常！"),
    NOTE_SERVICE_EXCEPTION(700, "笔记业务异常!"),
    ADMIN_SERVICE_EXCEPTION(800, "管理员业务异常！"),
    ADMIN_TOKEN_EXCEPTION(801, "管理员token异常！"),
    ;
    private final int status;
    private final String message;
}

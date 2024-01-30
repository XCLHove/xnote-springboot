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
    NOTE_ACCESS_CODE_EXCEPTION(211, "访问码不正确！"),
    NOT_FOUND_EXCEPTION(404, "请求路径不存在！"),
    ERROR(500, "操作失败！"),
    PARAMETER_VALIDATE_EXCEPTION(550, "参数校验异常！"),
    IP_FREQUENCY_EXCEPTION(551, "IP访问频率异常！"),
    VERIFICATION_CODE_EXCEPTION(552, "验证码错误！"),
    OPERATION_FREQUENCY_EXCEPTION(552, "操作频率异常！"),
    USER_SERVICE_EXCEPTION(600, "用户业务异常!"),
    USER_TOKEN_EXCEPTION(601, "用户token异常！"),
    NOTE_SERVICE_EXCEPTION(700, "笔记业务异常!"),
    ADMIN_SERVICE_EXCEPTION(800, "管理员业务异常！"),
    ADMIN_TOKEN_EXCEPTION(801, "管理员token异常！"),
    IMAGE_SERVICE_EXCEPTION(900, "图片业务异常！"),
    ;
    private final int status;
    private final String message;
    
    public static ResultType getResultType(int status) {
        for (ResultType type : ResultType.values()) {
            if (type.status == status) {
                return type;
            }
        }
        return null;
    }
}

package com.xclhove.xnote.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xclhove
 */
@Getter
@AllArgsConstructor
public enum ResultType {
    SUCCESS(200, "操作成功！"),
    NOT_FOUND(404, "请求路径不存在！"),
    FAIL(500, "操作失败！"),
    SYSTEM_EXCEPTION(550, "系统异常！"),
    IP_FREQUENCY_EXCEPTION(551, "IP访问频率异常！"),
    DEVICE_FREQUENCY_EXCEPTION(552, "设备访问频率异常！"),
    PARAMETER_VALIDATE_EXCEPTION(553, "参数格式错误！"),
    USER_SERVICE_EXCEPTION(600, "用户业务异常！"),
    USER_TOKEN_EXCEPTION(601, "用户身份校验失败！"),
    IMAGE_SERVICE_EXCEPTION(700, "图片业务异常！"),
    NOTE_SERVICE_EXCEPTION(800, "笔记业务异常！"),
    VERIFICATION_CODE_EXCEPTION(900, "验证码业务异常！"),
    NOTE_TYPE_SERVICE_EXCEPTION(1000, "笔记类型业务异常！"),
    SHARE_NOTE_RECORD_SERVICE_EXCEPTION(1100, "笔记分享记录业务异常！"),
    USER_IMAGE_SERVICE_EXCEPTION(1200, "用户图片业务异常！"),
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

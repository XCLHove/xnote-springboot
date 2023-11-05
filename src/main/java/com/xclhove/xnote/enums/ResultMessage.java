package com.xclhove.xnote.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xclhove
 */

@Getter
@AllArgsConstructor
public enum ResultMessage {
    SUCCESS("success!"),
    ERROR("error!"),
    INFO("info!"),
    WARNING("warning!"),
    USER_STATUS_EXCEPTION("用户状态异常！"),
    USER_BANNED("用户已被禁封！"),
    USER_LOGIN_EXCEPTION("用户登录异常!"),
    ADMIN_LOGIN_EXCEPTION("管理员登录异常！"),
    TOKEN_EXCEPTION("token异常！"),
    ;
    private final String message;
}

package com.xclhove.xnote.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xclhove
 */

@Getter
@AllArgsConstructor
public enum ResultStatus {
    SUCCESS(200),
    ERROR(500),
    LOGIN_EXCEPTION(601),
    TOKEN_EXCEPTION(602),
    USER_STATUS_EXCEPTION(603),
    ADMIN_LOGIN_EXCEPTION(604),
    ;
    @JsonValue
    private final int status;
}

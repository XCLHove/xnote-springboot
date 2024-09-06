package com.xclhove.xnote.pojo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态
 *
 * @author xclhove
 */
@Getter
@AllArgsConstructor
public enum UserStatus {
    DISABLE(0, "禁用"),
    ENABLE(1, "启用"),
    ;
    @EnumValue
    private final int status;
    @JsonValue
    private final String description;
}

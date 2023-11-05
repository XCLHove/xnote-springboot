package com.xclhove.xnote.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xclhove
 */

@Getter
@AllArgsConstructor
public enum UserStatus {
    NORMAL(1, "正常"),
    BANED(2, "已禁封");
    @EnumValue
    private final int status;
    @JsonValue
    private final String description;
}

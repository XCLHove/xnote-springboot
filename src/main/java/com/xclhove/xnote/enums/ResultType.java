package com.xclhove.xnote.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xclhove
 */

@Getter
@AllArgsConstructor
public enum ResultType {
    SUCCESS("success"),
    ERROR("error"),
    INFO("info"),
    WARNING("warning"),
    ;
    @JsonValue
    private final String type;
}
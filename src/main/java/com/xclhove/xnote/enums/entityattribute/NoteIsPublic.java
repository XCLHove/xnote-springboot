package com.xclhove.xnote.enums.entityattribute;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 笔记是否公开
 *
 * @author xclhove
 */

@Getter
@AllArgsConstructor
public enum NoteIsPublic {
    NO(0, "非公开"),
    YES(1, "公开");
    @EnumValue
    private final int value;
    @JsonValue
    private final String description;
}

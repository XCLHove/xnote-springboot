package com.xclhove.xnote.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author xclhove
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObjectList<T> {
    private List<T> value;
}
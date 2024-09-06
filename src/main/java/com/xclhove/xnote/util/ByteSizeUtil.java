package com.xclhove.xnote.util;

import java.util.HashMap;
import java.util.Map;

public class ByteSizeUtil {
    static final Map<String, Short> UNIT_TO_LEVEL;
    static final Map<Short, String> LEVEL_TO_UNIT;
    static final short MIN_LEVEL = 0;
    static {
        short index = MIN_LEVEL;
        UNIT_TO_LEVEL = new HashMap<>();
        UNIT_TO_LEVEL.put("B", index++);
        UNIT_TO_LEVEL.put("KB", index++);
        UNIT_TO_LEVEL.put("MB", index++);
        UNIT_TO_LEVEL.put("GB", index++);
        UNIT_TO_LEVEL.put("TB", index++);
        UNIT_TO_LEVEL.put("PB", index++);
        
        LEVEL_TO_UNIT = new HashMap<>(UNIT_TO_LEVEL.size());
        UNIT_TO_LEVEL.forEach((key, value) -> {
            LEVEL_TO_UNIT.put(value, key);
        });
    }
    
    public static String parseSizeWithUnit(long size) {
        short sizeLevel = MIN_LEVEL;
        float sizeFloat = size;
        while (sizeFloat > 1024) {
            sizeFloat /= 1024f;
            sizeLevel++;
        }
        return String.format("%.2f%s", sizeFloat, LEVEL_TO_UNIT.get(sizeLevel));
    }
}

package com.xclhove.xnote.util;

public class UuidUtil {
    private final static String UUID_REGEX = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";
    private final static String SIMPLE_UUID_REGEX = "^[0-9a-f]{8}[0-9a-f]{4}[0-9a-f]{4}[0-9a-f]{4}[0-9a-f]{12}$";
    private final static int UUID_LENGTH = 36;
    private final static int SIMPLE_UUID_LENGTH = 32;
    public static boolean isUuid(String uuid) {
        if (uuid == null) {
            return false;
        }
        if (uuid.length() != UUID_LENGTH) {
            return false;
        }
        return uuid.matches(UUID_REGEX);
    }
    
    public static boolean isSimpleUuid(String simpleUuid) {
        if (simpleUuid == null) {
            return false;
        }
        if (simpleUuid.length() != SIMPLE_UUID_LENGTH) {
            return false;
        }
        return simpleUuid.matches(SIMPLE_UUID_REGEX);
    }
}

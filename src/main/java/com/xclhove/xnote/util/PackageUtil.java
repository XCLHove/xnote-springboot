package com.xclhove.xnote.util;

/**
 * @author xclhove
 */
public class PackageUtil {
    public static String getBasePackage() {
        String currentPackage = PackageUtil.class.getPackage().getName();
        String parentPackage = currentPackage.substring(0, currentPackage.lastIndexOf("."));
        return parentPackage;
    }
    
    public static String join(String... strings) {
        return String.join(".", strings);
    }
}
package com.xclhove.xnote.util;

/**
 * @author xclhove
 */
public final class VariableNameUtil {
    private VariableNameUtil() {}
    
    public static String toUnderlineName(String camelCaseName) {
        if (camelCaseName == null || camelCaseName.isEmpty()) {
            return camelCaseName;
        }
        
        StringBuilder result = new StringBuilder();
        boolean isFirstCharacter = true;
        
        for (char ch : camelCaseName.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                if (!isFirstCharacter) {
                    result.append('_');
                }
                result.append(Character.toLowerCase(ch));
                isFirstCharacter = false;
            } else {
                result.append(ch);
                isFirstCharacter = false;
            }
        }
        
        return result.toString();
    }
    
}

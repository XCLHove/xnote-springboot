package com.xclhove.xnote.util;

/**
 * 验证码工具类
 *
 * @author xclhove
 */
public class VerificationCodeUtil {
    private final static int NUMBER_ONLY_LENGTH = 10;
    
    /**
     * 生成一个随机验证码
     *
     * @param codeLength 验证码的长度
     * @param isNumberOnly 是否只包含数字
     * @return 生成的验证码
     */
    public static String generate(int codeLength, boolean isNumberOnly) {
        char[] characters = new char[isNumberOnly ? NUMBER_ONLY_LENGTH : 62];
        int index = 0;
        
        for (int i = 0; i < NUMBER_ONLY_LENGTH; i++) {
            characters[index++] = (char) ('0' + i);
        }
        
        if (!isNumberOnly) {
            for (int i = 0; i < 26; i++) {
                characters[index++] = (char) ('a' + i);
            }
            for (int i = 0; i < 26; i++) {
                characters[index++] = (char) ('A' + i);
            }
        }
        
        StringBuilder verificationCode = new StringBuilder(codeLength);
        for (int i = 0; i < codeLength; i++) {
            int randomIndex = ((int) (Math.random() * 100)) % characters.length;
            verificationCode.append(characters[randomIndex]);
        }
        return verificationCode.toString();
    }
    
    /**
     * 生成一个由字母和数字组成的随机验证码
     *
     * @param codeLength 验证码的长度
     * @return 生成的验证码
     */
    public static String generate(int codeLength) {
        return generate(codeLength, false);
    }
}

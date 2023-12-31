package com.xclhove.xnote.util;

import com.xclhove.xnote.constant.RedisKey;

/**
 * 验证码工具类
 *
 * @author xclhove
 */
public class VerificationCodeUtil {
    
    /**
     * 生成一个由字母和数字组成的随机验证码
     *
     * @param codeLength 验证码的长度
     * @return 生成的验证码
     */
    public static String generateVerificationCode(int codeLength) {
        char[] characters = new char[62];
        int index = 0;
        
        for (int i = 0; i <= 9; i++) {
            characters[index++] = (char) ('0' + i);
        }
        for (int i = 0; i < 26; i++) {
            characters[index++] = (char) ('a' + i);
        }
        for (int i = 0; i < 26; i++) {
            characters[index++] = (char) ('A' + i);
        }
        
        StringBuilder verificationCode = new StringBuilder(codeLength);
        for (int i = 0; i < codeLength; i++) {
            verificationCode.append(characters[(int) (Math.random() * 62)]);
        }
        return verificationCode.toString();
    }
    
    public static String generateVerificationCodeKey(String email) {
        return RedisKey.VERIFICATION_CODE_OF + email;
    }
}

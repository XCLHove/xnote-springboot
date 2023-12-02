package com.xclhove.xnote.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具类
 *
 * @author xclhove
 */
public class EncryptUtil {
    @Getter
    @AllArgsConstructor
    public enum EncryptionAlgorithm {
        SHA256("SHA-256"),
        MD5("MD5");
        
        private final String value;
    }
    
    /**
     * 对字符串进行加密
     *
     * @param value     要加密的字符串
     * @param salt      盐值
     * @param algorithm 加密算法
     * @return 加密后的字符串
     */
    public static String encrypt(String value, String salt, EncryptionAlgorithm algorithm) {
        try {
            value += salt;
            MessageDigest md = MessageDigest.getInstance(algorithm.getValue());
            byte[] passwordBytes = value.getBytes();
            byte[] digest = md.digest(passwordBytes);
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
package com.xclhove.xnote.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author xclhove
 */
public final class Md5Util {
    private static MessageDigest messageDigest;
    
    private Md5Util() {
    }
    
    private static MessageDigest getInstance() throws NoSuchAlgorithmException {
        if (messageDigest != null) {
            return messageDigest;
        }
        synchronized (Md5Util.class) {
            if (messageDigest == null) {
                messageDigest = MessageDigest.getInstance("MD5");
            }
            return messageDigest;
        }
    }
    
    public static String getMd5(byte[] bytes) throws Exception {
        MessageDigest messageDigest = getInstance();
        bytes = messageDigest.digest(bytes);
        StringBuilder md5 = new StringBuilder();
        for (byte b : bytes) {
            md5.append(String.format("%02x", b));
        }
        return md5.toString();
    }
}

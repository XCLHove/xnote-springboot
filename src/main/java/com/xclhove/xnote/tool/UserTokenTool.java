package com.xclhove.xnote.tool;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.time.Duration;
import java.util.Date;

/**
 * @author xclhove
 */
public class UserTokenTool {
    
    public static String generate(String encryptPassword, Integer userId) {
        return JWT.create()
                .withAudience(String.valueOf(userId))
                //2小时后token过期
                .withExpiresAt(new Date(System.currentTimeMillis() + Duration.ofHours(2).toMillis()))
                // 以 encryptPassword 作为签名密钥
                .sign(Algorithm.HMAC256(encryptPassword));
    }
    
    public static Integer getUserId(String token) {
        try {
            return Integer.parseInt(JWT.decode(token).getAudience().get(0));
        } catch (Exception exception) {
            return null;
        }
    }
    
    public static boolean valid(String token, String encryptPassword) {
        try {
            JWT.require(Algorithm.HMAC256(encryptPassword)).build().verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

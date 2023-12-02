package com.xclhove.xnote.util;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.List;

/**
 * @author xclhove
 */
public class TokenUtil {
    
    
    /**
     * 生成token
     * @param id Id
     * @param secret 签名密钥
     * @return token
     */
    public static String generate(int id, String secret) {
        return JWT.create().withAudience(String.valueOf(id)) // 将 user id 保存到 token 里面
                .withExpiresAt(DateUtil.offsetHour(new Date(), 24)) //2小时后token过期
                .sign(Algorithm.HMAC256(secret)); // 以 secret 作为签名密钥
    }
    
    /**
     * 从token中提取id
     * @param token token
     * @return 用户id
     */
    public static Integer getId(String token) {
        try {
            List<String> audience = JWT.decode(token).getAudience();
            int userId = Integer.parseInt(audience.get(0));
            return userId;
        } catch (Exception exception) {
            return null;
        }
    }
    
    /**
     * 验证token
     * @param token token
     * @param  secret 签名密钥
     * @return 验证是否通过
     */
    public static boolean validate(String token, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true; // token验证通过
        } catch (Exception e) {
            return false;
        }
    }
}

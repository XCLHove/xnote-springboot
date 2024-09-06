package com.xclhove.xnote.service;

import cn.hutool.core.util.StrUtil;
import com.xclhove.xnote.config.XnoteConfig;
import com.xclhove.xnote.constant.RedisKey;
import com.xclhove.xnote.exception.VerificationCodeException;
import com.xclhove.xnote.tool.EmailTool;
import com.xclhove.xnote.util.VerificationCodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.concurrent.TimeUnit;

/**
 * @author xclhove
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationCodeService {
    private static final int VERIFICATION_CODE_LENGTH = 6;
    private static final int VERIFICATION_CODE_EXPIRE_SECONDS = 60;
    
    private final EmailTool emailTool;
    private final StringRedisTemplate stringRedisTemplate;
    private final XnoteConfig xnoteConfig;
    
    private String getRedisKey(String email) {
        return RedisKey.join(RedisKey.VerificationCode.EMAIL, email);
    }
    
    private String generate() {
        return VerificationCodeUtil.generate(VERIFICATION_CODE_LENGTH, true);
    }
    
    private void saveToRedis(String code, String email) {
        String redisKey = getRedisKey(email);
        stringRedisTemplate.opsForValue().set(redisKey, code, VERIFICATION_CODE_EXPIRE_SECONDS, TimeUnit.SECONDS);
    }
    
    private void deleteInRedis(String email) {
        stringRedisTemplate.delete(getRedisKey(email));
    }
    
    private String getFromRedis(String email) {
        String redisKey = getRedisKey(email);
        return stringRedisTemplate.opsForValue().get(redisKey);
    }
    
    private int getExpireSecond(String email) {
        String redisKey = getRedisKey(email);
        return Math.toIntExact(stringRedisTemplate.getExpire(redisKey, TimeUnit.SECONDS));
    }
    
    public boolean verify(String code, String email) {
        if (!xnoteConfig.debug.getEnableVerifyVerificationCode()) {
            return true;
        }
        
        String codeInRedis = getFromRedis(email);
        boolean valid = code.equalsIgnoreCase(codeInRedis);
        if (valid) {
            deleteInRedis(email);
        }
        return valid;
    }
    
    public int sendToEmail(String email) {
        String codeInRedis = getFromRedis(email);
        if (StrUtil.isNotBlank(codeInRedis)) {
            int expireSecond = getExpireSecond(email);
            
            StringBuilder content = new StringBuilder();
            content.append("您的验证码是：")
                    .append(codeInRedis)
                    .append("，请在")
                    .append(expireSecond)
                    .append("秒内使用。");
            if (xnoteConfig.debug.getEnableMockSendVerificationCode()) {
                log.info(content.toString());
            }
            return expireSecond;
        }
        
        String newCode = generate();
        StringBuilder content = new StringBuilder();
        content.append("您的验证码是：")
                .append(newCode)
                .append("，请在")
                .append(VERIFICATION_CODE_EXPIRE_SECONDS)
                .append("秒内使用。");
        try {
            if (xnoteConfig.debug.getEnableMockSendVerificationCode()) {
                log.info(content.toString());
            } else {
                emailTool.sendMail(email, "XNote验证码", content.toString());
            }
            saveToRedis(newCode, email);
            return VERIFICATION_CODE_EXPIRE_SECONDS;
        } catch (MessagingException e) {
            log.error("发送验证码失败", e);
            throw new VerificationCodeException();
        }
    }
}

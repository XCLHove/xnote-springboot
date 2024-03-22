package com.xclhove.xnote.tool;

import com.xclhove.xnote.constant.RedisKey;
import com.xclhove.xnote.constant.TreadLocalKey;
import com.xclhove.xnote.util.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author xclhove
 */
@Component
@RequiredArgsConstructor
public class TokenTool {
    private final RedisTool redisTool;
    
    public String get(Integer userId) {
        String key = generateTokenKey(userId);
        return redisTool.getValue(key, String.class);
    }
    
    public void set(Integer userId, String token) {
        String key = generateTokenKey(userId);
        redisTool.setValue(key, token, 24, TimeUnit.HOURS);
    }
    
    public void remove(Integer userId) {
        String key = generateTokenKey(userId);
        redisTool.deleteValue(key);
    }
    
    private String generateTokenKey(Integer userId) {
        String deviceId = ThreadLocalUtil.get(TreadLocalKey.DEVICE_ID, String.class);
        String key = RedisKey.USER_TOKEN + ":" + userId + ":" + deviceId;
        return key;
    }
}

package com.xclhove.xnote.tool;

import com.xclhove.xnote.constant.TreadLocalKey;
import com.xclhove.xnote.pojo.table.User;
import com.xclhove.xnote.util.ThreadLocalUtil;

/**
 * @author xclhove
 */
public final class ThreadLocalTool {
    private ThreadLocalTool() {}
    
    /**
     * 从ThreadLocal获取当前用户
     */
    public static User getUser() {
        User user = ThreadLocalUtil.get(TreadLocalKey.USER, User.class);
        return user;
    }
    
    public static void setUser(User user) {
        ThreadLocalUtil.set(TreadLocalKey.USER, user);
    }
    
    /**
     * 从ThreadLocal获取deviceId
     */
    public static String getDeviceId() {
        String deviceId = ThreadLocalUtil.get(TreadLocalKey.DEVICE_ID, String.class);
        return deviceId;
    }
    
    public static void setDeviceId(String deviceId) {
        ThreadLocalUtil.set(TreadLocalKey.DEVICE_ID, deviceId);
    }
    
    public static void setClientIp(String clientIpAddress) {
        ThreadLocalUtil.set(TreadLocalKey.CLIENT_IP, clientIpAddress);
    }
    
    public static String getClientIpAddress() {
        return ThreadLocalUtil.get(TreadLocalKey.CLIENT_IP, String.class);
    }
}

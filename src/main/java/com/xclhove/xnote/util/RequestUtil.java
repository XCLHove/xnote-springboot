package com.xclhove.xnote.util;

import cn.hutool.crypto.SecureUtil;
import com.xclhove.xnote.constant.RequestHeaderKey;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求工具类
 *
 * @author xclhove
 */
public class RequestUtil {
    /**
     * 获取客户端ip地址
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("X-Real-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            throw new RuntimeException("获取IP地址失败");
        }
        return ipAddress;
    }
    
    /**
     * 获取当前路径
     */
    public static String getCurrentPath(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String currentPath = requestUri.substring(contextPath.length());
        return currentPath;
    }
    
    
    
    public static String getDeviceIdFormUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader(RequestHeaderKey.USER_AGENT);
        return SecureUtil.md5(userAgent);
    }
}

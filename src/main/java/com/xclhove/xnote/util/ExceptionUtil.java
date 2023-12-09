package com.xclhove.xnote.util;

/**
 * 异常工具类
 * @author xclhove
 */
public class ExceptionUtil {
    
    /**
     * 获取异常的信息
     * @param e 异常
     * @return 异常信息字符串，如果为null，则返回异常的toString()结果
     */
    public static String getMessage(Exception e) {
        String message = e.getMessage();
        if (message == null) {
            message = e.toString();
        }
		return message;
	}
}
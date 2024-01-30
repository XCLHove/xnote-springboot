package com.xclhove.xnote.util;

import java.util.HashMap;
import java.util.Map;

/**
 * ThreadLocal工具类
 *
 * @author xclhove
 */
public class ThreadLocalUtil {
    private final static ThreadLocal<Map<String, Object>> THREAD_LOCAL = new ThreadLocal<>();
    
    /**
     * 获取数据
     *
     * @param name 数据名
     * @return 数据值
     */
    public static Object get(String name) {
        Map<String, Object> dataMap = THREAD_LOCAL.get();
        return dataMap.get(name);
    }
    
    /**
     * 获取数据
     * @param name 数据名
     * @param resultType 数据值返回类型
     * @return 数据值
     * @param <T>
     */
    public static <T> T get(String name, Class<T> resultType) {
        Map<String, Object> dataMap = THREAD_LOCAL.get();
        return (T) dataMap.get(name);
    }
    
    /**
     * 存入数据
     * @param name 数据名
     * @param value 数据值
     */
    public static void set(String name, Object value) {
        Map<String, Object> dataMap = THREAD_LOCAL.get();
        if (dataMap == null) dataMap = new HashMap<>();
        dataMap.put(name, value);
        THREAD_LOCAL.set(dataMap);
    }
    
    /**
     * 移除ThreadLocal，防止内存泄漏
     */
    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
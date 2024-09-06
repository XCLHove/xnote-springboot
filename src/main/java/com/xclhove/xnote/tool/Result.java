package com.xclhove.xnote.tool;

import com.xclhove.xnote.enums.ResultType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xclhove
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private int status;
    private String message;
    private T data;
    
    public static <T> Result<T> success() {
        return new Result<>(ResultType.SUCCESS.getStatus(), ResultType.SUCCESS.getMessage(), null);
    }
    
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultType.SUCCESS.getStatus(), ResultType.SUCCESS.getMessage(), data);
    }
    
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ResultType.SUCCESS.getStatus(), message, data);
    }
    
    public static <T> Result<T> success(int status, String message, T data) {
        return new Result<>(status, message, data);
    }
    
    public static <T> Result<T> fail() {
        return new Result<>(ResultType.FAIL.getStatus(), ResultType.FAIL.getMessage(), null);
    }
    
    public static <T> Result<T> fail(String message) {
        return new Result<>(ResultType.FAIL.getStatus(), message, null);
    }
    
    public static <T> Result<T> fail(int status, String message) {
        return new Result<>(status, message, null);
    }
    
    public static <T> Result<T> fail(int status, String message, T data) {
        return new Result<>(status, message, data);
    }
}

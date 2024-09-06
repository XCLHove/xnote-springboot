package com.xclhove.xnote.exception;

import com.xclhove.xnote.enums.ResultType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 自定义异常都要继承该类
 *
 * @author xclhove
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractServiceException extends RuntimeException {
    private int status;
    private String message;
    private Object data;
    
    public AbstractServiceException(String message) {
        this(ResultType.FAIL.getStatus(), message, null);
    }
    
    public AbstractServiceException(int status, String message) {
        this(status, message, null);
    }
}

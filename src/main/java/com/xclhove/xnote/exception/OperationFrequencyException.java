package com.xclhove.xnote.exception;

import com.xclhove.xnote.enums.result.ResultType;

/**
 * 操作频率异常
 *
 * @author xclhove
 */
public class OperationFrequencyException extends ServiceException {
    public OperationFrequencyException() {
        this(ResultType.OPERATION_FREQUENCY_EXCEPTION.getMessage());
    }
    
    public OperationFrequencyException(String message) {
        this(message, null);
    }
    
    public OperationFrequencyException(String message, Object data) {
        super(ResultType.OPERATION_FREQUENCY_EXCEPTION.getStatus(), message, data);
    }
}
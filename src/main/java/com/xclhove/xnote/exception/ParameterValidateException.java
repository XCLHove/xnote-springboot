package com.xclhove.xnote.exception;


import com.xclhove.xnote.enums.ResultType;

/**
 * @author xclhove
 */
public class ParameterValidateException extends AbstractServiceException {
    public ParameterValidateException() {
        this(ResultType.PARAMETER_VALIDATE_EXCEPTION.getMessage());
    }
    
    public ParameterValidateException(String message) {
        this(message, null);
    }
    
    public ParameterValidateException(String message, Object data) {
        super(ResultType.PARAMETER_VALIDATE_EXCEPTION.getStatus(), message, data);
    }
}

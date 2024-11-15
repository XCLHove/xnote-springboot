package com.xclhove.xnote.exception;


import com.xclhove.xnote.constant.ParameterName;
import com.xclhove.xnote.enums.ResultType;

/**
 * @author xclhove
 */
public class NeedImageVerificationCodeException extends AbstractServiceException {
    public NeedImageVerificationCodeException() {
        this(ResultType.NEED_IMAGE_VERIFICATION_CODE_EXCEPTION.getMessage());
    }
    
    public NeedImageVerificationCodeException(String message) {
        this(message, ParameterName.IMAGE_VERIFICATION_CODE);
    }
    
    public NeedImageVerificationCodeException(String message, Object data) {
        super(ResultType.NEED_IMAGE_VERIFICATION_CODE_EXCEPTION.getStatus(), message, data);
    }
}

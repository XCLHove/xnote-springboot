package com.xclhove.xnote.exception;

import com.xclhove.xnote.enums.result.ResultType;

/**
 * 操作频率异常
 *
 * @author xclhove
 */
public class VerificationCodeException extends ServiceException {
    public VerificationCodeException() {
        this(ResultType.VERIFICATION_CODE_EXCEPTION.getMessage());
    }
    
    public VerificationCodeException(String message) {
        super(ResultType.VERIFICATION_CODE_EXCEPTION.getStatus(), message);
    }
}
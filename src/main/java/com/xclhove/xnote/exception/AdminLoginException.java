package com.xclhove.xnote.exception;

import com.xclhove.xnote.enums.ResultMessage;
import com.xclhove.xnote.enums.ResultStatus;

/**
 * @author xclhove
 */
public class AdminLoginException extends ServiceException {
    public AdminLoginException( ) {
        super(ResultStatus.ADMIN_LOGIN_EXCEPTION, ResultMessage.ADMIN_LOGIN_EXCEPTION.getMessage());
    }
    public AdminLoginException(String message) {
        super(ResultStatus.ADMIN_LOGIN_EXCEPTION, message);
    }
}

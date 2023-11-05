package com.xclhove.xnote.exception;

import com.xclhove.xnote.enums.ResultMessage;
import com.xclhove.xnote.enums.ResultStatus;

/**
 * @author xclhove
 */
public class UserLoginException extends ServiceException {
    public UserLoginException() {
        super(ResultStatus.LOGIN_EXCEPTION, ResultMessage.USER_LOGIN_EXCEPTION.getMessage());
    }
    public UserLoginException(String message) {
        super(ResultStatus.LOGIN_EXCEPTION, message);
    }
}
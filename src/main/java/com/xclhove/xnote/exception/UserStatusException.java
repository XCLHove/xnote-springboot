package com.xclhove.xnote.exception;

import com.xclhove.xnote.enums.ResultMessage;
import com.xclhove.xnote.enums.ResultStatus;

/**
 * @author xclhove
 */
public class UserStatusException extends ServiceException{
    public UserStatusException() {
        super(ResultStatus.USER_STATUS_EXCEPTION, ResultMessage.USER_BANNED.getMessage());
    }
    public UserStatusException(String message) {
        super(ResultStatus.USER_STATUS_EXCEPTION, message);
    }
}

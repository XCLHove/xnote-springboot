package com.xclhove.xnote.exception;

import com.xclhove.xnote.enums.ResultMessage;
import com.xclhove.xnote.enums.ResultStatus;

/**
 * @author xclhove
 */
public class TokenException extends ServiceException {
    public TokenException( ) {
        super(ResultStatus.TOKEN_EXCEPTION, ResultMessage.TOKEN_EXCEPTION.getMessage());
    }
    public TokenException(String message) {
        super(ResultStatus.TOKEN_EXCEPTION, message);
    }
}

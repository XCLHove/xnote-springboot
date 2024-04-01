package com.xclhove.xnote.exception;

import com.xclhove.xnote.enums.result.ResultType;

/**
 * 笔记访问码异常
 *
 * @author xclhove
 */
public class NoteAccessCodeException extends ServiceException {
    public NoteAccessCodeException() {
        this(ResultType.NOTE_ACCESS_CODE_EXCEPTION.getMessage());
    }
    
    public NoteAccessCodeException(String message) {
        this(message, null);
    }
    
    public NoteAccessCodeException(String message, Object data) {
        super(ResultType.NOTE_ACCESS_CODE_EXCEPTION.getStatus(), message, data);
    }
}
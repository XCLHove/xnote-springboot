package com.xclhove.xnote.exception;


import com.xclhove.xnote.enums.ResultType;

/**
 * @author xclhove
 */
public class NoteTypeServiceException extends AbstractServiceException {
    public NoteTypeServiceException() {
        this(ResultType.NOTE_TYPE_SERVICE_EXCEPTION.getMessage());
    }
    
    public NoteTypeServiceException(String message) {
        this(message, null);
    }
    
    public NoteTypeServiceException(String message, Object data) {
        super(ResultType.NOTE_TYPE_SERVICE_EXCEPTION.getStatus(), message, data);
    }
}

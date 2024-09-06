package com.xclhove.xnote.exception;


import com.xclhove.xnote.enums.ResultType;

/**
 * @author xclhove
 */
public class ShareNoteRecordServiceException extends AbstractServiceException {
    public ShareNoteRecordServiceException() {
        this(ResultType.SHARE_NOTE_RECORD_SERVICE_EXCEPTION.getMessage());
    }
    
    public ShareNoteRecordServiceException(String message) {
        this(message, null);
    }
    
    public ShareNoteRecordServiceException(String message, Object data) {
        super(ResultType.SHARE_NOTE_RECORD_SERVICE_EXCEPTION.getStatus(), message, data);
    }
}

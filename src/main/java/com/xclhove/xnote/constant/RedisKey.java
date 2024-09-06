package com.xclhove.xnote.constant;

/**
 * @author xclhove
 */
public interface RedisKey {
    String REDIS_CONNECT_TEST = "redisConnectTest";
    String PROJECT = "xnote";
    String USER = "user";
    String NOTE = "note";
    String INTERCEPTOR = "interceptor";
    String VERIFICATION_CODE = "verificationCode";
    String NOTE_TYPE = "noteType";
    String IMAGE = "image";
    String USER_IMAGE = "userImage";
    String SHARE_NOTE = "shareNote";
    String EXCEPTION = "exception";
    
    static String join(String... keys) {
        return String.join(":", keys);
    }
    
    interface Interceptor {
        String PREFIX = join(PROJECT, INTERCEPTOR);
        String DEVICE = join(PREFIX, "device");
        String IP = join(PREFIX, "ip");
    }
    
    interface User {
        String PREFIX = join(PROJECT, USER);
        String TOKEN = join(PREFIX, "token");
        String ID = join(PREFIX, "id");
        String ACCOUNT = join(PREFIX, "account");
        String EMAIL = join(PREFIX, "email");
    }
    
    interface Note {
        String PREFIX = join(PROJECT, NOTE);
    }
    
    interface VerificationCode {
        String PREFIX = join(PROJECT, VERIFICATION_CODE);
        String EMAIL = join(PREFIX, "email");
        String IP = join(PREFIX, "ip");
    }
    
    interface NoteType {
        String PREFIX = join(PROJECT, NOTE_TYPE);
        String USER_ALL = join(PREFIX, "userAll");
        String ID = join(PREFIX, "id");
    }
    
    interface Image {
        String PREFIX = join(PROJECT, IMAGE);
        String ID = join(PREFIX, "id");
        String NAME = join(PREFIX, "name");
        String URL = join(PREFIX, "url");
        String USER_TOTAL_SIZE = join(PREFIX, "userTotalSize");
    }
    
    interface UserImage {
        String PREFIX = join(PROJECT, USER_IMAGE);
        String USER_AND_IMAGE_ID = join(PREFIX, "userAndImageId");
    }
    
    interface ShareNote {
        String PREFIX = join(PROJECT, SHARE_NOTE);
        String ID = join(PREFIX, "id");
        String CODE = join(PREFIX, "code");
    }
    
    interface Exception {
        String PREFIX = join(PROJECT, EXCEPTION);
        String CAN_SEND = join(PREFIX, "canSend");
    }
}
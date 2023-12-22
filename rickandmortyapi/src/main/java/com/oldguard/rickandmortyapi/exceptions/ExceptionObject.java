package com.oldguard.rickandmortyapi.exceptions;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

public record ExceptionObject(int code, Map<String, String> errors, Date timeStamp) {
    public static ExceptionObject singleMessageException(int code, String message, Date timeStamp) {
        return new ExceptionObject(code, Collections.singletonMap("message", message), timeStamp);
    }

    public static ExceptionObject manyMessagesException(int code, Map<String, String> messages, Date timeStamp) {
        return new ExceptionObject(code, messages, timeStamp);
    }
}

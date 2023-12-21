package com.oldguard.rickandmortyapi.exceptions;

import java.util.Date;

public record ExceptionObject(int code, String message, Date timeStamp) {
    public static ExceptionObject getExceptionObject(int code, String message, Date timeStamp){
        return new ExceptionObject(code,message,timeStamp);
    }
}

package com.hisham.HomeCentre.exceptions.CustomExceptions;

public class EmailException extends RuntimeException{
    public EmailException(String message){
        super(message);
    }

    public EmailException(String message, Throwable cause){
        super(message, cause);
    }
}

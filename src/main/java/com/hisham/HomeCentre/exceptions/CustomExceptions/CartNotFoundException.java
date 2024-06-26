package com.hisham.HomeCentre.exceptions.CustomExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CartNotFoundException extends RuntimeException{
    public CartNotFoundException(String message){
        super(message);
    }

    public CartNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}

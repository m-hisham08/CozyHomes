package com.hisham.HomeCentre.exceptions.CustomExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class InsufficientInventoryException extends RuntimeException{
    public InsufficientInventoryException(String productName, Long stock){
        super("Insufficient inventory to fulfill order. We only have " + stock + " " + productName + " in stock.");
    }

    public InsufficientInventoryException(String productName, Long stock, Throwable cause){
        super("Insufficient inventory to fulfill order. We only have " + stock + " " + productName + " in stock.", cause);
    }
}

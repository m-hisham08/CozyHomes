package com.hisham.HomeCentre.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ExceptionDTO {
    private Instant timestamp;
    private String message;
    private String details;
}

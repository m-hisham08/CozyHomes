package com.hisham.HomeCentre.payloads.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class APIResponse {
    private Boolean success;
    private String message;
}

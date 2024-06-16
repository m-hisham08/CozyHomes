package com.hisham.HomeCentre.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/sayhello")
public class SayHello {
    @GetMapping("")
    public ResponseEntity sayHello(){
        return ResponseEntity.ok("Hello there!");
    }
}

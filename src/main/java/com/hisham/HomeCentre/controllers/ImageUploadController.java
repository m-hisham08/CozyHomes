package com.hisham.HomeCentre.controllers;

import com.hisham.HomeCentre.services.CloudinaryImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/upload")
public class ImageUploadController {
    @Autowired
    private CloudinaryImageUploadService cloudinaryService;

    @PostMapping("")
    public ResponseEntity<Map> uploadImage(
            @RequestParam(value = "image") MultipartFile file
    ){
        Map imageData = cloudinaryService.upload(file);
        return ResponseEntity.ok(imageData);
    }
}

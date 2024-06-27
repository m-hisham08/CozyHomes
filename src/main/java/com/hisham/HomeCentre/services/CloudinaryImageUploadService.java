package com.hisham.HomeCentre.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CloudinaryImageUploadService {
    Map upload(MultipartFile file);
}

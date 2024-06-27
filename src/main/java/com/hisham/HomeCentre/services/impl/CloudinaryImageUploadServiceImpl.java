package com.hisham.HomeCentre.services.impl;

import com.cloudinary.Cloudinary;
import com.hisham.HomeCentre.constants.AppConstants;
import com.hisham.HomeCentre.exceptions.CustomExceptions.CloudinaryException;
import com.hisham.HomeCentre.exceptions.CustomExceptions.ImageUploadException;
import com.hisham.HomeCentre.services.CloudinaryImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryImageUploadServiceImpl implements CloudinaryImageUploadService {
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public Map<String, String> upload(MultipartFile file) {
        try {
            validateFile(file);
            Map<String, Object> data = cloudinary.uploader().upload(file.getBytes(), Map.of());
            String imageURL = data.get("secure_url").toString();
            return Map.of("url", imageURL);
        } catch (Exception e) {
            throw new CloudinaryException(e.getMessage());
        }
    }

    private void validateFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new CloudinaryException("The file is empty.");
        }
        if (!AppConstants.ImageUpload.ALLOWED_FILE_TYPES.contains(file.getContentType())) {
            throw new ImageUploadException("Invalid file type. Only JPEG, PNG, and GIF are allowed.");
        }
        if (file.getSize() > AppConstants.ImageUpload.MAX_FILE_SIZE) {
            throw new ImageUploadException("File size exceeds the maximum limit of "+ AppConstants.ImageUpload.MAX_FILE_SIZE/1024 * 1024 +"MB.");
        }
    }
}

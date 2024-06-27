package com.hisham.HomeCentre.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfiig {
    @Value("${app.cloudName}")
    private String cloudName;

    @Value("${app.apiKey}")
    private String apiKey;

    @Value("${app.apiSecret}")
    private String apiSecret;

    @Value("${app.secure}")
    private boolean secure;

    @Bean
    public Cloudinary getCloudinaryConfig(){
        Map config = new HashMap();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        config.put("secure", secure);

        Cloudinary cloudinary = new Cloudinary(config);
        return cloudinary;
    }
}

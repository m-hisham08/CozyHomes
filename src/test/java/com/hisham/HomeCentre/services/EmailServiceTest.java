package com.hisham.HomeCentre.services;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Disabled
    @Test
    public void EmailService_SendEmail_ReturnsVoid(){
        emailService.sendEmail(
                "youremail@email.com",
                "Testing Email Service",
                "Hello Hisham, congrats your test works fine!"
                );
    }
}

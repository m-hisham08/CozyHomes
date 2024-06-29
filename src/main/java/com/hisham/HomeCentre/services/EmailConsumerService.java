package com.hisham.HomeCentre.services;

import com.hisham.HomeCentre.models.kafka.EmailData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EmailConsumerService {

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "email", groupId = "email-group")
    public void consume(EmailData emailData){
        sendEmail(emailData);
    }

    public void sendEmail(EmailData emailData){
        emailService.sendEmail(emailData.getTo(), emailData.getSubject(), emailData.getContent());
    }
}

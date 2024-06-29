package com.hisham.HomeCentre.schedules;

import com.hisham.HomeCentre.models.User;
import com.hisham.HomeCentre.models.kafka.EmailData;
import com.hisham.HomeCentre.repositories.UserRepository;
import com.hisham.HomeCentre.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PromotionalEmails {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaTemplate<String, EmailData> kafkaTemplate;

//     @Scheduled(cron = "0 * * ? * *") // Every Minute (Test with breakpoints)
    @Scheduled(cron = "0 0 9 ? * MON") // Every Monday at 9 AM
    public void getAllUsersAndSendPromotionalEmails(){
        List<User> users = userRepository.findAll();
        for (User user: users){
            String subject = "Don't Miss Out on This Week's Exciting Deals!";
            String content = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <title>Weekly Deals</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <p>Hi " + user.getLastName() + ",</p>\n" +
                    "    <p>We hope you're having a great week! 🌟</p>\n" +
                    "    <p>This week at Cozy Homes, we have some amazing new arrivals and exclusive deals just for you. From the latest trends to unbeatable discounts, there's something for everyone.</p>\n" +
                    "    <ul>\n" +
                    "        <li><strong>Top Picks of the Week:</strong><br>\n" +
                    "            Discover our curated selection of must-have items. Whether you're looking for the perfect gift or a treat for yourself, our top picks won't disappoint.\n" +
                    "        </li>\n" +
                    "        <li><strong>Limited-Time Offers:</strong><br>\n" +
                    "            Hurry and grab these deals before they're gone! Check out our special discounts on a range of products. These offers are available for a limited time only, so act fast!\n" +
                    "        </li>\n" +
                    "        <li><strong>Customer Favorites:</strong><br>\n" +
                    "            See what others are loving and shop our bestsellers. Join the trend and find out why these items are flying off the shelves.\n" +
                    "        </li>\n" +
                    "    </ul>\n" +
                    "    <p>Visit our website now to explore these fantastic deals and more. Thank you for being a valued member of our community. We're always here to make your shopping experience better.</p>\n" +
                    "    <p>Happy Shopping!</p>\n" +
                    "    <p>Best regards,<br>\n" +
                    "    Mohammad Hisham<br>\n" +
                    "    <a href=\"https://stock.adobe.com/search?k=cat\">CozyHomes.in</a>\n" +
                    "    </p>\n" +
                    "</body>\n" +
                    "</html>\n";
            String to = user.getEmail();

            EmailData emailData = new EmailData(to, subject, content);
            kafkaTemplate.send("email", to, emailData);
//            emailService.sendEmail(to, subject, content);
        }
    }
}

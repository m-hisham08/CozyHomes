package com.hisham.HomeCentre.services.impl;

import com.hisham.HomeCentre.exceptions.CustomExceptions.PaymentException;
import com.hisham.HomeCentre.exceptions.CustomExceptions.ResourceNotFoundException;
import com.hisham.HomeCentre.models.Order;
import com.hisham.HomeCentre.models.OrderStatus;
import com.hisham.HomeCentre.models.User;
import com.hisham.HomeCentre.models.kafka.EmailData;
import com.hisham.HomeCentre.repositories.OrderRepository;
import com.hisham.HomeCentre.repositories.UserRepository;
import com.hisham.HomeCentre.services.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    public OrderRepository orderRepository;

    @Autowired
    private KafkaTemplate<String, EmailData> kafkaTemplate;

    @Autowired
    private UserRepository userRepository;

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Override
    public PaymentIntent createPaymentIntent(Long orderId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "ID", orderId));

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(order.getTotalPrice().multiply(BigDecimal.valueOf(100)).longValue())
                .setCurrency("usd")
                .setDescription("Payment for order " + order.getId())
                .build();

        return PaymentIntent.create(params);
    }

    @Override
    @Transactional
    public void handlePaymentSuccess(String paymentIntentId, Long orderId) {
        Stripe.apiKey = stripeSecretKey;

        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

            // Check if the PaymentIntent status is 'succeeded'
            if (!"succeeded".equals(paymentIntent.getStatus())) {
                Order order = orderRepository.findById(orderId)
                        .orElseThrow(() -> new ResourceNotFoundException("Order", "ID", orderId));

                order.setStatus(OrderStatus.PAID);
                orderRepository.save(order);

                // Send confirmation email
                sendConfirmationEmail(order);
            } else {
                throw new PaymentException("Payment not successful: " + paymentIntent.getStatus());
            }
        } catch (StripeException e) {
            throw new PaymentException("Stripe API error: " + e.getMessage(), e);
        }
    }

    private void sendConfirmationEmail(Order order) {
        User user = userRepository.findById(order.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "ID", order.getUser().getId()));

        String subject = "Order Confirmation - Thank You for Your Purchase!";
        String content = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Order Confirmation</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <p>Hi " + user.getLastName() + ",</p>\n" +
                "    <p>Thank you for your order! 🎉</p>\n" +
                "    <p>We are pleased to confirm that we have received your payment. Your order ID is <strong>" + order.getId() + "</strong>.</p>\n" +
                "    <p>We will notify you once your items have been shipped. You can track your order status on our website.</p>\n" +
                "    <p>If you have any questions or need further assistance, please don't hesitate to contact us.</p>\n" +
                "    <p>Thank you for shopping with us!</p>\n" +
                "    <p>Best regards,<br>\n" +
                "    John Doe<br>\n" +
                "    <a href=\"https://stock.adobe.com/search?k=cat\">CozyHomes.in</a>\n" +
                "    </p>\n" +
                "</body>\n" +
                "</html>\n";
        String to = user.getEmail();

        EmailData emailData = new EmailData(to, subject, content);
        kafkaTemplate.send("email", to, emailData);
    }
}

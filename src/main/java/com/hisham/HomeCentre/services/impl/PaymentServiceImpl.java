package com.hisham.HomeCentre.services.impl;

import com.hisham.HomeCentre.exceptions.CustomExceptions.PaymentException;
import com.hisham.HomeCentre.exceptions.CustomExceptions.ResourceNotFoundException;
import com.hisham.HomeCentre.models.Order;
import com.hisham.HomeCentre.models.OrderStatus;
import com.hisham.HomeCentre.repositories.OrderRepository;
import com.hisham.HomeCentre.services.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    public OrderRepository orderRepository;

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
            if ("succeeded".equals(paymentIntent.getStatus())) {
                Order order = orderRepository.findById(orderId)
                        .orElseThrow(() -> new ResourceNotFoundException("Order", "ID", orderId));

                order.setStatus(OrderStatus.PAID);
                orderRepository.save(order);
            } else {
                throw new PaymentException("Payment not successful: " + paymentIntent.getStatus());
            }
        } catch (StripeException e) {
            throw new PaymentException("Stripe API error: " + e.getMessage(), e);
        }
    }
}

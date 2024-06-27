package com.hisham.HomeCentre.services;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface PaymentService {
    PaymentIntent createPaymentIntent(Long orderId) throws StripeException;
    void handlePaymentSuccess(String paymentIntentId, Long userId);
}

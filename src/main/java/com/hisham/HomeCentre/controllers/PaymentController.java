package com.hisham.HomeCentre.controllers;

import com.hisham.HomeCentre.payloads.payments.PaymentResponse;
import com.hisham.HomeCentre.services.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/create-payment-intent")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/{orderId}")
    public ResponseEntity<PaymentResponse> createPaymentIntent(@PathVariable Long orderId){
        try {
            PaymentIntent paymentIntent = paymentService.createPaymentIntent(orderId);
            PaymentResponse paymentResponse =  new PaymentResponse(
                    paymentIntent.getId(),
                    paymentIntent.getStatus(),
                    paymentIntent.getAmount(),
                    paymentIntent.getCurrency(),
                    paymentIntent.getDescription(),
                    paymentIntent.getClientSecret()
            );

            return ResponseEntity.ok(paymentResponse);
        } catch (StripeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/success")
    public ResponseEntity<Void> handlePaymentSuccess(@RequestParam String paymentIntentId, @RequestParam Long orderId) {
        paymentService.handlePaymentSuccess(paymentIntentId, orderId);
        return ResponseEntity.ok().build();
    }
}

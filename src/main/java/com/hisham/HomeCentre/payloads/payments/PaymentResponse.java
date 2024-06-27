package com.hisham.HomeCentre.payloads.payments;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private String id;
    private String status;
    private Long amount;
    private String currency;
    private String description;
    private String clientSecret;
}

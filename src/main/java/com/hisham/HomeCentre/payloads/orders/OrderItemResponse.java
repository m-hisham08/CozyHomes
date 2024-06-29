package com.hisham.HomeCentre.payloads.orders;

import com.hisham.HomeCentre.payloads.products.ProductResponse;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    private Long id;
    private ProductResponse product;
}

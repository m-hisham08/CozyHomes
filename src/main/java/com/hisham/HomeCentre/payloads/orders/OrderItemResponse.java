package com.hisham.HomeCentre.payloads.orders;

import com.hisham.HomeCentre.models.Order;
import com.hisham.HomeCentre.models.Product;
import com.hisham.HomeCentre.payloads.products.ProductResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    private Long id;
    private ProductResponse product;
}

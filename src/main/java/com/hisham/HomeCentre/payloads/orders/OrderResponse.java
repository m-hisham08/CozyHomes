package com.hisham.HomeCentre.payloads.orders;

import com.hisham.HomeCentre.models.OrderItem;
import com.hisham.HomeCentre.models.OrderStatus;
import com.hisham.HomeCentre.models.User;
import com.hisham.HomeCentre.payloads.UserSummary;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private UserSummary user;
    private Set<OrderItemResponse> items = new HashSet<>();
    private String status;
    private BigDecimal totalPrice;
}

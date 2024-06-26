package com.hisham.HomeCentre.payloads.carts;

import jakarta.validation.constraints.Min;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartRequest {
    @Min(value = 1, message = "Quantity must be least 1!")
    private Long quantity;
}

package com.hisham.HomeCentre.payloads.carts;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hisham.HomeCentre.payloads.products.ProductResponse;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItemResponse {
    private Long id;
    private ProductResponse product;
    private Long quantity;
}

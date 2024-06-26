package com.hisham.HomeCentre.payloads.carts;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hisham.HomeCentre.models.Cart;
import com.hisham.HomeCentre.models.Product;
import com.hisham.HomeCentre.payloads.products.ProductResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

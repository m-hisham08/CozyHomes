package com.hisham.HomeCentre.payloads.products;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductRequest {
    @NotBlank
    @Size(max = 40, min = 4)
    private String name;

    @NotBlank
    private String imageURL;

    @NotBlank
    @Size(max = 300, min = 50)
    private String description;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal price;

    @NotNull
    @Min(0)
    private Long stock;

    @NotNull
    @Min(0)
    private Long categoryId;
}

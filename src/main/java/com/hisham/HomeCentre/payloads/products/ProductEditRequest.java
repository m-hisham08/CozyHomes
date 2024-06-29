package com.hisham.HomeCentre.payloads.products;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Optional;

@Getter
@Setter
public class ProductEditRequest {
    @Size(max = 40, min = 4, message = "Name must be between 4 and 40 characters")
    private String name;

    @NotBlank(message = "Image URL cannot be blank")
    private String imageURL;

    @Size(max = 300, min = 50, message = "Description must be between 50 and 300 characters")
    private String description;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be at least 0.0")
    private BigDecimal price;

    @Min(value = 0, message = "Stock must be at least 0")
    private Long stock;

    @Min(value = 0, message = "Category ID must be at least 0")
    private Long categoryId;
}

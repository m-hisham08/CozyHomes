package com.hisham.HomeCentre.payloads.products;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hisham.HomeCentre.payloads.UserSummary;
import com.hisham.HomeCentre.payloads.categories.CategoryResponse;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {
    private Long id;
    private String name;
    private String imageURL;
    private String description;
    private BigDecimal price;
    private Long stock;
    private CategoryResponse category;
    private Instant createdAt;
    private Instant lastModifiedAt;
    private UserSummary createdBy;
    private UserSummary lastModifiedBy;
    private Boolean isAvailable;
}

package com.hisham.HomeCentre.payloads.reviews;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hisham.HomeCentre.models.Product;
import com.hisham.HomeCentre.payloads.UserSummary;
import com.hisham.HomeCentre.payloads.products.ProductResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewResponse {
    private Long id;
    private ProductResponse product;
    private Float rating;
    private String comment;
    private Instant createdAt;
    private UserSummary createdBy;
}

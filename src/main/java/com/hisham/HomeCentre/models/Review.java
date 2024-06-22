package com.hisham.HomeCentre.models;

import com.hisham.HomeCentre.models.audit.UserDateAudit;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "reviews")
public class Review extends UserDateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    Product product;

    @NotNull
    @Min(1)
    @Max(5)
    private Float rating;

    @NotBlank
    @Size(max = 500)
    private String comment;
}

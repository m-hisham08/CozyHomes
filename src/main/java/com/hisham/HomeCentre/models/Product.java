package com.hisham.HomeCentre.models;

import com.hisham.HomeCentre.models.audit.UserDateAudit;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products")
public class Product extends UserDateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    public Boolean isAvailable(){
        return stock > 0;
    }

    public void addReview(Review review){
        reviews.add(review);
        review.setProduct(this);
    }

    public void removeReview(Review review){
        reviews.remove(review);
        review.setProduct(null);
    }
}

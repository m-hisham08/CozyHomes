package com.hisham.HomeCentre.models;

import com.hisham.HomeCentre.models.audit.UserDateAudit;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "cart_items")
public class CartItem extends UserDateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Min(1)
    private Long quantity;

    public void setQuantity(Long quantity){
        if(quantity < 1){
            throw new IllegalArgumentException("Quantity must be atleast 1!");
        }
        this.quantity = quantity;
    }
}

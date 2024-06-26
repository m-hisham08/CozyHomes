package com.hisham.HomeCentre.models;

import com.hisham.HomeCentre.models.audit.UserDateAudit;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_items")
public class OrderItem extends UserDateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Min(1)
    private Long quantity;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    public void setQuantity(Long quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1!");
        }
        this.quantity = quantity;
    }
}

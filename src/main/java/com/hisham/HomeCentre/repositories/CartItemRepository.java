package com.hisham.HomeCentre.repositories;

import com.hisham.HomeCentre.models.CartItem;
import com.hisham.HomeCentre.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByProduct(Product product);
}

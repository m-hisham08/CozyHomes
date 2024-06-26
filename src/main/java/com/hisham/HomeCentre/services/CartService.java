package com.hisham.HomeCentre.services;

import com.hisham.HomeCentre.models.Cart;
import org.springframework.security.core.userdetails.UserDetails;

public interface CartService {
    Cart getCartByUser(UserDetails user);
    Cart addProductToCart(UserDetails user, Long productId, Long quantity);
    void removeProductFromCart(UserDetails user, Long productId);
}

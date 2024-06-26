package com.hisham.HomeCentre.services.impl;

import com.hisham.HomeCentre.exceptions.CustomExceptions.CartNotFoundException;
import com.hisham.HomeCentre.exceptions.CustomExceptions.ResourceNotFoundException;
import com.hisham.HomeCentre.models.Cart;
import com.hisham.HomeCentre.models.CartItem;
import com.hisham.HomeCentre.models.Product;
import com.hisham.HomeCentre.models.User;
import com.hisham.HomeCentre.repositories.CartItemRepository;
import com.hisham.HomeCentre.repositories.CartRepository;
import com.hisham.HomeCentre.repositories.ProductRepository;
import com.hisham.HomeCentre.repositories.UserRepository;
import com.hisham.HomeCentre.services.CartService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional
    public Cart getCartByUser(UserDetails user) {
        User currentUser = userRepository.findByEmailOrUsername(user.getUsername(), user.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", user.getUsername()));
        return cartRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new CartNotFoundException("Cart not found for user with username: "+ currentUser.getUsername() + ". Please add products to create your cart!"));
    }

    @Override
    @Transactional
    public Cart addProductToCart(UserDetails user, Long productId, Long quantity) {
        // Retrieve the user from database
        User currentUser = userRepository.findByEmailOrUsername(user.getUsername(), user.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", user.getUsername()));

        // Retrieve the user's cart if it exists
        Cart cart = cartRepository.findByUserId(currentUser.getId()).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(currentUser);
            cartRepository.save(newCart);
            return newCart;
        });

        // Retrieve the product from database
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", productId));

        // Check if the product is already in the cart
        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingCartItem.isPresent()) {
            // Update the quantity of the existing cart item
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        } else {
            // Create a new cart item and add it to the cart
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cart.addItem(cartItem); // Ensure bidirectional relationship is set
            cartItemRepository.save(cartItem);
        }

        // Save the cart after modifications
        // Ensure CartItems are cascaded correctly
        cart = cartRepository.save(cart);

        return cart;
    }

    @Override
    @Transactional
    public void removeProductFromCart(UserDetails user, Long productId) {
        User currentUser = userRepository.findByEmailOrUsername(user.getUsername(), user.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", user.getUsername()));

        Cart cart = cartRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart", null, null));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", productId));

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart Item", "Product ID", productId));

        cart.removeItem(cartItem); // Remove from cart collection
        cartItemRepository.delete(cartItem); // Delete from repository
    }
}

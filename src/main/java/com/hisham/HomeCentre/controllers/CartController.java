package com.hisham.HomeCentre.controllers;

import com.hisham.HomeCentre.exceptions.CustomExceptions.ResourceNotFoundException;
import com.hisham.HomeCentre.models.Cart;
import com.hisham.HomeCentre.models.User;
import com.hisham.HomeCentre.payloads.APIResponse;
import com.hisham.HomeCentre.payloads.UserSummary;
import com.hisham.HomeCentre.payloads.carts.CartItemResponse;
import com.hisham.HomeCentre.payloads.carts.CartRequest;
import com.hisham.HomeCentre.payloads.carts.CartResponse;
import com.hisham.HomeCentre.payloads.products.ProductResponse;
import com.hisham.HomeCentre.repositories.UserRepository;
import com.hisham.HomeCentre.security.CurrentUser;
import com.hisham.HomeCentre.services.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public ResponseEntity<CartResponse> getUserCart(
            @CurrentUser UserDetails userDetails
            ){
       Cart cart =  cartService.getCartByUser(userDetails);

        User user = userRepository.findByEmailOrUsername(userDetails.getUsername(), userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", userDetails.getUsername()));

        CartResponse cartResponse = CartResponse.builder()
                .id(cart.getId())
                .items(cart.getCartItems().stream()
                        .map(item -> new CartItemResponse(
                                item.getId(),
                                ProductResponse.builder()
                                        .id(item.getProduct().getId())
                                        .name(item.getProduct().getName())
                                        .imageURL(item.getProduct().getImageURL())
                                        .price(item.getProduct().getPrice())
                                        .stock(item.getProduct().getStock())
                                        .build(),
                                item.getQuantity()))
                        .collect(Collectors.toSet()))
                .user(UserSummary.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .build())
                .build();

       return ResponseEntity.ok(cartResponse);
    }

    @PostMapping("/{productId}")
    public ResponseEntity<APIResponse> addProductToCart(
            @CurrentUser UserDetails userDetails,
            @RequestBody @Valid CartRequest cartRequest,
            @PathVariable(name = "productId") Long productId
            ){
    cartService.addProductToCart(userDetails, productId, cartRequest.getQuantity());

    return ResponseEntity.ok(new APIResponse(Boolean.TRUE, "Product added to cart!"));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProductFromCart(
            @CurrentUser UserDetails userDetails,
            @PathVariable(name = "productId") Long productId
    ){
        cartService.removeProductFromCart(userDetails, productId);
        return ResponseEntity.noContent().build();
    }
}

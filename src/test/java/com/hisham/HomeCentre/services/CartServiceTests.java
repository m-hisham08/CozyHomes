package com.hisham.HomeCentre.services;

import com.hisham.HomeCentre.models.Cart;
import com.hisham.HomeCentre.models.CartItem;
import com.hisham.HomeCentre.models.Product;
import com.hisham.HomeCentre.models.User;
import com.hisham.HomeCentre.repositories.CartItemRepository;
import com.hisham.HomeCentre.repositories.CartRepository;
import com.hisham.HomeCentre.repositories.ProductRepository;
import com.hisham.HomeCentre.repositories.UserRepository;
import com.hisham.HomeCentre.services.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class CartServiceTests {
    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCartByUser() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");

        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        Cart cart = new Cart();
        cart.setUser(user);

        when(userRepository.findByEmailOrUsername("testUser", "testUser")).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));

        Cart result = cartService.getCartByUser(userDetails);

        assertNotNull(result);
        assertEquals(user, result.getUser());
        verify(userRepository, times(1)).findByEmailOrUsername("testUser", "testUser");
        verify(cartRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testAddProductToCart() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");

        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        Product product = new Product();
        product.setId(1L);

        Cart cart = new Cart();
        cart.setUser(user);

        when(userRepository.findByEmailOrUsername("testUser", "testUser")).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(1L);
        cart.addItem(cartItem);

        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.addProductToCart(userDetails, 1L, 1L);

        assertNotNull(result);
        assertEquals(1, result.getCartItems().size());
        verify(userRepository, times(1)).findByEmailOrUsername("testUser", "testUser");
        verify(cartRepository, times(1)).findByUserId(1L);
        verify(productRepository, times(1)).findById(1L);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void testRemoveProductFromCart() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");

        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        Product product = new Product();
        product.setId(1L);

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.addItem(cartItem);

        when(userRepository.findByEmailOrUsername("testUser", "testUser")).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        cartService.removeProductFromCart(userDetails, 1L);

        verify(userRepository, times(1)).findByEmailOrUsername("testUser", "testUser");
        verify(cartRepository, times(1)).findByUserId(1L);
        verify(productRepository, times(1)).findById(1L);
        verify(cartItemRepository, times(1)).delete(cartItem);
    }
}

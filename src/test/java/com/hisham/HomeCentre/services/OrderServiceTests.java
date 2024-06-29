package com.hisham.HomeCentre.services;

import com.hisham.HomeCentre.models.*;
import com.hisham.HomeCentre.repositories.CartRepository;
import com.hisham.HomeCentre.repositories.OrderRepository;
import com.hisham.HomeCentre.repositories.UserRepository;
import com.hisham.HomeCentre.services.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderServiceTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Disabled
    @Test
    void testCreateOrderFromCart() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");

        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        Product product = new Product();
        product.setId(1L);
        product.setPrice(new BigDecimal("100.00"));

        Cart cart = new Cart();
        cart.setUser(user);

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2L);

        when(userRepository.findByEmailOrUsername("testUser", "testUser")).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));

        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setUser(user);
        savedOrder.setTotalPrice(new BigDecimal("200.00"));

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        Order result = orderService.createOrderFromCart(userDetails);

        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(new BigDecimal("200.00"), result.getTotalPrice());
        assertEquals(1, result.getOrderItems().size());
        assertEquals(product, result.getOrderItems().iterator().next().getProduct());

        verify(userRepository, times(1)).findByEmailOrUsername("testUser", "testUser");
        verify(cartRepository, times(1)).findByUserId(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(cartRepository, times(1)).deleteAll();
    }

    @Test
    void testGetAllOrders() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");

        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");

        Page<Order> expectedPage = mock(Page.class);
        when(expectedPage.getContent()).thenReturn(Collections.emptyList());

        when(userRepository.findByEmailOrUsername("testUser", "testUser")).thenReturn(Optional.of(user));
        when(orderRepository.findAllByUser(user, pageable)).thenReturn(expectedPage);

        Page<Order> result = orderService.getAllOrders(userDetails, 0, 10, "ASC", "id");

        assertNotNull(result);
        assertEquals(0, result.getContent().size());

        verify(userRepository, times(1)).findByEmailOrUsername("testUser", "testUser");
        verify(orderRepository, times(1)).findAllByUser(user, pageable);
    }
}

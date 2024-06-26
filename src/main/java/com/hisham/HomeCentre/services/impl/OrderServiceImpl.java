package com.hisham.HomeCentre.services.impl;


import com.hisham.HomeCentre.constants.AppConstants;
import com.hisham.HomeCentre.exceptions.CustomExceptions.BadRequestException;
import com.hisham.HomeCentre.exceptions.CustomExceptions.CartNotFoundException;
import com.hisham.HomeCentre.exceptions.CustomExceptions.ResourceNotFoundException;
import com.hisham.HomeCentre.models.*;
import com.hisham.HomeCentre.repositories.CartRepository;
import com.hisham.HomeCentre.repositories.OrderRepository;
import com.hisham.HomeCentre.repositories.UserRepository;
import com.hisham.HomeCentre.services.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Override
    @Transactional
    public Order createOrderFromCart(UserDetails user) {
        User currentUser = userRepository.findByEmailOrUsername(user.getUsername(), user.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", user.getUsername()));

        Cart cart = cartRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new CartNotFoundException("Cart not found for user with username: "+ currentUser.getUsername() + ". Please add products to create your cart!"));

        Order order = new Order();
        order.setUser(currentUser);
        order.setStatus(OrderStatus.PENDING);

        BigDecimal totalPrice = BigDecimal.ZERO;
        Set<OrderItem> orderItems = new HashSet<>();

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order); // Set the Order instance for each OrderItem
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItem.setQuantity(cartItem.getQuantity());

            orderItems.add(orderItem);

            // Calculate total price
            totalPrice = totalPrice.add(cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice);

        // Save Order and associated OrderItems
        order = orderRepository.save(order);

        // Clear the cart after successful order creation
        cartRepository.deleteAll();

        return order;
    }

    @Override
    @Transactional
    public Page<Order> getAllOrders(UserDetails user, int page, int size, String sortDirection, String sortBy) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }
        if (size > AppConstants.Defaults.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page number cannot exceed " + AppConstants.Defaults.MAX_PAGE_SIZE);
        }
        if (!sortDirection.equals("ASC") && !sortDirection.equals("DESC")) {
            throw new BadRequestException("Invalid sort direction!");
        }

        User currentUser = userRepository.findByEmailOrUsername(user.getUsername(), user.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", user.getUsername()));

        Pageable pageable;
        if(sortDirection.equals("DESC")){
            pageable = PageRequest.of(page, size, Sort.Direction.DESC, sortBy);
        }else{
            pageable = PageRequest.of(page, size, Sort.Direction.ASC, sortBy);
        }

        Page<Order> pagedOrder = orderRepository.findAllByUser(currentUser, pageable);

        return pagedOrder;
    }


}

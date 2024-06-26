package com.hisham.HomeCentre.services;

import com.hisham.HomeCentre.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

public interface OrderService {
    Order createOrderFromCart(UserDetails user);
    Page<Order> getAllOrders(UserDetails user, int page, int size, String sortDirection, String sortBy);
}

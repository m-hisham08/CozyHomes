package com.hisham.HomeCentre.controllers;

import com.hisham.HomeCentre.constants.AppConstants;
import com.hisham.HomeCentre.exceptions.CustomExceptions.ResourceNotFoundException;
import com.hisham.HomeCentre.models.Order;
import com.hisham.HomeCentre.models.User;
import com.hisham.HomeCentre.payloads.APIResponse;
import com.hisham.HomeCentre.payloads.PagedResponse;
import com.hisham.HomeCentre.payloads.UserSummary;
import com.hisham.HomeCentre.payloads.orders.OrderItemResponse;
import com.hisham.HomeCentre.payloads.orders.OrderResponse;
import com.hisham.HomeCentre.payloads.products.ProductResponse;
import com.hisham.HomeCentre.repositories.UserRepository;
import com.hisham.HomeCentre.security.CurrentUser;
import com.hisham.HomeCentre.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public ResponseEntity<PagedResponse<OrderResponse>> viewAllOrders(
            @CurrentUser UserDetails userDetails,
            @RequestParam(name = "page", defaultValue = AppConstants.Defaults.PAGE_NUMBER) int page,
            @RequestParam(name = "size", defaultValue = AppConstants.Defaults.PAGE_SIZE) int size,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.Defaults.SORT_BY) String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = AppConstants.Defaults.SORT) String sortDirection
    ){
        User user = userRepository.findByEmailOrUsername(userDetails.getUsername(), userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", userDetails.getUsername()));
        UserSummary userSummary = UserSummary.builder().id(user.getId()).username(user.getUsername()).firstName(user.getFirstName()).lastName(user.getLastName()).build();

        Page<Order> pagedOrder = orderService.getAllOrders(userDetails, page, size, sortDirection, sortBy);
        List<Order> orderList = pagedOrder.getContent();

        List<OrderResponse> orderResponseList = new ArrayList<>();
        orderList.forEach(order -> {

            orderResponseList.add(OrderResponse.builder()
                            .id(order.getId())
                            .user(userSummary)
                            .items(order.getOrderItems().stream().map(orderItem -> OrderItemResponse.builder().id(orderItem.getId()).product(ProductResponse.builder().id(orderItem.getProduct().getId()).name(orderItem.getProduct().getName()).imageURL(orderItem.getProduct().getImageURL()).price(orderItem.getProduct().getPrice()).stock(orderItem.getProduct().getStock()).build()).build()).collect(Collectors.toSet()))
                            .status(String.valueOf(order.getStatus()))
                            .totalPrice(order.getTotalPrice())
                    .build());
        });

        return ResponseEntity.ok().body(new PagedResponse<OrderResponse>(orderResponseList, pagedOrder.getNumber(), pagedOrder.getSize(), pagedOrder.getNumberOfElements(), pagedOrder.getTotalPages(), pagedOrder.isLast()));
    }

    @PostMapping("")
    public ResponseEntity<APIResponse> createOrderFromCart(@CurrentUser UserDetails userDetails){
        Order order = orderService.createOrderFromCart(userDetails);
        return ResponseEntity.ok().body(new APIResponse(Boolean.TRUE, "Order created successfully!"));
    }
}

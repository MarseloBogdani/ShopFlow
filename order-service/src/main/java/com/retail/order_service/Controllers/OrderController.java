package com.retail.order_service.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.retail.order_service.Entities.Order;
import com.retail.order_service.Entities.OrderItem;
import com.retail.order_service.Services.OrderService;

import jakarta.validation.Valid;



@RestController
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders/make-order/{userId}")
    public Order make_Order(@PathVariable Integer userId, @RequestBody List<@Valid OrderItem> OrderItems) {
        return orderService.makeOrder(userId,OrderItems);
    }

    @GetMapping("/orders")
    public Page<Order> getAll_Orders(@PageableDefault(size = 20) Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }

    @GetMapping("/orders/customer/{id}")
    public Page<Order> getOrdersByCustomer_id(@PathVariable Integer id, @PageableDefault(size = 10) Pageable pageable) {
        return orderService.getAllOrdersByCustomerId(id, pageable);
    }

    @GetMapping("/orders/{id}")
    public Order GetOrderBy_id(@PathVariable Integer id) {
        return orderService.getOrderById(id);
    }
    
}

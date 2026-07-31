package com.retail.order_service.Services;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import com.retail.order_service.Entities.OrderItem;
import com.retail.order_service.Exceptions.NotEnoughStockException;
import com.retail.order_service.Exceptions.ResourceNotFoundException;
import com.retail.order_service.Repositories.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final RestClient restClient;

    @Autowired
    public OrderService(OrderRepository orderRepository, RestClient.Builder builder) {
        this.orderRepository = orderRepository;
        this.restClient = builder.baseUrl("http://items").build();
    }
    

    @Transactional
    public void makeOrder(List<OrderItem> orderItems) {
        restClient.post().uri("/items/ReserveStockBatch/v1", orderItems);
    }
}
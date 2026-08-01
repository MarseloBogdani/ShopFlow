package com.retail.order_service.Services;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import com.retail.order_service.Entities.Order;
import com.retail.order_service.Entities.OrderItem;
import com.retail.order_service.Entities.OrderStatus;
import com.retail.order_service.Exceptions.NotEnoughStockException;
import com.retail.order_service.Exceptions.ResourceNotFoundException;
import com.retail.order_service.Records.DeductStockAnswer;
import com.retail.order_service.Records.DeductStockRequest;
import com.retail.order_service.Repositories.OrderRepository;

@Service
public class OrderService {
    private final Integer DEBUG_CUSTOMER_ID = 1;
    private final OrderRepository orderRepository;
    private final RestClient restClient;

    @Autowired
    public OrderService(OrderRepository orderRepository, RestClient.Builder builder) {
        this.orderRepository = orderRepository;
        this.restClient = builder.baseUrl("http://items:8080").build();
    }
    
    public Order makeOrder(Integer userId,List<OrderItem> orderItems) {
        List<DeductStockRequest> requests = orderItems.stream()
        .map(item -> new DeductStockRequest(item.getProductId(), item.getQuantity()))
        .toList();

        List<DeductStockAnswer> answers;
        try {
            answers = restClient.post()
                    .uri("/items/deduct-batch")
                    .body(requests)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<DeductStockAnswer>>() {});
        } catch (RestClientResponseException e) {
            throw new NotEnoughStockException(e.getResponseBodyAsString());
        }

        if(answers == null) {
            throw new ResourceNotFoundException("Could not retrieve Prices");
        }

        Map<Integer, DeductStockAnswer> answerMap = answers.stream()
            .collect(Collectors.toMap(DeductStockAnswer::productId, answer -> answer));

        for (OrderItem item : orderItems) {
            DeductStockAnswer answer = answerMap.get(item.getProductId());

            if (answer == null) {
                throw new IllegalStateException("Missing price/stock response for product: " + item.getProductId());
            }
            item.setPrice(answer.price());
        }

        Order myOrder = new Order(DEBUG_CUSTOMER_ID,OrderStatus.COMPLETE,orderItems);        
        return orderRepository.save(myOrder);

    }

    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Page<Order> getAllOrdersByCustomerId(Integer customerId, Pageable pageable) {
        return orderRepository.findByCustomerId(customerId,pageable);
    }

    public Order getOrderById(Integer orderId) {
        if (orderId == null || orderId <= 0) {
            throw new IllegalArgumentException("id must be a positive integer");
        }

        return orderRepository
            .findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order with id:" + orderId + " not found!"));
    }

}
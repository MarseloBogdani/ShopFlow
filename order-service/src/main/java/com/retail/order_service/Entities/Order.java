package com.retail.order_service.Entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @Positive(message = "customer Id must be a positive Integer!")
    @NotNull(message = "customer Id cannot be empty!")
    private Integer customerId;

    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> items = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Order() {}

    public Order(Integer customerId) {
        this.customerId = customerId;
        this.totalAmount = BigDecimal.ZERO;
    }

    public Order(Integer customerId,OrderStatus status,List<OrderItem> incoming_items){
        this.customerId = customerId;
        this.totalAmount = BigDecimal.ZERO;
        this.status = status;

        if (incoming_items != null) {
            for(OrderItem item : incoming_items){
                addOrderItem(item);
            }
        }
    }

    public void addOrderItem(OrderItem item){
        if (item != null){
            items.add(item);
            item.setOrder(this);
            BigDecimal itemTotal = item.calculateItemTotal();
            this.totalAmount = this.totalAmount.add(itemTotal);
        }
    }

    public void removeOrderItem(OrderItem item) {
        if (item != null){
            items.remove(item);
            item.setOrder(null);
            BigDecimal itemTotal = item.calculateItemTotal();
            this.totalAmount = this.totalAmount.subtract(itemTotal);
        }
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customer_id) { this.customerId = customer_id; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public BigDecimal getTotalAmount() { return totalAmount; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public List<OrderItem> getItems() { return items; }
}

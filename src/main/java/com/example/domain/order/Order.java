package com.example.domain.order;

import lombok.Value;

import java.util.UUID;

@Value
public class Order {
    UUID id;
    String customerId;
    OrderStatus status;
    Money totalAmount;

    public Order(String customerId, Money totalAmount) {
        this.id = UUID.randomUUID();
        this.customerId = customerId;
        this.status = OrderStatus.CREATED;
        this.totalAmount = totalAmount;
    }

    private Order(UUID id, String customerId, OrderStatus status, Money totalAmount) {
        this.id = id;
        this.customerId = customerId;
        this.status = status;
        this.totalAmount = totalAmount;
    }

    public Order confirm() {
        if (status != OrderStatus.CREATED) {
            throw new IllegalStateException("Order can only be confirmed when in CREATED status");
        }
        return new Order(id, customerId, OrderStatus.CONFIRMED, totalAmount);
    }
} 
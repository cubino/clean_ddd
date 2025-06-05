package com.example.application.order;

import com.example.domain.order.Money;
import com.example.domain.order.Order;
import com.example.infrastructure.persistence.InMemoryOrderRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class CreateOrderUseCase {
    private final InMemoryOrderRepository orderRepository;

    public Order execute(String customerId, BigDecimal amount, String currency) {
        Money totalAmount = Money.of(amount, currency);
        Order order = new Order(customerId, totalAmount);
        return orderRepository.save(order);
    }
} 
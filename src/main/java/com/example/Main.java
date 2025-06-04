package com.example;

import com.example.application.order.ConfirmOrderUseCase;
import com.example.application.order.CreateOrderUseCase;
import com.example.domain.order.Order;
import com.example.infrastructure.persistence.InMemoryOrderRepository;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        // Инициализация репозитория
        InMemoryOrderRepository orderRepository = new InMemoryOrderRepository();

        // Создание юзкейсов
        CreateOrderUseCase createOrderUseCase = new CreateOrderUseCase(orderRepository);
        ConfirmOrderUseCase confirmOrderUseCase = new ConfirmOrderUseCase(orderRepository);

        // Создание заказа
        Order order = createOrderUseCase.execute(
            "customer123",
            new BigDecimal("100.00"),
            "USD"
        );
        System.out.println("Created order: " + order);

        // Подтверждение заказа
        Order confirmedOrder = confirmOrderUseCase.execute(order.getId());
        System.out.println("Confirmed order: " + confirmedOrder);
    }
} 
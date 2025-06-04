package com.example.domain.order;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class OrderDomainService {
    private final OrderRepository orderRepository;

    /**
     * Проверяет, может ли клиент создать новый заказ
     * на основе его предыдущих заказов
     */
    public boolean canCustomerCreateOrder(String customerId) {
        List<Order> customerOrders = orderRepository.findByCustomerId(customerId);
        
        // Проверяем, нет ли у клиента неоплаченных заказов
        boolean hasUnpaidOrders = customerOrders.stream()
            .anyMatch(order -> order.getStatus() == OrderStatus.CREATED);
            
        // Проверяем, не превышен ли лимит заказов
        boolean exceedsOrderLimit = customerOrders.size() >= 10;
        
        return !hasUnpaidOrders && !exceedsOrderLimit;
    }

    /**
     * Рассчитывает скидку для заказа на основе
     * истории заказов клиента
     */
    public Money calculateDiscount(String customerId, Money orderAmount) {
        List<Order> customerOrders = orderRepository.findByCustomerId(customerId);
        
        // Если у клиента больше 5 заказов, даем скидку 10%
        if (customerOrders.size() > 5) {
            BigDecimal discountAmount = orderAmount.getAmount()
                .multiply(new BigDecimal("0.1"));
            return Money.of(discountAmount, orderAmount.getCurrency());
        }
        
        return Money.of(BigDecimal.ZERO, orderAmount.getCurrency());
    }
} 
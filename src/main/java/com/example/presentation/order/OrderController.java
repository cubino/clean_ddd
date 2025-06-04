package com.example.presentation.order;

import com.example.application.order.ConfirmOrderUseCase;
import com.example.application.order.CreateOrderUseCase;
import com.example.domain.order.Order;
import com.example.presentation.order.dto.CreateOrderRequest;
import com.example.presentation.order.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;
    private final ConfirmOrderUseCase confirmOrderUseCase;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        Order order = createOrderUseCase.execute(
            request.getCustomerId(),
            request.getAmount(),
            request.getCurrency()
        );
        return ResponseEntity.ok(OrderResponse.fromDomain(order));
    }

    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<OrderResponse> confirmOrder(@PathVariable UUID orderId) {
        Order order = confirmOrderUseCase.execute(orderId);
        return ResponseEntity.ok(OrderResponse.fromDomain(order));
    }
} 
package com.example.presentation.order.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateOrderRequest {
    private String customerId;
    private BigDecimal amount;
    private String currency;
} 
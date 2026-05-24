package com.sopro.project_demoday.dto;

import java.math.BigDecimal;

public record CheckoutDTO(
        String plano,
        BigDecimal valor,
        String formaPagamento,
        String transactionId
) {}
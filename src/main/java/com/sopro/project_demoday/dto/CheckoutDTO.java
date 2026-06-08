package com.sopro.project_demoday.dto;

import java.math.BigDecimal;

public record CheckoutDTO(
        //  Assinatura
        String plano,
        BigDecimal valorPlano,

        // Dados do Hardware
        boolean incluiDispositivo,
        BigDecimal valorDispositivo,
        String produtoDescricao,

        // Dados financeiros gerais
        BigDecimal valorTotal,
        String formaPagamento,
        String transactionId,

        // Dados do endereço preenchidos na hora da compra
        String cep,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado
) {}
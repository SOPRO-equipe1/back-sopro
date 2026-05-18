package com.sopro.project_demoday.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PerfilResponseDTO(
        String nomeCompleto,
        String plano,
        String cidadeEstado,
        String email,
        String cpf,
        String telefoneCelular,
        LocalDate dataNascimento,
        String enderecoCompleto,
        UltimoPedidoDTO ultimoPedido
) {
    // Sub-DTO aninhado especificamente para encapsular a caixinha do pedido na tela do SOPRO
    public record UltimoPedidoDTO(
            String codigoPedido,
            String produtoDescricao,
            String status,
            String codigoRastreio,
            LocalDate dataEntregaPrevista,
            BigDecimal valorTotal
    ) {}
}
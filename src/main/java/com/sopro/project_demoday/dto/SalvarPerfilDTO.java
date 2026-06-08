package com.sopro.project_demoday.dto;

import java.time.LocalDate;

public record SalvarPerfilDTO(
        // Campos do Botão 1 (Dados Pessoais)
        String nomeCompleto,
        String cpf,
        String telefoneCelular,
        LocalDate dataNascimento,
        String cidadeEstado,

        // Campos do Botão 2 (Endereço Completo de Entrega)
        String cep,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado
) {}
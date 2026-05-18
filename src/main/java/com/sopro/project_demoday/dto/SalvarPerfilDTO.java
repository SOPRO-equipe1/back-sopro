package com.sopro.project_demoday.dto;

import java.time.LocalDate;

public record SalvarPerfilDTO(
        String nomeCompleto,
        String cpf,
        String telefoneCelular,
        LocalDate dataNascimento,
        String cidadeEstado,
        String logradouro,
        String complemento
) {}
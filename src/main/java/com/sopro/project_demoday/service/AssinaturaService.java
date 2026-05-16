package com.sopro.project_demoday.service;

import com.sopro.project_demoday.model.Assinatura;
import com.sopro.project_demoday.model.StatusAssinatura;
import com.sopro.project_demoday.repository.AssinaturaRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class AssinaturaService {

    private final AssinaturaRepository assinaturaRepository;

    // Injeção de dependência do repositório via construtor
    public AssinaturaService(AssinaturaRepository assinaturaRepository) {
        this.assinaturaRepository = assinaturaRepository;
    }

    /**
     * Verifica e atualiza o status com base na data de expiração.
     */
    public StatusAssinatura verificarEAtualizarStatus(Assinatura assinatura) {
        LocalDate hoje = LocalDate.now();

        // Se o status já for EXPIRADO, não precisa recalcular
        if (assinatura.getStatus() == StatusAssinatura.EXPIRADO) {
            return assinatura.getStatus();
        }

        // Se a data atual passou da data de expiração, atualiza para EXPIRADO
        if (hoje.isAfter(assinatura.getExpiracao())) {
            assinatura.setStatus(StatusAssinatura.EXPIRADO);
            assinaturaRepository.save(assinatura);
            return StatusAssinatura.EXPIRADO;
        }

        // Se está dentro do prazo e o status original é TRIAL, ele continua em TRIAL
        if (assinatura.getStatus() == StatusAssinatura.TRIAL) {
            return StatusAssinatura.TRIAL;
        }

        // Caso contrário (dentro do prazo e não é trial), está ATIVO
        return StatusAssinatura.ATIVO;
    }
}

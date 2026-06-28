package com.sopro.project_demoday.controller;

import com.sopro.project_demoday.service.FinOpsOptimizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/finops")
public class FinOpsController {

    private final FinOpsOptimizationService optimizationService;

    public FinOpsController(FinOpsOptimizationService optimizationService) {
        this.optimizationService = optimizationService;
    }

    @PostMapping("/optimize")
    public ResponseEntity<Map<String, Object>> forcarOtimizacao(@RequestParam String resourceGroup) {
        // Invoca o serviço para pausar os App Services ociosos
        List<String> appsParados = optimizationService.otimizarAppServicesOciosos(resourceGroup);

        // Métrica de FinOps: Simulação de economia baseada em planos de produção (ex: S1/P1v2)
        // Poupar 60 horas de fim de semana evita desperdício real no orçamento do banco
        double economiaEstimadaFimDeSemanaUSD = appsParados.size() * 12.50;

        return ResponseEntity.ok(Map.of(
                "status", "Sucesso",
                "tipoRecurso", "Azure App Service (Pacote Clássico)",
                "mensagem", "Varredura de infraestrutura concluída com sucesso.",
                "aplicacoesOtimizadas", appsParados,
                "economiaEstimadaFimDeSemanaUSD", economiaEstimadaFimDeSemanaUSD
        ));
    }
}
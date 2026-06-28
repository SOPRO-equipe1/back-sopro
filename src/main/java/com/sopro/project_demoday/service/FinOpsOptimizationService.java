package com.sopro.project_demoday.service;

import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.appservice.WebApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FinOpsOptimizationService {

    private static final Logger logger = LoggerFactory.getLogger(FinOpsOptimizationService.class);
    private final AzureAuthService azureAuthService;


    @Value("${finops.simulation.mode:true}")
    private boolean simulationMode;

    public FinOpsOptimizationService(AzureAuthService azureAuthService) {
        this.azureAuthService = azureAuthService;
    }

    public List<String> otimizarAppServicesOciosos(String resourceGroupName) {
        List<String> appsOtimizados = new ArrayList<>();

        if (simulationMode) {
            logger.info("[MODO SIMULAÇÃO FINOPS] Iniciando varredura no Resource Group: {}", resourceGroupName);
            logger.warn("[MODO SIMULAÇÃO] Verificando microsserviços com a tag 'Environment: Development'...");

            // Simulando que encontrou 2 App Services de Dev ativos no projeto SOPRO
            try {
                Thread.sleep(1500); // Simula o tempo de resposta da nuvem
                logger.warn("[MODO SIMULAÇÃO] App Service ativo detectado: sopro-api-dev. Parando serviço...");
                appsOtimizados.add("sopro-api-dev");

                Thread.sleep(1000);
                logger.warn("[MODO SIMULAÇÃO] App Service ativo detectado: sopro-web-dev. Parando serviço... ");
                appsOtimizados.add("sopro-web-dev");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            logger.info("[MODO SIMULAÇÃO] Varredura concluída. Total de aplicações pausadas: {}", appsOtimizados.size());
            return appsOtimizados;
        }


        try {
            Azure azure = azureAuthService.getAzureClient();
            logger.info("Iniciando varredura FinOps REAL em App Services no Resource Group: {}", resourceGroupName);

            for (WebApp webApp : azure.webApps().listByResourceGroup(resourceGroupName)) {
                try {
                    String envTag = webApp.tags().get("Environment");

                    if ("Development".equalsIgnoreCase(envTag) || "Test".equalsIgnoreCase(envTag)) {
                        if ("Running".equalsIgnoreCase(webApp.state())) {
                            logger.warn("App Service ativo detectado: {}. Parando serviço...", webApp.name());
                            webApp.stop();
                            appsOtimizados.add(webApp.name());
                        }
                    }
                } catch (Exception appException) {
                    logger.error("Erro no App Service {}: {}", webApp.name(), appException.getMessage());
                }
            }
        } catch (Exception e) {
            logger.error("Erro geral no Resource Group de FinOps: {}", e.getMessage(), e);
        }

        return appsOtimizados;
    }
}
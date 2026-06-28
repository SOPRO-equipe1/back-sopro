package com.sopro.project_demoday.service;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import com.microsoft.azure.management.Azure;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AzureAuthService {

    @Value("${azure.tenant.id}")
    private String tenantId;

    @Value("${azure.subscription.id}")
    private String subscriptionId;


    private String clientId = System.getenv("AZURE_CLIENT_ID");
    private String clientSecret = System.getenv("AZURE_CLIENT_SECRET");

    public Azure getAzureClient() {
        ApplicationTokenCredentials credentials = new ApplicationTokenCredentials(
                clientId, tenantId, clientSecret, AzureEnvironment.AZURE);

        return Azure.configure()
                .authenticate(credentials)
                .withSubscription(subscriptionId);
    }
}
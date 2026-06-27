package com.sopro.project_demoday.service;

import com.azure.core.credential.TokenCredential;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.AzureResourceManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AzureAuthService {

    @Value("${azure.tenant.id}")
    private String tenantId;

    @Value("${azure.subscription.id}")
    private String subscriptionId;

    public AzureResourceManager getAzureClient() {
        TokenCredential credential = new DefaultAzureCredentialBuilder().build();
        AzureProfile profile = new AzureProfile(tenantId, subscriptionId, AzureEnvironment.AZURE);

        return AzureResourceManager.configure()
                .authenticate(credential, profile)
                .withSubscription(subscriptionId);
    }
}
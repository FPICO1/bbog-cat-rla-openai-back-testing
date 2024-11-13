package com.banbta.bbogcatrlaopenaibacktesting.application.config;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.banbta.bbogcatrlaopenaibacktesting.application.services.SecretManagerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureOpenAiClientConfig {

    private final SecretManagerService secretManagerService;

    public AzureOpenAiClientConfig(SecretManagerService secretManagerService) {
        this.secretManagerService = secretManagerService;
    }

    @Bean
    public OpenAIClient openAIClient() {
        return new OpenAIClientBuilder()
                .endpoint(secretManagerService.getEndpoint())
                .credential(new AzureKeyCredential(secretManagerService.getApiKey()))
                .buildClient();
    }
}
package com.banbta.bbogcatrlaopenaibacktesting.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://znr46ipcj3.execute-api.us-east-1.amazonaws.com/qa") // URL base de tu API Gateway
                .build();
    }
}
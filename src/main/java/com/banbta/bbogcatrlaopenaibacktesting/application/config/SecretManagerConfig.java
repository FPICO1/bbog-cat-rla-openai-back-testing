package com.banbta.bbogcatrlaopenaibacktesting.application.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software. amazon. awssdk. regions. Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;

@Configuration
public class SecretManagerConfig {

    String secretName = "prod/itest/azureIA";


    @Bean
    public SecretsManagerClient secretsManagerClient() {
        return  SecretsManagerClient.builder()
                .region(Region.of("us-east-1"))
                .build();

    }

    @Bean
    public GetSecretValueRequest getSecretValueRequest() {
        return GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();
    }



}
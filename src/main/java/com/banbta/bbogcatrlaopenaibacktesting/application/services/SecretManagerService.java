package com.banbta.bbogcatrlaopenaibacktesting.application.services;

import com.banbta.bbogcatrlaopenaibacktesting.application.config.SecretManagerConfig;
import com.banbta.bbogcatrlaopenaibacktesting.common.LinkSecretManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.io.IOException;
import java.util.Map;

@Getter
@Service
public class SecretManagerService {

    private final SecretsManagerClient secretsManagerClient;
    private final SecretManagerConfig secretManagerConfig;
    private final ObjectMapper objectMapper;
    private String apiKey;
    private String endpoint;


    @Autowired
    public SecretManagerService(SecretsManagerClient secretsManagerClient,
                                SecretManagerConfig secretManagerConfig,
                                ObjectMapper objectMapper) {
        this.secretsManagerClient = secretsManagerClient;
        this.secretManagerConfig = secretManagerConfig;
        this.objectMapper = objectMapper;
        loadSecrets();
    }

    private void loadSecrets() {
        try {

            GetSecretValueResponse secretValueResponse =
                    secretsManagerClient.getSecretValue(secretManagerConfig.getSecretValueRequest(LinkSecretManager.SECRET_NAME_AZURE_IA));


            String secretJson = secretValueResponse.secretString();
            Map<String, String> secretsMap = objectMapper.readValue(secretJson, Map.class);

            //Produccion
            this.apiKey = secretsMap.get("AZURE_OPENAI_API_KEY");
            this.endpoint = secretsMap.get("AZURE_OPENAI_ENDPOINT");

        } catch (IOException e) {
            throw new RuntimeException("Error loading secrets from AWS Secrets Manager", e);
        }
    }



}

package com.banbta.bbogcatrlaopenaibacktesting.application.services;

import com.banbta.bbogcatrlaopenaibacktesting.application.dto.request.GetFileJsonRequestDTO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
@Slf4j
public class GetFileJsonService {

    private final WebClient webClient;
    private final Gson gson = new Gson();

    // Endpoint del API Gateway
    private static final String PATH = "/downloadFileJson";

    @Autowired
    public GetFileJsonService(WebClient webClient) {
        this.webClient = webClient;
    }

    public JsonObject downloadFileJson(GetFileJsonRequestDTO requestDTO) {
        try {
            String responseBody = webClient.post()
                    .uri(PATH)
                    .bodyValue(requestDTO)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("Respuesta del API Gateway: {}", responseBody);

            if (responseBody == null || responseBody.isEmpty()) {
                log.error("Error: El cuerpo de respuesta está vacío o es nulo");
                return new JsonObject();
            }

            JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);

            // Verifica si jsonResponse contiene 'body'
            if (jsonResponse == null || !jsonResponse.has("body")) {
                log.error("Error: El cuerpo de respuesta no tiene el formato esperado");
                return new JsonObject();
            }

            String bodyContentStr = jsonResponse.get("body").getAsString();
            JsonObject bodyContent = gson.fromJson(bodyContentStr, JsonObject.class);

            return bodyContent;

        } catch (Exception e) {
            log.error("Error al consumir el endpoint del API Gateway: {}", e.getMessage());
            return null;
        }
    }
}
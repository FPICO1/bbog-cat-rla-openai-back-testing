package com.banbta.bbogcatrlaopenaibacktesting.application.services;


import com.banbta.bbogcatrlaopenaibacktesting.application.dto.request.GetFileJsonRequestDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GetFileJsonService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public GetFileJsonService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl("https://znr46ipcj3.execute-api.us-east-1.amazonaws.com/qa").build();
        this.objectMapper = objectMapper;
    }

    public Mono<JsonNode> getFileJson(GetFileJsonRequestDTO request) {
        return webClient.post()
                .uri("/downloadFileJson")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    try {
                        // Convertir el response completo a un JsonNode
                        JsonNode jsonResponse = objectMapper.readTree(response);

                        // Verificar si "body" está presente y no es null
                        if (jsonResponse.has("body") && !jsonResponse.get("body").isNull()) {
                            // Obtener el contenido de "body" como String
                            String bodyString = jsonResponse.get("body").asText();
                            // Convertir el contenido del "body" de String a JsonNode
                            return objectMapper.readTree(bodyString);
                        } else {
                            // Loguear y lanzar excepción si no está "body"
                            System.out.println("Respuesta completa del API Gateway (sin 'body' o null): " + response);
                            throw new IllegalArgumentException("El campo 'body' no está presente o es null en la respuesta.");
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("Error procesando el response", e);
                    }
                });
    }
}

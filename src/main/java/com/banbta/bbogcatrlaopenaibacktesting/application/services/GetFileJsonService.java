package com.banbta.bbogcatrlaopenaibacktesting.application.services;

import com.banbta.bbogcatrlaopenaibacktesting.application.dto.request.GetFileJsonRequestDTO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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

    public Mono<JsonObject> downloadFileJson(GetFileJsonRequestDTO requestDTO) {
        return webClient.post()
                .uri("/downloadFileJson") // Asegúrate de que esta URI sea correcta
                .bodyValue(requestDTO) // Enviar el requestDTO como cuerpo
                .retrieve()
                .bodyToMono(String.class)
                .map(responseBody -> {
                    log.info("Respuesta del API Gateway: {}", responseBody);
                    System.out.println("Respuesta del API Gateway: {}" + responseBody);
                    return gson.fromJson(responseBody, JsonObject.class);
                })
                .doOnError(e -> log.error("Error al consumir el endpoint del API Gateway: {}", e.getMessage()));
    }


}

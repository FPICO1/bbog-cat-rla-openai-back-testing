package com.banbta.bbogcatrlaopenaibacktesting.web.functions;

import com.banbta.bbogcatrlaopenaibacktesting.application.dto.request.GetFileJsonRequestDTO;
import com.banbta.bbogcatrlaopenaibacktesting.application.dto.response.GetFileJsonResponseDTO;
import com.banbta.bbogcatrlaopenaibacktesting.application.services.GetFileJsonService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
public class GetFileJsonFunction {

    private final GetFileJsonService getFileJsonService;
    private final Gson gson = new Gson();

    @Autowired
    public GetFileJsonFunction(GetFileJsonService getFileJsonService) {
        this.getFileJsonService = getFileJsonService;
    }

    @Bean
    public Function<GetFileJsonRequestDTO, Mono<GetFileJsonResponseDTO>> downloadFileJson() {
        return requestDTO -> {
            GetFileJsonResponseDTO responseDTO = new GetFileJsonResponseDTO();
            responseDTO.setHeaders(Map.of("Content-Type", "application/json")); // Definir encabezados

            System.out.println("Request DTO: " + gson.toJson(requestDTO)); // Log para verificar el requestDTO

            if (requestDTO == null) {
                responseDTO.setBody("{\"message\": \"Error: No request data found\"}");
                responseDTO.setStatusCode(400);
                return Mono.just(responseDTO);
            } else {
                return getFileJsonService.downloadFileJson(requestDTO)
                        .map(jsonObject -> {
                            if (jsonObject != null) {
                                responseDTO.setBody(gson.toJson(jsonObject));
                                responseDTO.setStatusCode(200);
                            } else {
                                responseDTO.setBody("{\"message\": \"Error: No data returned from API Gateway\"}");
                                responseDTO.setStatusCode(404);
                            }
                            return responseDTO; // Retornar el responseDTO
                        })
                        .onErrorResume(error -> {
                            responseDTO.setBody("{\"message\": \"Error: " + error.getMessage() + "\"}");
                            responseDTO.setStatusCode(500);
                            return Mono.just(responseDTO); // Retornar el responseDTO
                        });
            }
        };
    }


}

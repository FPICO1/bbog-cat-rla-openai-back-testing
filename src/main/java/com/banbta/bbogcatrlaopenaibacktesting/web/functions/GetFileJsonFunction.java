package com.banbta.bbogcatrlaopenaibacktesting.web.functions;

import com.banbta.bbogcatrlaopenaibacktesting.application.dto.request.GetFileJsonRequestDTO;
import com.banbta.bbogcatrlaopenaibacktesting.application.services.GetFileJsonService;
import com.google.gson.Gson;
import com.fasterxml.jackson.databind.JsonNode;  // Importa JsonNode de Jackson
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
public class GetFileJsonFunction {

    private final GetFileJsonService getFileJsonService;
    private final Gson gson;

    public GetFileJsonFunction(GetFileJsonService getFileJsonService) {
        this.getFileJsonService = getFileJsonService;
        this.gson = new Gson();
    }

    @Bean
    public Function<String, Mono<JsonNode>> getFileJson() {  // Cambia el retorno a Mono<JsonNode>
        return input -> {
            // Convierte el JSON de entrada (String) en GetFileJsonRequestDTO
            GetFileJsonRequestDTO request = gson.fromJson(input, GetFileJsonRequestDTO.class);
            // Llama al servicio con el objeto GetFileJsonRequestDTO
            return getFileJsonService.getFileJson(request);
        };
    }
}

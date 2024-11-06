package com.banbta.bbogcatrlaopenaibacktesting;

import com.banbta.bbogcatrlaopenaibacktesting.application.dto.request.GetFileJsonRequestDTO;
import com.banbta.bbogcatrlaopenaibacktesting.application.dto.response.GetFileJsonResponseDTO;
import com.banbta.bbogcatrlaopenaibacktesting.web.functions.GetFileJsonFunction;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Map;
import java.util.function.Function;

@SpringBootApplication
public class BbogCatRlaOpenaiBackTestingApplication {

    private final GetFileJsonFunction getFileJsonFunction;

    @Autowired
    public BbogCatRlaOpenaiBackTestingApplication(GetFileJsonFunction getFileJsonFunction) {
        this.getFileJsonFunction = getFileJsonFunction;
    }


    public static void main(String[] args) {
        SpringApplication.run(BbogCatRlaOpenaiBackTestingApplication.class, args);
    }

    @Bean
    public Function<Map<String, Object>, Map<String, Object>> route() {
        Gson gson = new Gson();

        return event -> {
            String path = (String) event.get("resource");

            if (path.equalsIgnoreCase("/downloadFileJson")) {
                String body = (String) event.get("body");
                GetFileJsonRequestDTO requestDTO = gson.fromJson(body, GetFileJsonRequestDTO.class);
                GetFileJsonResponseDTO responseDTO = getFileJsonFunction.downloadFileJson().apply(requestDTO).block();

                assert responseDTO != null;
                return Map.of(
                        "statusCode", responseDTO.getStatusCode(),
                        "headers", responseDTO.getHeaders(),
                        "body", responseDTO.getBody()
                );
            }

            return Map.of("message", "path no existente");
        };
    }
}
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

            if ("/downloadFileJson".equalsIgnoreCase(path)) {
                String body = (String) event.get("body");

                if (body == null) {
                    return Map.of("statusCode", 400, "body", "{\"message\": \"Error: Body is null\"}");
                }

                GetFileJsonRequestDTO requestDTO = gson.fromJson(body, GetFileJsonRequestDTO.class);

                GetFileJsonResponseDTO responseDTO = getFileJsonFunction.downloadFileJson().apply(requestDTO);

                if (responseDTO != null) {
                    return Map.of(
                            "statusCode", responseDTO.getStatusCode(),
                            "headers", responseDTO.getHeaders(),
                            "body", responseDTO.getBody()
                    );
                } else {
                    return Map.of("statusCode", 500, "body", "{\"message\": \"Error: Response DTO is null\"}");
                }
            }

            return Map.of("statusCode", 404, "body", "{\"message\": \"Path not found\"}");
        };
    }

}

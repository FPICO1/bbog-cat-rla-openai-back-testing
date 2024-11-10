package com.banbta.bbogcatrlaopenaibacktesting.application.services;

import com.banbta.bbogcatrlaopenaibacktesting.application.common.EndPointPaths;
import com.banbta.bbogcatrlaopenaibacktesting.application.dto.request.DataRequestDTO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
@Slf4j
public class ConsumeLambdaDownloadS3Service {


    private final WebClient webClient;
    private final Gson gson;

    @Autowired
    public ConsumeLambdaDownloadS3Service(WebClient webClient,Gson gson) {
        this.webClient = webClient;
        this.gson = gson;
    }


    public JsonObject getDataJson(DataRequestDTO dataRequestDTO) {

//        String body = String.format("{\"body\": \"{\\\"userName\\\": \\\"%s\\\", \\\"moduleName\\\": \\\"%s\\\"}\"}",
//                dataRequestDTO.getUserName(),
//                dataRequestDTO.getModuleName());

        String body = String.format("{\"resource\": \"/downloadFileJson\", \"httpMethod\": \"POST\", \"body\": \"{\\\"userName\\\": \\\"%s\\\", \\\"moduleName\\\": \\\"%s\\\"}\", \"isBase64Encoded\": false}",
                dataRequestDTO.getUserName(),
                dataRequestDTO.getModuleName());


        try {

            String responseBody = webClient.post()
                    .uri(EndPointPaths.LAMBDA_DOWNLOAD_DATA_FILE_S3)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();


            log.info("Respuesta del API Gateway: {}", responseBody);

            if (responseBody == null || responseBody.isEmpty()) {
                log.error("Error: El cuerpo de respuesta está vacío o es nulo");
                return gson.fromJson("Mensaje error : El cuerpo de respuesta está vacío o es nulo" , JsonObject.class);
            }

            JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);

            // Verifica si jsonResponse contiene 'body'
            if (jsonResponse == null || !jsonResponse.has("body")) {
                log.error("Error: El cuerpo de respuesta no tiene el formato esperado");
                return gson.fromJson("Mensaje error :  El cuerpo de respuesta no tiene el formato esperado" , JsonObject.class);
            }

            String bodyContentStr = jsonResponse.get("body").getAsString();

            return gson.fromJson(bodyContentStr, JsonObject.class);

        } catch (Exception e) {
            log.error("Error al consumir el endpoint del API Gateway: {}", e.getMessage());
            return gson.fromJson("Mensage : Error al consumir el endpoint del API Gateway: {}" + e.getMessage(), JsonObject.class);
        }
    }
}
package com.banbta.bbogcatrlaopenaibacktesting.application.services;

import com.banbta.bbogcatrlaopenaibacktesting.application.dto.request.DataRequestDTO;
import com.banbta.bbogcatrlaopenaibacktesting.common.LambdaNames;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaAsyncClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

import java.util.concurrent.ExecutionException;


@Service
@Slf4j
public class ConsumeLambdaDownloadS3Service {

    private final LambdaAsyncClient lambdaAsyncClient;
    private final Gson gson;

    @Autowired
    public ConsumeLambdaDownloadS3Service(LambdaAsyncClient lambdaAsyncClient, Gson gson) {
        this.lambdaAsyncClient = lambdaAsyncClient;
        this.gson = gson;
    }

    public JsonObject getDataJson(DataRequestDTO dataRequestDTO) {
        // Crear el cuerpo de la solicitud
        String body = String.format(
                "{\"resource\": \"/downloadFileJson\", \"httpMethod\": \"POST\", \"body\": \"{\\\"userName\\\": \\\"%s\\\", \\\"moduleName\\\": \\\"%s\\\"}\", \"isBase64Encoded\": false}",
                dataRequestDTO.getUserName(),
                dataRequestDTO.getModuleName()
        );

        log.info("Este es el body: {}", body);

        // Construir la solicitud InvokeRequest
        InvokeRequest request = InvokeRequest.builder()
                .functionName(LambdaNames.LAMBDA_DOWNLOAD_DATA_FILE_S3) // Reemplaza con el nombre de tu función Lambda
                .payload(SdkBytes.fromUtf8String(body)) // Cuerpo de la solicitud
                .build();

        try {
            // Invocar la función Lambda de forma sincrónica
            InvokeResponse response = lambdaAsyncClient.invoke(request).get();

            // Obtener el cuerpo de la respuesta
            String responseBody = response.payload().asUtf8String();
            log.info("Respuesta de Lambda: {}", responseBody);

            if (responseBody == null || responseBody.isEmpty()) {
                log.error("Error: El cuerpo de respuesta está vacío o es nulo");
                return crearErrorJson("El cuerpo de respuesta está vacío o es nulo");
            }

            // Deserializar responseBody a JsonObject
            JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);

            if (jsonResponse == null) {
                log.error("Error: No se pudo parsear responseBody a JsonObject");
                return crearErrorJson("No se pudo parsear responseBody a JsonObject");
            }

            // Extraer el campo "body" y deserializarlo
            if (jsonResponse.has("body")) {
                String bodyContentStr = jsonResponse.get("body").getAsString();

                // Deserializar el contenido de 'body' que es una cadena JSON
                JsonObject bodyJson = gson.fromJson(bodyContentStr, JsonObject.class);
                return bodyJson;
            } else {
                log.error("Error: La respuesta no contiene el campo 'body'");
                return crearErrorJson("La respuesta no contiene el campo 'body'");
            }

        } catch (InterruptedException | ExecutionException e) {
            log.error("Error al invocar la función Lambda: {}", e.getMessage());
            return crearErrorJson("Error al invocar la función Lambda: " + e.getMessage());
        } catch (JsonSyntaxException e) {
            log.error("Error al parsear la respuesta de Lambda: {}", e.getMessage());
            return crearErrorJson("Error al parsear la respuesta de Lambda");
        } catch (Exception e) {
            log.error("Error inesperado al procesar la respuesta de Lambda: {}", e.getMessage());
            return crearErrorJson("Error inesperado al procesar la respuesta de Lambda");
        }
    }

    private JsonObject crearErrorJson(String mensaje) {
        JsonObject errorJson = new JsonObject();
        errorJson.addProperty("error", mensaje);
        return errorJson;
    }
}
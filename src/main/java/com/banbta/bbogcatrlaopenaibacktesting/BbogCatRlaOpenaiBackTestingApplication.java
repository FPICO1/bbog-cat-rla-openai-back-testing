package com.banbta.bbogcatrlaopenaibacktesting;

import com.banbta.bbogcatrlaopenaibacktesting.application.config.GsonConfig;
import com.banbta.bbogcatrlaopenaibacktesting.application.dto.request.DataBdDynamoRequestDTO;
import com.banbta.bbogcatrlaopenaibacktesting.application.dto.response.ApiResponse;
import com.banbta.bbogcatrlaopenaibacktesting.common.EndPointPaths;
import com.banbta.bbogcatrlaopenaibacktesting.application.dto.request.DataRequestDTO;
import com.banbta.bbogcatrlaopenaibacktesting.application.dto.response.GenerateReportResponseDTO;
import com.banbta.bbogcatrlaopenaibacktesting.web.functions.GenerateReportFunction;
import com.banbta.bbogcatrlaopenaibacktesting.web.functions.OperationBdDynamoFunction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.util.Map;
import java.util.function.Function;

@SpringBootApplication
public class BbogCatRlaOpenaiBackTestingApplication {

    private final GenerateReportFunction generateReportFunction;
    private final OperationBdDynamoFunction operationBdDynamoFunction;
    private final Gson gson;

    @Autowired
    public BbogCatRlaOpenaiBackTestingApplication(GenerateReportFunction generateReportFunction, OperationBdDynamoFunction operationBdDynamoFunction, Gson gson) {
        this.generateReportFunction = generateReportFunction;
        this.operationBdDynamoFunction = operationBdDynamoFunction;
        this.gson = gson;
    }

    public static void main(String[] args) {


        SpringApplication.run(BbogCatRlaOpenaiBackTestingApplication.class, args);
    }

    @Bean
    public Function<Map<String, Object>, Map<String, Object>> route() {
        return event -> {
            String path = (String) event.get("resource");

            Map<String, Object> responseMap = Map.of("statusCode", 404, "body", "Ruta no encontrada");

            try {
                if (path.equalsIgnoreCase(EndPointPaths.GENERATE_REPORT)) {

                    String body = (String) event.get("body");

                     // Extraer el cuerpo del event
                    DataRequestDTO dataRequestDTO = gson.fromJson(body, DataRequestDTO.class);

                    GenerateReportResponseDTO responseDTO = generateReportFunction.generateReport().apply(dataRequestDTO);

                    String jsonBody = responseDTO.getBody();

                    // Verificar y limpiar comillas de inicio y fin
                    if (jsonBody.startsWith("\"") && jsonBody.endsWith("\"")) {
                        // Eliminar las comillas de inicio y fin y reemplazar \" con "
                        jsonBody = jsonBody.substring(1, jsonBody.length() - 1).replace("\\\"", "\"");
                    }

                    System.out.println("despues del arreglos jsonBody : " + jsonBody);

                    return gson.fromJson(responseDTO.getBody(), new TypeToken<Map<String, Object>>() {
                    }.getType());

                } else     if (path.equalsIgnoreCase(EndPointPaths.FIND_REPORTS_BY_HU)) {
                    String body = (String) event.get("body");

                    // Deserializar el cuerpo de la solicitud
                    DataBdDynamoRequestDTO dataBdDynamoRequestDTO = gson.fromJson(body, DataBdDynamoRequestDTO.class);

                    // Llamar a la función y obtener la respuesta
                    GenerateReportResponseDTO responseDTO = operationBdDynamoFunction.findReportsByHistoryUser().apply(dataBdDynamoRequestDTO);

                    // Deserializar el cuerpo de la respuesta en ApiResponse
                    ApiResponse<?> apiResponse = gson.fromJson(responseDTO.getBody(), ApiResponse.class);

                    // Convertir ApiResponse a Map para la respuesta
                    responseMap = gson.fromJson(responseDTO.getBody(), new TypeToken<Map<String, Object>>() {}.getType());

                    // Puedes agregar lógica adicional aquí si es necesario
                }
            } catch (Exception e) {
                System.err.println("Error al procesar la solicitud: " + e.getMessage());
                responseMap = Map.of("statusCode", 500, "body", "Error interno del servidor");
            }

            return responseMap;
        };


    }


}

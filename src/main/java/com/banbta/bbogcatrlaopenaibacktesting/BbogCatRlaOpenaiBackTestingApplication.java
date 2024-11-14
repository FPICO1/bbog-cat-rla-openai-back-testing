package com.banbta.bbogcatrlaopenaibacktesting;

import com.banbta.bbogcatrlaopenaibacktesting.common.EndPointPaths;
import com.banbta.bbogcatrlaopenaibacktesting.application.dto.request.DataRequestDTO;
import com.banbta.bbogcatrlaopenaibacktesting.application.dto.response.GenerateReportResponseDTO;
import com.banbta.bbogcatrlaopenaibacktesting.web.functions.GenerateReportFunction;
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
    private final Gson gson;

    @Autowired
    public BbogCatRlaOpenaiBackTestingApplication(GenerateReportFunction generateReportFunction, Gson gson) {
        this.generateReportFunction = generateReportFunction;
        this.gson = gson;
    }

    public static void main(String[] args) {


        SpringApplication.run(BbogCatRlaOpenaiBackTestingApplication.class, args);
    }

    @Bean
    public Function<Map<String, Object>, Map<String, Object>> route() {
        return event -> {
            String path = (String) event.get("resource");

            if (path.equalsIgnoreCase(EndPointPaths.GENERATE_REPORT)) {

                // Obtener el cuerpo del evento y deserializarlo
                String body = (String) event.get("body"); // Extraer el cuerpo del event
                DataRequestDTO dataRequestDTO = gson.fromJson(body, DataRequestDTO.class);

                GenerateReportResponseDTO responseDTO = generateReportFunction.generateReport().apply(dataRequestDTO);

                String jsonBody = responseDTO.getBody();

                // Verificar y limpiar comillas de inicio y fin
                if (jsonBody.startsWith("\"") && jsonBody.endsWith("\"")) {
                    // Eliminar las comillas de inicio y fin y reemplazar \" con "
                    jsonBody = jsonBody.substring(1, jsonBody.length() - 1).replace("\\\"", "\"");
                }

                System.out.println("despues del arreglos jsonBody : "+jsonBody);


                // Deserializar directamente el JSON en un Map
                //Map<String, Object> bodyJson = gson.fromJson(responseDTO.getBody(), new TypeToken<Map<String, Object>>() {}.getType());


                // Specify the type as Map<String, Object>
                return gson.fromJson(responseDTO.getBody(), new TypeToken<Map<String, Object>>() {}.getType());

            }

            return Map.of("statusCode", 404, "body", "Ruta no encontrada");
        };
    }
}

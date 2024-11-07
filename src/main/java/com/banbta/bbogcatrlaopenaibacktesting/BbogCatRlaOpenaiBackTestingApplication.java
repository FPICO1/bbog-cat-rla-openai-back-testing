package com.banbta.bbogcatrlaopenaibacktesting;

import com.banbta.bbogcatrlaopenaibacktesting.application.common.EndPointPaths;
import com.banbta.bbogcatrlaopenaibacktesting.application.dto.request.GetFileJsonRequestDTO;
import com.banbta.bbogcatrlaopenaibacktesting.application.dto.response.GenerateReportResponseDTO;
import com.banbta.bbogcatrlaopenaibacktesting.application.dto.response.GetFileJsonResponseDTO;
import com.banbta.bbogcatrlaopenaibacktesting.web.functions.GenerateReportFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@SpringBootApplication
public class BbogCatRlaOpenaiBackTestingApplication {

    private final GenerateReportFunction generateReportFunction;

    @Autowired
    public BbogCatRlaOpenaiBackTestingApplication(GenerateReportFunction generateReportFunction) {
        this.generateReportFunction = generateReportFunction;
    }

    public static void main(String[] args) {
        SpringApplication.run(BbogCatRlaOpenaiBackTestingApplication.class, args);
    }

    @Bean
    public Function<Map<String, Object>, Map<String, Object>> route() {
        return event -> {
            String path = (String) event.get("resource");

            if (path.equalsIgnoreCase(EndPointPaths.GENERATE_REPORT)) {
                GenerateReportResponseDTO responseDTO = generateReportFunction.generateReport().get();

                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("statusCode", responseDTO.getStatusCode());
                responseMap.put("headers", responseDTO.getHeaders());
                responseMap.put("body", responseDTO.getBody());
                return responseMap;
            }

            return Map.of("statusCode", 404, "body", "Ruta no encontrada");
        };
    }
}

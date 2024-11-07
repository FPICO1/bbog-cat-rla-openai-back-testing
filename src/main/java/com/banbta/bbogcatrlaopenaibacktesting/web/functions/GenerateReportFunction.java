package com.banbta.bbogcatrlaopenaibacktesting.web.functions;

import com.banbta.bbogcatrlaopenaibacktesting.application.dto.response.GenerateReportResponseDTO;
import com.banbta.bbogcatrlaopenaibacktesting.application.services.GenerateReportService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
@Slf4j
public class GenerateReportFunction {

    private final GenerateReportService generateReportService;
    private final Gson gson = new Gson();

    @Autowired
    public GenerateReportFunction(GenerateReportService generateReportService) {
        this.generateReportService = generateReportService;
    }

    @Bean
    public Supplier<GenerateReportResponseDTO> generateReport() {
        return () -> {
            try {
                Map<String, Object> report = generateReportService.generateReport();
                return new GenerateReportResponseDTO(gson.toJson(report), 200, Map.of("Content-Type", "application/json"));
            } catch (Exception e) {
                log.error("Error al generar el reporte: ", e);
                return new GenerateReportResponseDTO(gson.toJson(Map.of("error", e.getMessage())), 500, Map.of("Content-Type", "application/json"));
            }
        };
    }
}
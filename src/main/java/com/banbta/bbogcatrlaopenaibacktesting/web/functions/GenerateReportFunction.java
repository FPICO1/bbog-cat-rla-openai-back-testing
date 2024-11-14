package com.banbta.bbogcatrlaopenaibacktesting.web.functions;

import com.banbta.bbogcatrlaopenaibacktesting.application.dto.request.DataRequestDTO;
import com.banbta.bbogcatrlaopenaibacktesting.application.dto.response.GenerateReportResponseDTO;
import com.banbta.bbogcatrlaopenaibacktesting.application.dto.response.MessageResponseDTO;
import com.banbta.bbogcatrlaopenaibacktesting.application.services.GenerateReportService;
import com.banbta.bbogcatrlaopenaibacktesting.domain.entitys.ReportAiEntity;
import com.banbta.bbogcatrlaopenaibacktesting.domain.repository.ReportAiRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
public class GenerateReportFunction {


    private final GenerateReportService generateReportService;
    private final Gson gson = new Gson();

    @Autowired
    public GenerateReportFunction( GenerateReportService generateReportService) {
        this.generateReportService = generateReportService;
    }



    @Bean
    public Function<DataRequestDTO, GenerateReportResponseDTO> generateReport()  {

        GenerateReportResponseDTO generateReportResponseDTO = new GenerateReportResponseDTO();


        return dataRequestDTO -> {


            System.out.println("Datos enviados:  "+dataRequestDTO);

            try {

                JsonObject report= generateReportService.generateReport(dataRequestDTO);

                if (report == null){

                    generateReportResponseDTO.setStatusCode(400);
                    generateReportResponseDTO.setHeaders(Map.of("Content-Type", "application/json"));
                    generateReportResponseDTO.setBody(gson.toJson(new MessageResponseDTO("No se genero el reporte de IA")));

                    return generateReportResponseDTO;
                }else{

                    generateReportResponseDTO.setStatusCode(200);
                    generateReportResponseDTO.setHeaders(Map.of("Content-Type", "application/json"));
                    generateReportResponseDTO.setBody(gson.toJson(report));

                    return generateReportResponseDTO;
                }

            } catch (Exception e) {
                log.error("Error al generar el reporte: ", e);

                generateReportResponseDTO.setStatusCode(500);
                generateReportResponseDTO.setHeaders(Map.of("Content-Type", "application/json"));
                generateReportResponseDTO.setBody(gson.toJson(new MessageResponseDTO("Error mayor: "+ e.getMessage())));

                return generateReportResponseDTO;
            }
        };

    }
}


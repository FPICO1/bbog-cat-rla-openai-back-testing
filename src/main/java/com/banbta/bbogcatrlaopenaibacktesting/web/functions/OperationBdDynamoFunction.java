package com.banbta.bbogcatrlaopenaibacktesting.web.functions;

import com.banbta.bbogcatrlaopenaibacktesting.application.dto.request.DataBdDynamoRequestDTO;
import com.banbta.bbogcatrlaopenaibacktesting.application.dto.response.ApiResponse;
import com.banbta.bbogcatrlaopenaibacktesting.application.dto.response.GenerateReportResponseDTO;
import com.banbta.bbogcatrlaopenaibacktesting.application.services.OperationBdDynamoServices;
import com.banbta.bbogcatrlaopenaibacktesting.domain.entitys.ReportAiEntity;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
public class OperationBdDynamoFunction {


    private final OperationBdDynamoServices operationBdDynamoServices;
    private final Gson gson;
    private final GenerateReportResponseDTO responseDTO;

    @Autowired
    public OperationBdDynamoFunction(OperationBdDynamoServices operationBdDynamoServices, Gson gson, GenerateReportResponseDTO responseDTO) {
        this.operationBdDynamoServices = operationBdDynamoServices;
        this.gson = gson;
        this.responseDTO = responseDTO;
    }




    @Bean
    public Function<DataBdDynamoRequestDTO, GenerateReportResponseDTO> findReportsByHistoryUser() {

        return dataBdDynamoRequestDTO -> {

            System.out.println("Consultando reports para historyUser: " + dataBdDynamoRequestDTO.getHistoryUser());

            try {
                List<ReportAiEntity> reports = operationBdDynamoServices.findReportsByHistoryUser(dataBdDynamoRequestDTO.getHistoryUser().toUpperCase());

                ApiResponse<?> apiResponse;

                if (reports.isEmpty()) {
                    apiResponse = new ApiResponse<>(404, null, "No se encontraron reportes para el historyUser especificado");
                } else {
                    apiResponse = new ApiResponse<>(200, reports, null);
                }



                responseDTO.setStatusCode(apiResponse.getStatusCode());
                responseDTO.setHeaders(Map.of("Content-Type", "application/json"));
                responseDTO.setBody(gson.toJson(apiResponse));

                System.out.println("Se genero bien aca ");
                System.out.println(responseDTO);

                return responseDTO;

            } catch (Exception e) {
                log.error("Error al consultar los reportes por historyUser: ", e);

                ApiResponse<String> apiResponse = new ApiResponse<>(500, null, "Error al consultar reportes: " + e.getMessage());

                responseDTO.setStatusCode(apiResponse.getStatusCode());
                responseDTO.setHeaders(Map.of("Content-Type", "application/json"));
                responseDTO.setBody(gson.toJson(apiResponse));

                return responseDTO;
            }
        };
    }
}


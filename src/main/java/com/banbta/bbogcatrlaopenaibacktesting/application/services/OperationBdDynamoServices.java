package com.banbta.bbogcatrlaopenaibacktesting.application.services;


import com.banbta.bbogcatrlaopenaibacktesting.application.dto.response.ApiResponse;
import com.banbta.bbogcatrlaopenaibacktesting.application.dto.response.GenerateReportResponseDTO;
import com.banbta.bbogcatrlaopenaibacktesting.domain.entitys.ReportAiEntity;
import com.banbta.bbogcatrlaopenaibacktesting.domain.repository.ReportAiRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationBdDynamoServices {

    private final ReportAiRepository reportAiRepository;
    private final Gson gson;
    private final GenerateReportResponseDTO generateReportResponseDTO;

    @Autowired
    public OperationBdDynamoServices(ReportAiRepository reportAiRepository, Gson gson, GenerateReportResponseDTO generateReportResponseDTO) {
        this.reportAiRepository = reportAiRepository;
        this.gson = gson;
        this.generateReportResponseDTO = generateReportResponseDTO;
    }

    /**
     * Obtiene una lista de ReportAiEntity que coinciden con el historyUser especificado.
     * @param historyUser El valor de historyUser a buscar.
     * @return Una lista de ReportAiEntity que coinciden con el historyUser.
     */
    public List<ReportAiEntity> findReportsByHistoryUser(String historyUser) {

        List<ReportAiEntity> reports = reportAiRepository.getReportAiByHistoryUser(historyUser);

        if (reports.isEmpty()) {
            ApiResponse<String> apiResponse = new ApiResponse<>(404, null, "No se encontraron reportes para el historyUser especificado");
            generateReportResponseDTO.setBody(gson.toJson(apiResponse));
        } else {
            ApiResponse<List<ReportAiEntity>> apiResponse = new ApiResponse<>(200, reports, null);
            generateReportResponseDTO.setBody(gson.toJson(apiResponse));
        }

        return reports;
    }
}

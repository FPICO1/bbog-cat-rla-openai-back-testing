package com.banbta.bbogcatrlaopenaibacktesting.application.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class GenerateReportResponseDTO {
    private String body;
    private int statusCode;
    private Map<String, String> headers;
}
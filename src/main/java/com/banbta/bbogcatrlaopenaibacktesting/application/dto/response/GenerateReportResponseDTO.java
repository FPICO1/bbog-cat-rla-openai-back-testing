package com.banbta.bbogcatrlaopenaibacktesting.application.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateReportResponseDTO {
    private int statusCode;
    private Map<String, String> headers;
    private String body;

}
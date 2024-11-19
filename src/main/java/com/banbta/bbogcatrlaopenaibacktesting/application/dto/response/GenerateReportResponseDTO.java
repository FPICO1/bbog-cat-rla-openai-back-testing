package com.banbta.bbogcatrlaopenaibacktesting.application.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class GenerateReportResponseDTO {
    private int statusCode;
    private Map<String, String> headers;
    private String body;

}
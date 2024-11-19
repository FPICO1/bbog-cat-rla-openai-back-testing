package com.banbta.bbogcatrlaopenaibacktesting.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private int statusCode;
    private T data;
    private String body;

// Constructores, getters y setters

}
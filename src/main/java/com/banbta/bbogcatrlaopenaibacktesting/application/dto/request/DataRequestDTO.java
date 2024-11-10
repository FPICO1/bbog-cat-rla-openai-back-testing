package com.banbta.bbogcatrlaopenaibacktesting.application.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataRequestDTO {

    @NotEmpty(message = "El campo userName es obligatorio")
    private String userName;

    @NotEmpty(message = "El campo moduleName es obligatorio")
    private String moduleName;

    @NotEmpty(message = "El campo historyUser es obligatorio")
    private String historyUser;

    @NotEmpty(message = "El campo environment es obligatorio")
    private String environment;

    @NotEmpty(message = "El campo endPoint es obligatorio")
    private String endPoint;

}
package com.banbta.bbogcatrlaopenaibacktesting.application.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataBdDynamoRequestDTO {


    @NotEmpty(message = "El campo historyUser es obligatorio")
    private String historyUser;


}
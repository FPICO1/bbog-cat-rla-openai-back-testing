package com.banbta.bbogcatrlaopenaibacktesting.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetFileJsonRequestDTO {

    private String userName;
    private String moduleName;

}
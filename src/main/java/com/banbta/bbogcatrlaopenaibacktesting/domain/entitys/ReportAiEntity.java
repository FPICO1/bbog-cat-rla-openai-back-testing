package com.banbta.bbogcatrlaopenaibacktesting.domain.entitys;


import com.banbta.bbogcatrlaopenaibacktesting.common.LocalDateConverter;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbConvertedBy;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@ToString
@DynamoDbBean
@AllArgsConstructor
@NoArgsConstructor
public class ReportAiEntity {

    private Long idReportAi;
    private String historyUser;
    private String userRed;
    private String endpoint;
    private String environment;
    private String jsonData;          // JSON como Map
    private String jsonResponseAi;
    private LocalDate dateExecute;




    @DynamoDbPartitionKey
    public Long getIdReportAi() {
        return idReportAi;
    }


    @DynamoDbAttribute("historyUser")
    public String getHistoryUser() {
        return historyUser;
    }

    @DynamoDbAttribute("userRed")
    public String getUserRed() {
        return userRed;
    }

    @DynamoDbAttribute("endpoint")
    public String getEndpoint() {
        return endpoint;
    }
    @DynamoDbAttribute("environment")
    public String getEnvironment() {
        return environment;
    }
    @DynamoDbAttribute("jsonData")
    public String getJsonData() {
        return jsonData;
    }

    @DynamoDbAttribute("jsonResponseAi")
    public String getJsonResponseAi() {
        return jsonResponseAi;
    }

    @DynamoDbAttribute("dateExecute")
    @DynamoDbConvertedBy(LocalDateConverter.class)
    public LocalDate getDateExecute() {
        return dateExecute;
    }







}

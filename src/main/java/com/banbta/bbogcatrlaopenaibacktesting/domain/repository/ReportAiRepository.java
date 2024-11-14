package com.banbta.bbogcatrlaopenaibacktesting.domain.repository;

import com.banbta.bbogcatrlaopenaibacktesting.domain.entitys.ReportAiEntity;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class ReportAiRepository implements ReportAiInterfaceRepository {


    private final DynamoDbTable<ReportAiEntity> reportAiEntityDynamoDbTable;


    public ReportAiRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.reportAiEntityDynamoDbTable = dynamoDbEnhancedClient.table("reports-ai-bd", TableSchema.fromBean(ReportAiEntity.class));
    }


    @Override
    public void saveReportAi(ReportAiEntity reportAiEntity) {
        reportAiEntityDynamoDbTable.putItem(reportAiEntity);
    }

    @Override
    public ReportAiEntity getReportAiById(Long idReportAi) {

        Key key = Key.builder().partitionValue(idReportAi).build();
        return reportAiEntityDynamoDbTable.getItem(key);
    }

    @Override
    public void deleteReportAiById(Long idReportAi) {

        Key key = Key.builder().partitionValue(idReportAi).build();
        reportAiEntityDynamoDbTable.deleteItem(key);

    }
}

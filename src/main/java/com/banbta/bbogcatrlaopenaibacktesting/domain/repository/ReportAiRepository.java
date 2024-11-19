package com.banbta.bbogcatrlaopenaibacktesting.domain.repository;

import com.banbta.bbogcatrlaopenaibacktesting.domain.entitys.ReportAiEntity;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<ReportAiEntity> getReportAiByHistoryUser(String historyUser) {
        try {
            // Configurar la consulta en el índice `historyUser-index` y usar `stream` para recolectar los resultados en una lista
            return reportAiEntityDynamoDbTable
                    .index("historyUser-index") // Nombre del índice
                    .query(r -> r.queryConditional(QueryConditional.keyEqualTo(Key.builder().partitionValue(historyUser).build())))
                    .stream()
                    .flatMap(page -> page.items().stream()) // Obtiene los elementos en cada página
                    .collect(Collectors.toList());

        } catch (DynamoDbException e) {
            System.err.println("Error al consultar DynamoDB: " + e.getMessage());
            return List.of();
        }
    }

   @Override
    public void deleteReportAiById(Long idReportAi) {

        Key key = Key.builder().partitionValue(idReportAi).build();
        reportAiEntityDynamoDbTable.deleteItem(key);

    }
}

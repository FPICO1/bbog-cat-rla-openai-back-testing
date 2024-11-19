package com.banbta.bbogcatrlaopenaibacktesting.domain.repository;

import com.banbta.bbogcatrlaopenaibacktesting.domain.entitys.ReportAiEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportAiInterfaceRepository {


    void saveReportAi(ReportAiEntity reportAiEntity);
    ReportAiEntity getReportAiById(Long idReportAi);
    List<ReportAiEntity> getReportAiByHistoryUser(String historyUser);

    void deleteReportAiById(Long idReportAi);


}

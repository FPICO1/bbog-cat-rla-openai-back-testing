package com.banbta.bbogcatrlaopenaibacktesting.domain.repository;

import com.banbta.bbogcatrlaopenaibacktesting.domain.entitys.ReportAiEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportAiInterfaceRepository {


    void saveReportAi(ReportAiEntity reportAiEntity);
    ReportAiEntity getReportAiById(Long idReportAi);
    void deleteReportAiById(Long idReportAi);

}

package com.example.webmagic.pipeline;

import com.example.webmagic.domain.JobCompanyInfo;
import com.example.webmagic.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
public class DatabasePipeline implements Pipeline {

    @Autowired
    private DatabaseService databaseService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        Object jobCompanyInfoObject = resultItems.get("jobCompanyInfo");
        JobCompanyInfo jobCompanyInfo = resultItems.get("jobCompanyInfo");
        if (jobCompanyInfo != null) {
            databaseService.processEntry(jobCompanyInfo);
        }
    }
}
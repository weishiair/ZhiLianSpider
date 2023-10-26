package com.example.webmagic.pipeline;

import com.example.webmagic.domain.JobCompanyInfo;
import com.example.webmagic.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

@Component
public class DatabasePipeline implements Pipeline {

    @Autowired
    private DatabaseService databaseService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        // 从 resultItems 获取名为 "jobCompanyInfo" 的对象，并检查它是否为 ArrayList 类型
        Object jobCompanyInfoObject = resultItems.get("jobCompanyInfo");
        System.out.println("Type: " + jobCompanyInfoObject.getClass());
        System.out.println("Value: " + jobCompanyInfoObject.toString());


        if (jobCompanyInfoObject instanceof List<?>) {
            List<?> list = (List<?>) jobCompanyInfoObject;
            for (Object item : list) {
                if (item instanceof JobCompanyInfo) {
                    // 如果列表中的项是 JobCompanyInfo 类型的，处理它
                    JobCompanyInfo jobCompanyInfo = (JobCompanyInfo) item;
                    databaseService.processEntry(jobCompanyInfo);
                } else {
                    System.err.println("Unexpected item type in list: " + item.getClass());
                }
            }
        } else {
            System.err.println("Unexpected object type for jobCompanyInfo: " + jobCompanyInfoObject.getClass());
        }
    }
}

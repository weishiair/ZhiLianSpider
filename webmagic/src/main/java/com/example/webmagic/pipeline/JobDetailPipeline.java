package com.example.webmagic.pipeline;

import com.example.webmagic.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
public class JobDetailPipeline implements Pipeline {

    @Autowired
    private DatabaseService databaseService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        System.out.println("进入JobDetailPipeline进程.");

        // 从 Spider 对象获取 jobId 和 companyId
        String[] ids = task.getUUID().split("-");
        Integer jobId = Integer.parseInt(ids[0]);
        Integer companyId = Integer.parseInt(ids[1]);

        // 从 ResultItems 获取 jobLocation, jobDescription, companyWebsite
        String jobLocation = resultItems.get("jobLocation");
        String jobDescription = resultItems.get("jobDescription");
        String companyWebsite = resultItems.get("companyWebsite");
        String labels = resultItems.get("labels");

        // 更新数据库
        databaseService.updateJobAndCompanyInfo(jobId, jobLocation, jobDescription, companyId, companyWebsite,labels);

        System.out.println("退出JobDetailPipeline进程.");
    }
}

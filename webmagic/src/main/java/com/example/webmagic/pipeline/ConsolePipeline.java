package com.example.webmagic.pipeline;

import com.example.webmagic.domain.CompanyInfo;
import com.example.webmagic.domain.JobInfo;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

@Component
public class ConsolePipeline implements Pipeline {

    @Override
    public void process(ResultItems resultItems, Task task) {
        // 获取到数据
        List<JobInfo> jobInfos = resultItems.get("jobInfo");
        List<CompanyInfo> companyInfos = resultItems.get("companyInfo");

        // 如果 jobInfos 不为空并且其大小大于0，则打印每个JobInfo对象
        if (jobInfos != null && !jobInfos.isEmpty()) {
            for (JobInfo jobInfo : jobInfos) {
                System.out.println(jobInfo);  // 打印到控制台
            }
        } else {
            System.out.println("没有获取到职位数据");
        }

        // 如果 companyInfos 不为空并且其大小大于0，则打印每个CompanyInfo对象
        if (companyInfos != null && !companyInfos.isEmpty()) {
            for (CompanyInfo companyInfo : companyInfos) {
                System.out.println(companyInfo);  // 打印到控制台
            }
        } else {
            System.out.println("没有获取到公司数据");
        }
    }
}


package com.example.webmagic.pipeline;

import com.example.webmagic.domain.CompanyInfo;
import com.example.webmagic.domain.JobCompanyInfo;
import com.example.webmagic.domain.JobInfo;
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
                    JobCompanyInfo jobCompanyInfo = (JobCompanyInfo) item;

                    // 清洗JobInfo和CompanyInfo中的数据
                    cleanJobInfoData(jobCompanyInfo.getJobInfo());
                    cleanCompanyInfoData(jobCompanyInfo.getCompanyInfo());

                    // 将清洗后的数据对象传递给数据库服务进行处理
                    databaseService.processEntry(jobCompanyInfo);
                } else {
                    System.err.println("列表中出现意外的项目类型: " + item.getClass());
                }
            }
        } else {
            System.err.println("jobCompanyInfo 的对象类型异常: " + jobCompanyInfoObject.getClass());
        }
    }

    private void cleanJobInfoData(JobInfo jobInfo) {
        if (jobInfo != null) {
            jobInfo.setJobName(cleanString(jobInfo.getJobName()));
            jobInfo.setSalary(cleanString(jobInfo.getSalary()));
            jobInfo.setCity(cleanString(jobInfo.getCity()));
            jobInfo.setExperience(cleanString(jobInfo.getExperience()));
            jobInfo.setEducation(cleanString(jobInfo.getEducation()));
            jobInfo.setJobDetails(cleanString(jobInfo.getJobDetails()));
        }
    }

    private void cleanCompanyInfoData(CompanyInfo companyInfo) {
        if (companyInfo != null) {
            companyInfo.setCompanyName(cleanString(companyInfo.getCompanyName()));
            companyInfo.setCompanyNature(cleanString(companyInfo.getCompanyNature()));
            companyInfo.setCompanySize(cleanString(companyInfo.getCompanySize()));
        }
    }

    private String cleanString(String str) {
        return str != null ? str.trim().replaceAll("\\s+", " ") : null;
    }
}

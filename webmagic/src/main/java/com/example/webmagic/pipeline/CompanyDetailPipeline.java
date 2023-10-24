package com.example.webmagic.pipeline;


import com.example.webmagic.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class CompanyDetailPipeline implements Pipeline {

    @Autowired
    private DatabaseService databaseService;


    @Override
    public void process(ResultItems resultItems, Task task) {
        System.out.println("Entering process method in CompanyDetailPipeline.");

        // 从 Spider 对象获取 companyId
        String[] ids = task.getUUID().split("-");
        Integer companyId = Integer.parseInt(ids[0]);

        // 从 ResultItems 获取公司详情数据
        String establishmentDateStr = resultItems.get("establishmentDateStr");
        String registeredCapital = resultItems.get("registeredCapital");
        String legalRepresentative = resultItems.get("legalRepresentative");
        String companyAddress = resultItems.get("companyAddress");
        String companyIntroduce = resultItems.get("companyIntroduce");

        // 检查成立时间字符串是否为空或null，然后尝试解析它
        Date establishmentDate = null;
        if (establishmentDateStr != null && !establishmentDateStr.isEmpty()) {
            try {
                establishmentDate = new SimpleDateFormat("yyyy-MM-dd").parse(establishmentDateStr);
            } catch (ParseException e) {
                e.printStackTrace();  // 打印解析异常，您也可以选择记录或处理异常
            }
        }

        // 更新数据库
        databaseService.updateCompanyDetails(
                companyId,
                establishmentDate,
                registeredCapital,
                legalRepresentative,
                companyAddress,
                companyIntroduce

        );

        System.out.println("Exiting process method in CompanyDetailPipeline.");
    }

}

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
        System.out.println("进入CompanyDetailPipeline流程.");

        // 从 Spider 对象获取 companyId
        String[] ids = task.getUUID().split("-");
        Integer companyId = Integer.parseInt(ids[0]);

        // 从 ResultItems 获取公司详情数据
        String establishmentDateStr = cleanString(resultItems.get("establishmentDateStr"));
        String registeredCapital = cleanString(resultItems.get("registeredCapital"));
        String legalRepresentative = cleanString(resultItems.get("legalRepresentative"));
        String companyAddress = cleanString(resultItems.get("companyAddress"));
        String companyIntroduce = cleanString(resultItems.get("companyIntroduce"));

        // 检查成立时间字符串是否为空或null，然后尝试解析它
        Date establishmentDate = null;
        if (establishmentDateStr != null && !establishmentDateStr.isEmpty()) {
            try {
                establishmentDate = new SimpleDateFormat("yyyy-MM-dd").parse(establishmentDateStr);
            } catch (ParseException e) {
                System.err.println("成立时间字符串解析失败: " + establishmentDateStr);
                e.printStackTrace();  // 打印解析异常
            }
        }

        // 设置当前时间为最后更新时间
        Date now = new Date();

        // 更新数据库
        databaseService.updateCompanyDetails(
                companyId,
                establishmentDate,
                registeredCapital,
                legalRepresentative,
                companyAddress,
                companyIntroduce,
                now
        );

        System.out.println("退出CompanyDetailPipeline流程.");
    }

    private String cleanString(String str) {
        if (str == null) {
            return null;
        }
        // 去除字符串两端的空白字符
        str = str.trim();
        // 替换字符串中的连续多个空白字符为一个空格
        str = str.replaceAll("\\s+", " ");
        return str;
    }
}

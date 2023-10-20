package com.example.webmagic;

import com.example.webmagic.domain.JobDetailInfo;
import com.example.webmagic.domain.JobInfo;
import com.example.webmagic.pipeline.ConsolePipeline;
import com.example.webmagic.pipeline.JobDetailPipeline;
import com.example.webmagic.processor.JobDetailProcessor;
import com.example.webmagic.service.DatabaseService;
import com.example.webmagic.service.WebDriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import us.codecraft.webmagic.Spider;
import com.example.webmagic.processor.ZhilianJobProcessor;
import com.example.webmagic.downloader.SeleniumDownloader;
import com.example.webmagic.pipeline.DatabasePipeline;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import java.util.List;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private ZhilianJobProcessor zhilianJobProcessor;

    @Autowired
    private DatabasePipeline databasePipeline;

    @Autowired
    private JobDetailProcessor jobDetailProcessor;

    @Autowired
    private JobDetailPipeline jobDetailPipeline;

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private WebDriverService webDriverService;


    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        SpringApplication.run(Application.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        // 创建并启动主爬虫任务
        Spider spider = Spider.create(zhilianJobProcessor)
                .addUrl("https://sou.zhaopin.com/?jl=801&kw=soildworks&p=1")
                .setDownloader(new SeleniumDownloader(webDriverService))
                .addPipeline(databasePipeline)
                .setScheduler(new QueueScheduler()
                        .setDuplicateRemover(new BloomFilterDuplicateRemover(10000000)))  // 10000000是预期的URL数量
                .thread(1);

        // 启动爬虫
        spider.run();

        // 获取需要爬取详情的工作列表
        List<JobDetailInfo> jobInfos = databaseService.getJobsForDetailScraping();
        for (JobDetailInfo jobInfo : jobInfos) {
            // 为每个工作创建并启动一个新的 Spider 实例
            Spider detailSpider = Spider.create(jobDetailProcessor)
                    .addUrl(jobInfo.getJobDetails())
                    .addPipeline(jobDetailPipeline)
                    .thread(1);
            // 将 jobId 和 companyId 传递给 processor 和 pipeline
            detailSpider.setUUID(jobInfo.getJobId().toString() + "-" + jobInfo.getCompanyId().toString());
            detailSpider.run();  // 使用 detailSpider 实例运行爬虫
        }
        webDriverService.destroy();
    }
}

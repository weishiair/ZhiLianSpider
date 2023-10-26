package com.example.webmagic;

import com.example.webmagic.config.WebDriverProvider;
import com.example.webmagic.domain.CompanyDetailInfo;
import com.example.webmagic.domain.CompanyInfo;
import com.example.webmagic.domain.JobDetailInfo;
import com.example.webmagic.domain.JobInfo;
import com.example.webmagic.pipeline.CompanyDetailPipeline;
import com.example.webmagic.pipeline.ConsolePipeline;
import com.example.webmagic.pipeline.JobDetailPipeline;
import com.example.webmagic.processor.CompanyDetailProcessor;
import com.example.webmagic.processor.JobDetailProcessor;
import com.example.webmagic.service.DatabaseService;
import com.example.webmagic.service.UserInputService;
import com.example.webmagic.service.WebDriverService;
import com.example.webmagic.util.LoginCheckUtil;
import com.example.webmagic.util.LoginUtil;
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
    private WebDriverProvider webDriverProvider;  // 更改为 WebDriverProvider

    @Autowired
    private CompanyDetailProcessor companyDetailProcessor;

    @Autowired
    private CompanyDetailPipeline companyDetailPipeline;

    @Autowired
    private LoginUtil loginUtil;

    @Autowired
    private LoginCheckUtil LogincheckUtil;


    @Autowired
    private WebDriverService webDriverService;  // 保持使用 WebDriverService

    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        UserInputService userInputService = new UserInputService();
        String city = userInputService.getCity();
        String keyword = userInputService.getKeyword();

        // 尝试应用之前保存的 cookies
        webDriverService.applyCookies();  // 正确的方法调用
        loginUtil.loginIfNecessary();
        boolean isLoggedIn = LogincheckUtil.isUserLoggedIn();
        if (!isLoggedIn) {
            System.err.println("登录失败，终止爬虫.");
            return;
        }

        // 创建并启动主爬虫任务
        Spider spider = Spider.create(zhilianJobProcessor)
                .addUrl(String.format("https://sou.zhaopin.com/?jl=%s&kw=%s&p=1", city, keyword))
                .setDownloader(new SeleniumDownloader(loginUtil, LogincheckUtil, webDriverProvider))  // 更新为 webDriverProvider
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
                    .thread(2);
            // 将 jobId 和 companyId 传递给 processor 和 pipeline
            detailSpider.setUUID(jobInfo.getJobId().toString() + "-" + jobInfo.getCompanyId().toString());
            detailSpider.run();  // 使用 detailSpider 实例运行爬虫
        }

        // 获取需要爬取详情的公司列表
        List<CompanyDetailInfo> companyInfos = databaseService.getCompaniesForDetailScraping();
        for (CompanyDetailInfo companyInfo : companyInfos) {
            String companyWebsite = companyInfo.getCompanyWebsite();
            if (companyWebsite == null || companyWebsite.isEmpty()) {
                // 此公司的网站URL为空，跳过处理
                System.err.println("Skipping company with ID " + companyInfo.getCompanyId() + " due to missing website URL.");
                continue;  // 跳到循环的下一个迭代
            }
            // 为每个公司创建并启动一个新的 Spider 实例
            Spider companyDetailSpider = Spider.create(companyDetailProcessor)
                    .addUrl(companyInfo.getCompanyWebsite())
                    .addPipeline(companyDetailPipeline)
                    .thread(2);
            // 将 companyId 传递给 processor 和 pipeline
            companyDetailSpider.setUUID(companyInfo.getCompanyId().toString());
            companyDetailSpider.run();  // 使用 companyDetailSpider 实例运行爬虫
        }
        webDriverProvider.destroy();
    }
}
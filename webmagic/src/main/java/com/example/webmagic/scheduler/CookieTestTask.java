package com.example.webmagic.scheduler;

import com.example.webmagic.config.SearchConfig;
import com.example.webmagic.config.WebDriverProvider;
import com.example.webmagic.domain.CompanyDetailInfo;
import com.example.webmagic.domain.JobDetailInfo;
import com.example.webmagic.downloader.SeleniumDownloader;
import com.example.webmagic.pipeline.CompanyDetailPipeline;
import com.example.webmagic.pipeline.DatabasePipeline;
import com.example.webmagic.pipeline.JobDetailPipeline;
import com.example.webmagic.processor.CompanyDetailProcessor;
import com.example.webmagic.processor.JobDetailProcessor;
import com.example.webmagic.processor.ZhilianJobProcessor;
import com.example.webmagic.service.DatabaseService;
import com.example.webmagic.service.WebDriverService;
import com.example.webmagic.util.LoginCheckUtil;
import com.example.webmagic.util.LoginUtil;
import com.example.webmagic.util.UrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import java.util.List;

@Component
public class CookieTestTask {
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
    private WebDriverProvider webDriverProvider;

    @Autowired
    private CompanyDetailProcessor companyDetailProcessor;

    @Autowired
    private CompanyDetailPipeline companyDetailPipeline;

    @Autowired
    private LoginUtil loginUtil;

    @Autowired
    private LoginCheckUtil LogincheckUtil;

    @Autowired
    private WebDriverService webDriverService;
    @Autowired
    private SearchConfig searchConfig;  // 注入 SearchConfig
    @Autowired
    private UrlUtil urlUtil;  // 将 UrlUtil 的自动装配移到类的顶部


    private static String listPageUrl;  // 添加这个字段来存储列表页面的URL

    public static String getListPageUrl() {
        return listPageUrl;
    }

    @Scheduled(cron = "00 23 14 * * ?")  // 每天上午11:00执行
    public void runDailyJobCrawl() throws Exception {
        // 从 SearchConfig 中获取城市和关键字列表
        List<String> cities = searchConfig.getCities();
        List<String> keywords = searchConfig.getKeywords();

        // 尝试应用之前保存的 cookies
        webDriverService.applyCookies();

        boolean isLoggedIn = LogincheckUtil.isUserLoggedIn();
        int retryCount = 0;
        int maxRetries = 3;  // 设置最大重试次数，例如3次
        while (!isLoggedIn && retryCount < maxRetries) {
            loginUtil.loginIfNecessary();
            isLoggedIn = LogincheckUtil.isUserLoggedIn();
            retryCount++;
            if (!isLoggedIn) {
                System.err.println("登录失败，重试 " + retryCount + " / " + maxRetries);
                // 等待一段时间再重试
                Thread.sleep(5000);  // 等待5秒
            }
        }
        if (!isLoggedIn) {
            System.err.println("登录失败，终止爬虫.");
            return;
        }

        // 处理所有城市和关键字的组合
        for (String city : cities) {
            for (String keyword : keywords) {
                zhilianJobProcessor.setCity(city);
                zhilianJobProcessor.setKeyword(keyword);
                System.out.println("Current City: " + city + ", Current Keyword: " + keyword);

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
                listPageUrl = String.format("https://sou.zhaopin.com/?jl=%s&kw=%s&p=1", city, keyword);

                // 获取需要爬取详情的工作列表
                List<JobDetailInfo> jobInfos = databaseService.getJobsForDetailScraping();
                for (JobDetailInfo jobInfo : jobInfos) {
                    // 获取并确保工作详情 URL 是 HTTPS
                    String jobDetailsUrl = urlUtil.ensureHttps(jobInfo.getJobDetails());  // 使用 urlUtil.ensureHttps

                    // 为每个工作创建并启动一个新的 Spider 实例
                    Spider detailSpider = Spider.create(jobDetailProcessor)
                            .addUrl(jobDetailsUrl)  // 使用新变量 jobDetailsUrl
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
                    // 确保网址是HTTPS
                    companyWebsite = urlUtil.ensureHttps(companyWebsite);  // 使用 urlUtil.ensureHttps

                    // 为每个公司创建并启动一个新的 Spider 实例
                    Spider companyDetailSpider = Spider.create(companyDetailProcessor)
                            .addUrl(companyWebsite)  // 使用新变量 companyWebsite
                            .addPipeline(companyDetailPipeline)
                            .thread(1);
                    // 将 companyId 传递给 processor 和 pipeline
                    companyDetailSpider.setUUID(companyInfo.getCompanyId().toString());
                    companyDetailSpider.run();  // 使用 companyDetailSpider 实例运行爬虫
                }

            }
        }
        //webDriverProvider.destroy();  // 确保在完成所有任务后销毁 WebDriver 实例
    }
}
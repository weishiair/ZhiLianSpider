package com.example.webmagic.scheduler;

import com.example.webmagic.config.SearchConfig;
import com.example.webmagic.config.WebDriverProvider;
import com.example.webmagic.domain.CompanyDetailInfo;
import com.example.webmagic.domain.JobDetailInfo;
import com.example.webmagic.domain.UserConfig;
import com.example.webmagic.downloader.SeleniumDownloader;
import com.example.webmagic.pipeline.CompanyDetailPipeline;
import com.example.webmagic.pipeline.DatabasePipeline;
import com.example.webmagic.pipeline.JobDetailPipeline;
import com.example.webmagic.processor.CompanyDetailProcessor;
import com.example.webmagic.processor.JobDetailProcessor;
import com.example.webmagic.processor.ZhilianJobProcessor;
import com.example.webmagic.service.DatabaseService;
import com.example.webmagic.service.UserConfigService;
import com.example.webmagic.service.WebDriverService;
import com.example.webmagic.util.LoginCheckUtil;
import com.example.webmagic.util.LoginUtil;
import com.example.webmagic.util.UrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
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
    private UrlUtil urlUtil;  // 将 UrlUtil 的自动装配移到类的顶部
    @Autowired
    private UserConfigService userConfigService;


    private static String listPageUrl;  // 添加这个字段来存储列表页面的URL

    public static String getListPageUrl() {
        return listPageUrl;
    }


    // 定时任务的入口点
    @Scheduled(cron = "00 34 10 * * ?")
    public void runDailyJobCrawl() throws Exception {
        executeCrawl(); // 定时调用新的方法
    }

    // 实际执行爬虫的方法
    public void executeCrawl() throws Exception {
        webDriverService.applyCookies();

        boolean isLoggedIn = LogincheckUtil.isUserLoggedIn();
        int retryCount = 0;
        int maxRetries = 3;
        while (!isLoggedIn && retryCount < maxRetries) {
            loginUtil.loginIfNecessary();
            isLoggedIn = LogincheckUtil.isUserLoggedIn();
            retryCount++;
            if (!isLoggedIn) {
                System.err.println("登录失败，重试 " + retryCount + " / " + maxRetries);
                Thread.sleep(5000);
            }
        }
        if (!isLoggedIn) {
            System.err.println("登录失败，终止爬虫.");
            return;
        }
        List<UserConfig> activeConfigs = userConfigService.listActiveConfigs();

        for (UserConfig config : activeConfigs) {
            String city = config.getCity();
            String keyword = config.getKeyword();
            zhilianJobProcessor.setCity(city);
            zhilianJobProcessor.setKeyword(keyword);
            System.out.println("现在的城市是: " + city + ", 现在的关键词是: " + keyword);

            Spider spider = Spider.create(zhilianJobProcessor)
                    .addUrl(String.format("https://sou.zhaopin.com/?jl=%s&kw=%s&p=1", city, keyword))
                    .setDownloader(new SeleniumDownloader(loginUtil, LogincheckUtil, webDriverProvider))
                    .addPipeline(databasePipeline)
                    .setScheduler(new QueueScheduler()
                            .setDuplicateRemover(new BloomFilterDuplicateRemover(10000000)))
                    .thread(1);
            spider.run();
            listPageUrl = String.format("https://sou.zhaopin.com/?jl=%s&kw=%s&p=1", city, keyword);

            List<JobDetailInfo> jobInfos = databaseService.getJobsForDetailScraping();
            for (JobDetailInfo jobInfo : jobInfos) {
                String jobDetailsUrl = urlUtil.ensureHttps(jobInfo.getJobDetails());

                Spider detailSpider = Spider.create(jobDetailProcessor)
                        .addUrl(jobDetailsUrl)
                        .addPipeline(jobDetailPipeline)
                        .thread(2);
                detailSpider.setUUID(jobInfo.getJobId().toString() + "-" + jobInfo.getCompanyId().toString());
                detailSpider.run();
            }

            List<CompanyDetailInfo> companyInfos = databaseService.getCompaniesForDetailScraping();
            for (CompanyDetailInfo companyInfo : companyInfos) {
                String companyWebsite = companyInfo.getCompanyWebsite();
                if (companyWebsite == null || companyWebsite.isEmpty()) {
                    System.err.println("跳过id为" + companyInfo.getCompanyId() + " 的公司，因为url丢失.");
                    continue;
                }
                companyWebsite = urlUtil.ensureHttps(companyWebsite);

                Spider companyDetailSpider = Spider.create(companyDetailProcessor)
                        .addUrl(companyWebsite)
                        .addPipeline(companyDetailPipeline)
                        .thread(1);
                companyDetailSpider.setUUID(companyInfo.getCompanyId().toString());
                companyDetailSpider.run();
            }
        }
        //webDriverProvider.destroy();  // 确保在完成所有任务后销毁 WebDriver 实例
    }
    }

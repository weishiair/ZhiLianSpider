package com.example.webmagic.processor;

import com.example.webmagic.config.SearchConfig;
import com.example.webmagic.config.WebDriverProvider;
import com.example.webmagic.domain.JobCompanyInfo;
import com.example.webmagic.domain.JobInfo;
import com.example.webmagic.domain.CompanyInfo;
import com.example.webmagic.service.DatabaseService;
import com.example.webmagic.service.WebDriverService;
import com.example.webmagic.util.SliderHandler;
import com.example.webmagic.util.UserAgentUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.*;

@Component
public class ZhilianJobProcessor implements PageProcessor {

    @Autowired
    private WebDriverProvider webDriverProvider;  // 注入 WebDriverProvider
    @Autowired
    private SliderHandler sliderHandler;  // 注入 SliderHandler
    private final DatabaseService databaseService;


    private String city;

    private String keyword;

    // 添加设置方法
    public void setCity(String city) {
        this.city = city;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }



    //@Autowired
    public ZhilianJobProcessor(WebDriverProvider webDriverProvider, DatabaseService databaseService) {
        this.webDriverProvider = webDriverProvider;
        this.databaseService = databaseService;
    }




    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setUserAgent(UserAgentUtils.randomUserAgent());

    @Override
    public void process(Page page) {
        WebDriver driver = webDriverProvider.getWebDriver();  // 获取 WebDriver 实例
        processPage(page, driver);  // 直接调用 processPage 方法，不需要循环

    }


    public void processPage(Page page, WebDriver driver) {
        // 构建URL
        String url = String.format("https://sou.zhaopin.com/?jl=%s&kw=%s&p=1", city, keyword);
        System.out.println("Constructed URL: " + url);  // 打印构造的URL
        driver.get(url);  // 使用新构建的URL访问网页
        try {
            Thread.sleep(5000);  // 等待5秒以确保页面加载完成
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 检测滑块验证码
        boolean isSliderPresent = isSliderPresent(driver);
        if (isSliderPresent) {
            sliderHandler.handleSlider(driver);  // 调用 SliderHandler 来处理滑块
        }

        boolean hasNextPage = true;

        while (hasNextPage) {
            String pageSource = driver.getPageSource();
            Html html = new Html(pageSource);

            // 创建一个JobInfo列表来保存解析的数据
            List<JobInfo> jobInfos = new ArrayList<>();
            List<CompanyInfo> companyInfos = new ArrayList<>();
            List<JobCompanyInfo> jobCompanyInfos = new ArrayList<>();

            //提取页面上的数据
            List<String> jobs = html.xpath("//div[@class='positionlist']//a//div[@class='iteminfo__line1__jobname']/span/@title").all();
            List<String> companies = html.xpath("//div[@class='positionlist']//a//div[@class='iteminfo__line1__compname']/span/@title").all();
            List<String> salaries = html.xpath("//div[@class='positionlist']//a//div[@class='iteminfo__line2__jobdesc']/p/text()").all();
            List<String> cities = html.xpath("//div[@class='positionlist']//a//div[@class='iteminfo__line2__jobdesc']/ul/li[1]/text()").all();
            List<String> experiences = html.xpath("//div[@class='positionlist']//a//div[@class='iteminfo__line2__jobdesc']/ul/li[2]/text()").all();
            List<String> educations = html.xpath("//div[@class='positionlist']//a//div[@class='iteminfo__line2__jobdesc']/ul/li[3]/text()").all();
            List<String> companyTypes = html.xpath("//div[@class='positionlist']//a//div[@class='iteminfo__line2__compdesc']/span[1]/text()").all();
            List<String> companySizes = html.xpath("//div[@class='positionlist']//a//div[@class='iteminfo__line2__compdesc']/span[2]/text()").all();
            List<String> detailPageUrls = html.xpath("//div[@class='positionlist']//a/@href").all();


            // 获取所有列表中的最小大小
            int minSize = Collections.min(Arrays.asList(jobs.size(), companies.size(), salaries.size(), cities.size(),
                    experiences.size(), educations.size(), companyTypes.size(), companySizes.size(), detailPageUrls.size()));

            for (int i = 0; i < minSize; i++) {
                JobInfo jobInfo = new JobInfo();
                CompanyInfo companyInfo = new CompanyInfo();
                jobInfo.setJobName(getValue(jobs, i));
                jobInfo.setSalary(getValue(salaries, i));
                jobInfo.setCity(getValue(cities, i));
                jobInfo.setExperience(getValue(experiences, i));
                jobInfo.setEducation(getValue(educations, i));
                companyInfo.setCompanyName(getValue(companies, i));
                companyInfo.setCompanyNature(getValue(companyTypes, i));
                companyInfo.setCompanySize(getValue(companySizes, i));
                jobInfo.setJobDetails(getValue(detailPageUrls, i));
                JobCompanyInfo jobCompanyInfo = new JobCompanyInfo();
                jobCompanyInfo.setJobInfo(jobInfo);
                jobCompanyInfo.setCompanyInfo(companyInfo);
                jobCompanyInfos.add(jobCompanyInfo);
                jobInfos.add(jobInfo);  // 将JobInfo对象添加到列表中
                companyInfos.add(companyInfo);
            }

            // 将解析的JobInfo列表放入Page对象中，以便传递给Pipeline
            page.putField("jobInfo", jobInfos);
            page.putField("companyInfo", companyInfos);
            page.putField("jobCompanyInfo", jobCompanyInfos);

            // 立即将数据写入数据库
            databaseService.processEntries(jobCompanyInfos);

            List<WebElement> buttons = driver.findElements(By.xpath("//div[@class='soupager']/button[@class='btn soupager__btn']"));
            if (!buttons.isEmpty()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", buttons.get(0));
                try {
                    Thread.sleep(8000);  // 等待8秒以确保页面加载
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Fetching completed!");
                hasNextPage = false;  // 如果没有下一页按钮，设置hasNextPage为false以退出循环
            }
        }
    }

    private String getValue(List<String> list, int index) {
        return index < list.size() ? list.get(index) : null;
    }

    @Override
    public Site getSite() {
        return site;
    }

    public boolean isSliderPresent(WebDriver driver) {
        // 使用 findElements 方法查找元素
        List<WebElement> sliders = driver.findElements(By.id("nc_1_nocaptcha"));
        // 检查返回的列表是否为空
        return !sliders.isEmpty();
    }

}

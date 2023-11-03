package com.example.webmagic.processor;

import com.example.webmagic.config.WebDriverProvider;
import com.example.webmagic.service.WebDriverService;
import com.example.webmagic.util.SliderHandler;
import com.example.webmagic.util.UrlUtil;
import com.example.webmagic.util.UserAgentUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

@Component
public class CompanyDetailProcessor implements PageProcessor {
    @Autowired
    private WebDriverProvider webDriverProvider;  // 注入 WebDriverProvider

    @Autowired
    private SliderHandler sliderHandler;  // 注入 SliderHandler
    @Autowired
    private UrlUtil urlUtil;  // 注入 UrlUtil


    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setUserAgent(UserAgentUtils.randomUserAgent());

    private Random random = new Random();  // 创建一个 Random 对象来生成随机数

    @Override
    public void process(Page page) {
        // 使用 urlUtil 确保 URL 是 HTTPS
        String httpsUrl = urlUtil.ensureHttps(page.getUrl().toString());

        // 检查URL是否以 "http://company.zhaopin.com" 开头
        if (!page.getUrl().toString().startsWith("https://company.zhaopin.com")) {
            return;  // 如果不是，直接返回，跳过后续的解析逻辑
        }

        WebDriver driver = webDriverProvider.getWebDriver();
        try {
            driver.get(httpsUrl);  // 使用处理过的 HTTPS URL
            boolean isSliderPresent = isSliderPresent(driver);
            if (isSliderPresent) {
                sliderHandler.handleSlider(driver);  // 调用 SliderHandler 来处理滑块
            }

            // 定义你想要等待出现的元素的选择器
            By companyNameSelector = By.cssSelector("div.base-info");


            // 设置最大等待时间（例如，10秒）
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

            // 等待直到元素出现
            wait.until(ExpectedConditions.visibilityOfElementLocated(companyNameSelector));

            int randomWaitTime = 1000 + random.nextInt(2000);  // 生成一个随机的等待时间，范围是1000到3000毫秒（1到3秒）
            Thread.sleep(randomWaitTime);

            // 从WebDriver实例获取页面源代码
            System.out.println("Current URL: " + driver.getCurrentUrl());
            System.out.println("Current Title: " + driver.getTitle());
            String pageSource = driver.getPageSource();
            Document document = Jsoup.parse(pageSource);

            // 提取公司详细信息
            String establishmentDateStr = document.select("li:has(label:contains(成立时间)) em").text();
            String registeredCapital = document.select("li:has(label:contains(注册资本)) em").text();
            String legalRepresentative = document.select("li:has(label:contains(法人代表)) em").text();
            String companyAddress = document.select("div.map-box address").text();
            // 提取公司的信息
            String companyIntroduce = document.select("div.company-show__content__description").text();

            // 将提取的数据存储在Page对象中
            page.putField("establishmentDateStr", establishmentDateStr);
            page.putField("registeredCapital", registeredCapital);
            page.putField("legalRepresentative", legalRepresentative);
            page.putField("companyAddress", companyAddress);
            // 将提取的公司信息数据存储在Page对象中
            page.putField("companyIntroduce", companyIntroduce);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*@Override
    public void process(Page page) {
        // 检查URL是否以 "http://company.zhaopin.com" 开头
        if (!page.getUrl().toString().startsWith("http://company.zhaopin.com")) {
            return;  // 如果不是，直接返回，跳过后续的解析逻辑
        }

        WebDriver driver = webDriverProvider.getWebDriver();
        try {
            driver.get(page.getUrl().toString());  // 确保 WebDriver 导航到正确的页面
            boolean isSliderPresent = isSliderPresent(driver);
            if (isSliderPresent) {
                sliderHandler.handleSlider(driver);  // 调用 SliderHandler 来处理滑块
            }
            int sleepTime = 3000 + random.nextInt(5000);  // 生成一个随机的等待时间，范围是2000到7000毫秒
            Thread.sleep(sleepTime);  // 暂停执行

            // 从WebDriver实例获取页面源代码
            System.out.println("Current URL: " + driver.getCurrentUrl());
            System.out.println("Current Title: " + driver.getTitle());
            String pageSource = driver.getPageSource();
            Document document = Jsoup.parse(pageSource);

            // 提取公司详细信息
            String establishmentDateStr = document.select("li:has(label:contains(成立时间)) em").text();
            String registeredCapital = document.select("li:has(label:contains(注册资本)) em").text();
            String legalRepresentative = document.select("li:has(label:contains(法人代表)) em").text();
            String companyAddress = document.select("div.map-box address").text();
            // 提取公司的信息
            String companyIntroduce = document.select("div.company-show__content__description").text();

            // 将提取的数据存储在Page对象中
            page.putField("establishmentDateStr", establishmentDateStr);
            page.putField("registeredCapital", registeredCapital);
            page.putField("legalRepresentative", legalRepresentative);
            page.putField("companyAddress", companyAddress);
            // 将提取的公司信息数据存储在Page对象中
            page.putField("companyIntroduce", companyIntroduce);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    public boolean isSliderPresent(WebDriver driver) {
        // 使用 findElements 方法查找元素
        List<WebElement> sliders = driver.findElements(By.id("nc_1_nocaptcha"));
        // 检查返回的列表是否为空
        return !sliders.isEmpty();
    }



    @Override
    public Site getSite() {
        return site;
    }


}

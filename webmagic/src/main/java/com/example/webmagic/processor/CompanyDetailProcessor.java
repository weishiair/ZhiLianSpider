package com.example.webmagic.processor;

import com.example.webmagic.config.WebDriverProvider;
import com.example.webmagic.service.WebDriverService;
import com.example.webmagic.util.SliderHandler;
import com.example.webmagic.util.UrlUtil;
import com.example.webmagic.util.UserAgentUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
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
        String httpsUrl = urlUtil.ensureHttps(page.getUrl().toString());
        if (!httpsUrl.startsWith("https://company.zhaopin.com")) {
            return;  // 如果不是智联招聘公司页，直接返回
        }

        WebDriver driver = webDriverProvider.getWebDriver();
        try {
            driver.get(httpsUrl);
            if (isSliderPresent(driver)) {
                sliderHandler.handleSlider(driver);
            }

            By companyNameSelector = By.cssSelector("div.base-info");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(companyNameSelector));
            } catch (TimeoutException e) {
                System.out.println("等待元素超时，跳过页面: " + httpsUrl);
                return; // 超时则直接跳过后续代码执行
            }

            int randomWaitTime = 1000 + random.nextInt(2000);
            Thread.sleep(randomWaitTime);

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

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 重新设置中断状态
        } catch (Exception e) {
            System.err.println("处理页面时发生异常: " + httpsUrl);
            e.printStackTrace(); // 打印其他异常的堆栈跟踪
        }
    }

    private boolean isSliderPresent(WebDriver driver) {
        List<WebElement> sliders = driver.findElements(By.id("nc_1_nocaptcha"));
        return !sliders.isEmpty();
    }

    @Override
    public Site getSite() {
        return site;
    }
}

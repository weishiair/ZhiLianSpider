package com.example.webmagic.processor;

import com.example.webmagic.config.WebDriverProvider;
import com.example.webmagic.service.WebDriverService;
import com.example.webmagic.util.SliderHandler;
import com.example.webmagic.util.UrlUtil;
import com.example.webmagic.util.UserAgentUtils;
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
import us.codecraft.webmagic.selector.Html;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

@Component
public class JobDetailProcessor implements PageProcessor {

    @Autowired
    private WebDriverProvider webDriverProvider;  // 注入 WebDriverProvider

    @Autowired
    private SliderHandler sliderHandler;  // 注入 SliderHandler
    @Autowired
    private UrlUtil urlUtil;  // 注入 UrlUtil


    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setUserAgent(UserAgentUtils.randomUserAgent());
    ;

    private Random random = new Random();  // 创建一个 Random 对象来生成随机数

    @Override
    public void process(Page page) {
        // 使用 urlUtil 确保 URL 是 HTTPS
        String httpsUrl = urlUtil.ensureHttps(page.getUrl().toString());

        WebDriver driver = webDriverProvider.getWebDriver();
        try {
            driver.get(httpsUrl);  // 使用处理过的 HTTPS URL
            boolean isSliderPresent = isSliderPresent(driver);
            if (isSliderPresent) {
                sliderHandler.handleSlider(driver);  // 调用 SliderHandler 来处理滑块
            }

            // 定义你想要等待出现的元素的选择器
            By jobTitleSelector = By.cssSelector("h3.summary-plane__title");

            // 设置最大等待时间（例如，10秒）
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

            // 等待直到元素出现
            wait.until(ExpectedConditions.visibilityOfElementLocated(jobTitleSelector));
            // 即使元素已经出现，也等待一个随机的时间
            int randomWaitTime = 1000 + random.nextInt(2000);  // 生成一个随机的等待时间，范围是1000到3000毫秒（1到3秒）
            Thread.sleep(randomWaitTime);

            // 从WebDriver实例获取页面源代码
            String pageSource = driver.getPageSource();
            Html html = new Html(pageSource);

            // 现在你可以安全地提取元素的信息
            // 提取工作地点
            String jobLocation = html.xpath("//div[@class='job-address__content']/span[@class='job-address__content-text']/text()").get();
            // 提取职位描述
            List<String> descriptionParts = html.xpath(
                    "//div[@class='describtion__detail-content']//text() " +
                            "| //div[@class='describtion__detail-content']/div//text() " +
                            "| //div[@class='describtion__detail-content']/div/div//text() " +
                            "| //div[@class='describtion__detail-content']/p//text() " +
                            "| //div[@class='describtion__detail-content']/div/p//text() "
            ).all();

            String jobDescription = String.join("\n", descriptionParts).trim();
            // 提取公司主页地址
            String companyWebsite = html.xpath("//a[@class='company__page-site']/@href").get();
            // 提取标签
            List<String> labels = html.xpath("//div[@class='describtion__skills-content']/span[@class='describtion__skills-item']/text()").all();
            String jobLabel = String.join(", ", labels);  // 将提取到的标签用逗号分隔合并为一个字符串

            // 将提取的数据存入Page对象，以便传递给Pipeline
            page.putField("jobLocation", jobLocation);
            page.putField("jobDescription", jobDescription);
            page.putField("companyWebsite", companyWebsite);
            page.putField("labels", jobLabel);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
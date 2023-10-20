package com.example.webmagic.processor;

import com.example.webmagic.service.WebDriverService;
import com.example.webmagic.util.UserAgentUtils;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.List;
import java.util.Random;

@Component
public class JobDetailProcessor implements PageProcessor {

    @Autowired
    private WebDriverService webDriverService;
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setUserAgent(UserAgentUtils.randomUserAgent());
    ;

    private Random random = new Random();  // 创建一个 Random 对象来生成随机数

    @Override
    public void process(Page page) {
        WebDriver driver = webDriverService.getWebDriver();
        try {
            driver.get(page.getUrl().toString());  // 使用driver字段
            int sleepTime = 2000 + random.nextInt(5000);  // 生成一个随机的等待时间，范围是2000到7000毫秒
            Thread.sleep(sleepTime);  // 暂停执行

            // 从WebDriver实例获取页面源代码
            String pageSource = driver.getPageSource();
            Html html = new Html(pageSource);

            // 提取工作地点
            String jobLocation = html.xpath("//div[@class='job-address__content']/span[@class='job-address__content-text']/text()").get();
            // 提取职位描述
            List<String> descriptionParts = html.xpath("//div[@class='describtion__detail-content']//text() | //div[@class='describtion__detail-content']/div//text() | //div[@class='describtion__detail-content']/p//text()").all();
            String jobDescription = String.join("\n", descriptionParts);
            // 提取公司主页地址
            String companyWebsite = html.xpath("//a[@class='company__page-site']/@href").get();

            // 将提取的数据存入Page对象，以便传递给Pipeline
            page.putField("jobLocation", jobLocation);
            page.putField("jobDescription", jobDescription);
            page.putField("companyWebsite", companyWebsite);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
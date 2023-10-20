package com.example.webmagic.downloader;

import com.example.webmagic.service.WebDriverService;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.PlainText;

import java.util.Scanner;

@Component
public class SeleniumDownloader implements Downloader {


    private final WebDriverService webDriverService;

    @Autowired
    public SeleniumDownloader(WebDriverService webDriverService) {
        this.webDriverService = webDriverService;
        System.out.println("WebDriverService is injected: " + (webDriverService != null));
    }


    static {
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
    }

    public void login(WebDriver driver) {
        driver.get("https://passport.zhaopin.com/login?bkUrl=%2F%2Fi.zhaopin.com%2Fblank%3Fhttps%3A%2F%2Fwww.zhaopin.com%2Fbeijing%2F");
        System.out.println("请在浏览器中完成登录，然后在这里输入任意字符并按回车键继续...");
        Scanner scanner = new Scanner(System.in);
        scanner.next();  // 等待用户输入
        scanner.close();  // 关闭scanner以释放资源
    }

    @Override
    public Page download(Request request, Task task) {
        System.out.println("WebDriverService is: " + webDriverService);
        WebDriver driver = webDriverService.getWebDriver();
        System.out.println("WebDriver is: " + driver);
        try {
            login(driver);  // 调用登录方法
            driver.get(request.getUrl());  // 导航到要抓取的页面
            String pageSource = driver.getPageSource();
            Page page = new Page();
            page.setRawText(pageSource);
            page.setUrl(new PlainText(request.getUrl()));
            page.setRequest(request);
            page.putField("webDriver", driver);  // 将WebDriver实例存储在Page对象中
            return page;
        } finally {
            //driver.close();
        }
    }

    @Override
    public void setThread(int threadNum) {

    }
}


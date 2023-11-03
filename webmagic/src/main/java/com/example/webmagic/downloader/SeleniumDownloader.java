package com.example.webmagic.downloader;

import com.example.webmagic.config.WebDriverProvider;
import com.example.webmagic.util.LoginCheckUtil;
import com.example.webmagic.util.LoginUtil;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.PlainText;

@Component
public class SeleniumDownloader implements Downloader {

    private final LoginUtil loginUtil;
    private final LoginCheckUtil checkLoginUtil;
    private final WebDriverProvider webDriverProvider;  // 更新为 WebDriverProvider

    @Autowired
    public SeleniumDownloader(LoginUtil loginUtil, LoginCheckUtil checkLoginUtil, WebDriverProvider webDriverProvider) {
        this.loginUtil = loginUtil;
        this.checkLoginUtil = checkLoginUtil;
        this.webDriverProvider = webDriverProvider;  // 更新为 WebDriverProvider
        System.out.println("SeleniumDownloader constructor called");
    }

    @Override
    public Page download(Request request, Task task) {
        System.out.println("LoginUtil is: " + loginUtil);
        System.out.println("CheckLoginUtil is: " + checkLoginUtil);
        System.out.println("WebDriverProvider is: " + webDriverProvider);  // 更新为 WebDriverProvider

        // 检查WebDriverProvider是否为空
        if (webDriverProvider == null) {
            System.err.println("webDriverProvider is null!");  // 更新错误消息
            return null;  // 或抛出一个异常
        }

        WebDriver driver = webDriverProvider.getWebDriver();  // 从 WebDriverProvider 获取 WebDriver 实例
        System.out.println("WebDriver is: " + driver);

        // 检查WebDriver是否为空
        if (driver == null) {
            System.err.println("WebDriver is null!");
            return null;  // 或抛出一个异常
        }

        // 获取请求的网页内容
        String url = request.getUrl();
        // 检查URL是否以http:开头，如果是则替换为https:
        if (url.startsWith("http:")) {
            url = url.replace("http:", "https:");
        }
        System.out.println("Requesting URL: " + url);
        driver.get(url);  // 使用重用的WebDriver实例获取更新后的URL

        Page page = new Page();
        page.setRawText(driver.getPageSource());
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);

        return page;
    }


    @Override
    public void setThread(int thread) {

    }
}
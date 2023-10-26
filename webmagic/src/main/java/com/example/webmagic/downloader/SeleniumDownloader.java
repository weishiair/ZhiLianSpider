package com.example.webmagic.downloader;

import com.example.webmagic.service.WebDriverService;
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
    private final WebDriverService webDriverService;

    @Autowired
    public SeleniumDownloader(LoginUtil loginUtil, LoginCheckUtil checkLoginUtil, WebDriverService webDriverService) {
        this.loginUtil = loginUtil;
        this.checkLoginUtil = checkLoginUtil;
        this.webDriverService = webDriverService;
        System.out.println("SeleniumDownloader constructor called");
    }
    @Override
    public Page download(Request request, Task task) {
        System.out.println("LoginUtil is: " + loginUtil);
        System.out.println("CheckLoginUtil is: " + checkLoginUtil);
        System.out.println("WebDriverService is: " + webDriverService);

        // 检查WebDriverService是否为空
        if (webDriverService == null) {
            System.err.println("webDriverService is null!");
            return null;  // 或抛出一个异常
        }

        WebDriver driver = webDriverService.getWebDriver();
        System.out.println("WebDriver is: " + driver);

        // 检查WebDriver是否为空
        if (driver == null) {
            System.err.println("WebDriver is null!");
            return null;  // 或抛出一个异常
        }

        // 检查用户是否已登录，如果没有，则尝试登录
        boolean isLoggedIn = checkLoginUtil.isUserLoggedIn();
        if (!isLoggedIn) {
            loginUtil.loginIfNecessary();
        }

        // 获取请求的网页内容
        driver.get(request.getUrl());  // 使用重用的WebDriver实例
        Page page = new Page();
        page.setRawText(driver.getPageSource());
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);

        return page;
    }

    @Override
    public void setThread(int thread) {
        // 此方法可以根据需要实现
    }
}

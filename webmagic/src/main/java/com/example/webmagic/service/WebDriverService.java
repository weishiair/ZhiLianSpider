package com.example.webmagic.service;

import com.example.webmagic.config.WebDriverProvider;
import com.example.webmagic.util.LoginUtil;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class WebDriverService {

    @Autowired
    private WebDriverProvider webDriverProvider;  // 注入 WebDriverProvider
    @Autowired
    private CookieService cookieService;


    private LoginUtil loginUtil;

    @Autowired  // 使用setter方法来注入LoginUtil
    public void setLoginUtil(LoginUtil loginUtil) {
        this.loginUtil = loginUtil;
    }


    /**
     * 从数据库中获取 cookies 并将它们应用到 WebDriver 实例中。
     */
    public void applyCookies() {
        WebDriver webDriver = webDriverProvider.getWebDriver();  // 从 WebDriverProvider 获取 WebDriver 实例
        System.out.println("正在进入 applyCookies 方法");

        // 首先，导航到正确的域，以便你可以设置cookies
        webDriver.navigate().to("https://passport.zhaopin.com");
        System.out.println("首次导航到https://passport.zhaopin.com");

        // 检查当前的URL，并在必要时导航到正确的域
        String currentUrl = webDriver.getCurrentUrl();
        if (!currentUrl.contains("i.zhaopin.com")) {
            webDriver.navigate().to("https://i.zhaopin.com");
            System.out.println("导航到https://i.zhaopin.com: " + webDriver.getCurrentUrl());
        }

        // 从数据库中获取 cookies
        List<com.example.webmagic.domain.Cookie> cookies = cookieService.getAllCookies();  // 假设您的CookieService有一个方法来获取所有的cookies

        if (cookies.isEmpty()) {
            System.err.println("数据库中未找到Cookie，正在尝试登录...");
            loginUtil.loginIfNecessary();  // 修改这里
            // 重新从数据库获取 cookies，因为 loginIfNecessary 方法可能已经保存了新的 cookies
            cookies = cookieService.getAllCookies();
            if (cookies.isEmpty()) {
                // 如果仍然没有 cookies，可能需要处理这种情况，例如通过抛出异常或记录错误
                System.err.println("登录尝试后仍未找到 Cookie，正在终止 applyCookies 方法。");
                return;
            }
        }

        // 将每个 cookie 应用到 WebDriver 实例中
        for (com.example.webmagic.domain.Cookie cookie : cookies) {
            // 将你自定义的 Cookie 对象转换为 Selenium 的 Cookie 对象
            Cookie seleniumCookie = new Cookie.Builder(cookie.getName(), cookie.getValue())
                    .domain(".zhaopin.com")  // 注意这里的域设置
                    .path(cookie.getPath())
                    .expiresOn(cookie.getExpiry() != null ? new Date(cookie.getExpiry().getTime()) : null)
                    .isSecure(cookie.getSecure())
                    .isHttpOnly(cookie.getHttpOnly())
                    .build();

            webDriver.manage().addCookie(seleniumCookie);
            System.out.println("Added cookie: " + seleniumCookie);

            // 等待一段时间，以便网站有机会处理cookies并可能执行重定向
            try {
                Thread.sleep(1000);  // 等待1秒，或者你认为合适的时间
            } catch (InterruptedException e) {
                e.printStackTrace();  // 或者你可以选择其他方式来处理这个异常
            }

            System.out.println("添加cookie后的当前网址： " + webDriver.getCurrentUrl());
        }
        // 在循环结束后尝试刷新页面
        webDriver.navigate().refresh();
        System.out.println("刷新页面: " + webDriver.getCurrentUrl());

        // 或者尝试导航到你想要的 URL
        webDriver.navigate().to("https://i.zhaopin.com");
        System.out.println("导航至https://i.zhaopin.com: " + webDriver.getCurrentUrl());


        try {
            // 等待一段时间，以确保网站有机会处理cookies并重定向用户
            Thread.sleep(5000);  // 等待5秒，或者你认为合适的时间
        } catch (InterruptedException e) {
            e.printStackTrace();  // 或者你可以选择其他方式来处理这个异常
        }

        // 检查当前的URL
        if (currentUrl.startsWith("https://i.zhaopin.com")) {
            // 如果在i.zhaopin.com，就不再导航了
            System.out.println("已在i.zhaopin.com,不需要进一步导航.");
            return;
        }
    }

}

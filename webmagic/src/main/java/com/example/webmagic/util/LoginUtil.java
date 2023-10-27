package com.example.webmagic.util;

import com.example.webmagic.config.WebDriverProvider;
import com.example.webmagic.service.CookieService;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Scanner;
import java.util.Set;

@Component
public class LoginUtil {


    @Autowired
    private WebDriverProvider webDriverProvider;  // 注入 WebDriverProvider

    @Autowired
    private CookieService cookieService;
    @Autowired
    private LoginCheckUtil loginCheckUtil;


    /**
     * 如果用户未登录，则执行登录操作
     */
    public void loginIfNecessary() {
        // 检查登录状态
        boolean isLoggedIn = loginCheckUtil.isUserLoggedIn();
        if (isLoggedIn) {
            System.out.println("User is already logged in.");
            return;
        }

        System.out.println("进入登录方法");

        WebDriver webDriver = webDriverProvider.getWebDriver();  // 从 WebDriverProvider 获取 WebDriver 实例
        webDriver.get("https://passport.zhaopin.com/login?bkUrl=%2F%2Fi.zhaopin.com%2Fblank%3Fhttps%3A%2F%2Fwww.zhaopin.com%2Fbeijing%2F");

        // 提示用户在浏览器中完成登录操作
        System.out.println("请在浏览器中完成登录，然后在这里输入任意字符并按回车键继续...");

        // 创建一个 Scanner 对象来读取用户的输入
        Scanner scanner = new Scanner(System.in);

        // 等待用户输入任意字符并按回车键
        scanner.next();

        // 等待
        try {
            System.out.println("等待浏览器处理登录信息...");
            Thread.sleep(5000);  //等待5秒。
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // 获取并保存 cookie
        Set<Cookie> seleniumCookies = webDriver.manage().getCookies();
        for (Cookie seleniumCookie : seleniumCookies) {
            com.example.webmagic.domain.Cookie cookie = new com.example.webmagic.domain.Cookie();  // 使用您自定义的Cookie类
            cookie.setDomain(seleniumCookie.getDomain());
            cookie.setPath(seleniumCookie.getPath());
            cookie.setName(seleniumCookie.getName());
            cookie.setValue(seleniumCookie.getValue());
            if (seleniumCookie.getExpiry() != null) {
                cookie.setExpiry(new Timestamp(seleniumCookie.getExpiry().getTime()));
            }
            cookie.setSecure(seleniumCookie.isSecure());
            cookie.setHttpOnly(seleniumCookie.isHttpOnly());
            cookieService.saveCookie(cookie);  // 用CookieService来保存cookie
        }
        System.out.println("离开登录方法");
    }
}
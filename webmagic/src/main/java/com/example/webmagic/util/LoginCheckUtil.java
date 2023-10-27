package com.example.webmagic.util;

import com.example.webmagic.config.WebDriverProvider;
import com.example.webmagic.service.CookieService;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class LoginCheckUtil {

    @Autowired
    private WebDriverProvider webDriverProvider;

    @Autowired
    private CookieService cookieService;  // 注入CookieService

    public boolean isUserLoggedIn() {
        System.out.println("进入检查方法");
        WebDriver driver = webDriverProvider.getWebDriver();
        driver.get("https://www.zhaopin.com/");  // 替换为你的网站URL

        // 创建WebDriverWait实例，指定最长等待时间
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        try {
            // 等待直到指定的元素出现在页面上
            WebElement userElement = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector(".home-header__c-login"))
            );
            System.out.println("离开检查方法");
            return true;  // 如果元素存在，返回true
        } catch (TimeoutException e) {
            // 如果等待时间超时，元素仍未出现，捕获TimeoutException
            System.err.println("Cookie可能已失效，尝试重新登录...");

            // 删除所有旧的cookie
            cookieService.deleteAllCookies();
            // 尝试重新登录以获取新的cookie
            //loginUtil.loginIfNecessary();
            System.out.println("离开检查方法");
            return false;  // 如果元素不存在，返回false
        }
    }
}

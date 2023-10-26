package com.example.webmagic.util;

import com.example.webmagic.service.WebDriverService;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
public class LoginCheckUtil {

    @Autowired
    private WebDriverService webDriverService;

    /**
     * 检查用户是否已登录
     *
     * @return 如果用户已登录，返回true，否则返回false
     */
    public boolean isUserLoggedIn() {
        System.out.println("进入检查方法");
        WebDriver driver = webDriverService.getWebDriver();
        driver.get("https://www.zhaopin.com/");  // 替换为你的网站URL

        // 创建WebDriverWait实例，并指定最长等待时间（例如10秒）
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
            System.out.println("离开检查方法");
            return false;  // 如果元素不存在，返回false
        }
    }
}
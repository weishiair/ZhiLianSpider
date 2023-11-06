package com.example.webmagic.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
/*
* 将webdriver单独提出来管理
* */

@Component
public class WebDriverProvider {

    private WebDriver webDriver;

    public WebDriver getWebDriver() {
        // 检查webDriver实例是否已经存在，如果不存在则创建它
        if (webDriver == null) {
            initWebDriver();
        }
        return webDriver;
    }

    private void initWebDriver() {
        //修改为读取默认位置
        //System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("--remote-allow-origins=*");
        webDriver = new ChromeDriver(options);
        System.out.println("WebDriver is initialized: " + (webDriver != null));
    }

    @PreDestroy
    public void destroy() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}

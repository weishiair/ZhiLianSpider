package com.example.webmagic.service;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class WebDriverService {

    private WebDriver webDriver;



    @PostConstruct  // 该方法在Spring容器初始化该bean后立即执行
    public void init() {
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("--remote-allow-origins=*");
        webDriver = new ChromeDriver(options);
        System.out.println("WebDriver is initialized: " + (webDriver != null));

    }





    public WebDriver getWebDriver() {
        System.out.println("Returning WebDriver: " + webDriver);
        return webDriver;
    }

    @PreDestroy  // 该方法在Spring容器销毁该bean前执行
    public void destroy() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}

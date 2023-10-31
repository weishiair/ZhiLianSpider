package com.example.webmagic.util;

import com.example.webmagic.config.WebDriverProvider;
import com.example.webmagic.scheduler.CookieTestTask;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.Set;

@Component
public class RandomClicker implements Runnable {

    @Autowired
    private WebDriverProvider webDriverProvider;

    private boolean running = true;  // 控制循环的标志

    @Override
    public void run() {
        Random random = new Random();
        while (running) {
            try {
                // 计算随机点击的时间点（0到30分钟之间）
                long randomTime = random.nextInt(30 * 60 * 1000);
                System.out.println("Random click will be executed in " + (randomTime / 1000) + " seconds.");

                // 等待随机时间
                Thread.sleep(randomTime);

                WebDriver driver = webDriverProvider.getWebDriver();
                String listPageUrl = CookieTestTask.getListPageUrl();  // 从CookieTestTask类获取列表页面的URL
                if (listPageUrl != null) {
                    driver.get(listPageUrl);  // 在执行随机点击前返回列表页面
                }

                // 执行随机点击
                List<WebElement> clickableElements = driver.findElements(By.xpath("//a"));
                if (!clickableElements.isEmpty()) {
                    WebElement randomElement = clickableElements.get(random.nextInt(clickableElements.size()));
                    try {
                        randomElement.click();
                        System.out.println("Random click executed.");  // 打印随机点击执行的消息
                    } catch (Exception e) {
                        System.err.println("Error during random click: " + e.getMessage());
                    }
                } else {
                    System.err.println("No clickable elements found for random click.");
                }

                // 管理窗口
                manageWindows(driver);

            } catch (InterruptedException e) {
                System.err.println("Random clicker interrupted: " + e.getMessage());
            }
        }
    }

    public void stop() {
        running = false;
    }

    private void manageWindows(WebDriver driver) {
        Set<String> windowHandles = driver.getWindowHandles();
        String originalWindowHandle = driver.getWindowHandle();

        if (windowHandles.size() > 1) {
            for (String windowHandle : windowHandles) {
                if (!windowHandle.equals(originalWindowHandle)) {
                    driver.switchTo().window(windowHandle);
                    driver.close();
                }
            }
            // Switch back to the original window
            driver.switchTo().window(originalWindowHandle);
        }

        String listPageUrl = CookieTestTask.getListPageUrl();  // 从CookieTestTask类获取列表页面的URL
        if (listPageUrl != null) {
            driver.get(listPageUrl);  // 返回列表页面
        }
    }
}

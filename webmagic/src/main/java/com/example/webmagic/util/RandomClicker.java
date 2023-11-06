package com.example.webmagic.util;

import com.example.webmagic.config.WebDriverProvider;
import com.example.webmagic.scheduler.CookieTestTask;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RandomClicker implements Runnable {

    @Autowired
    private WebDriverProvider webDriverProvider;

    private boolean running = true;

    @Override
    public void run() {
        Random random = new Random();
        while (running) {
            try {
                long randomTime = random.nextInt(30 * 60 * 1000);
                System.out.println("随机点击将在 " + (randomTime / 1000) + " 秒后执行。");
                Thread.sleep(randomTime);

                WebDriver driver = webDriverProvider.getWebDriver();
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

                String listPageUrl = CookieTestTask.getListPageUrl();
                if (listPageUrl != null) {
                    driver.get(listPageUrl);
                }

                List<WebElement> clickableElements = findClickableElements(driver);

                if (!clickableElements.isEmpty()) {
                    WebElement randomElement = clickableElements.get(random.nextInt(clickableElements.size()));
                    scrollToElementWithJS(driver, randomElement); // 滚动到元素位置
                    simulateMouseMovement(driver, randomElement); // 模拟鼠标移动到元素上
                    waitBeforeClick(random);

                    try {
                        wait.until(ExpectedConditions.elementToBeClickable(randomElement));
                        randomElement.click();
                    } catch (ElementClickInterceptedException e) {
                        clickWithJS(driver, randomElement); // 使用JS点击
                    }

                    System.out.println("随机点击执行成功。");
                } else {
                    System.err.println("没有发现可以点击的元素。");
                }

                manageWindows(driver);
            } catch (InterruptedException e) {
                System.err.println("随机点击中断: " + e.getMessage());
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.err.println("在执行随机点击过程中出现异常: " + e.getMessage());
            }
        }
    }
    private void simulateMouseMovement(WebDriver driver, WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element).perform();
    }

    private List<WebElement> findClickableElements(WebDriver driver) {
        // 找到所有的可点击且可见的元素
        List<WebElement> allElements = driver.findElements(By.xpath("//a | //button"));
        List<WebElement> clickableAndVisibleElements = allElements.stream()
                .filter(WebElement::isDisplayed)
                .filter(e -> {
                    try {
                        return ExpectedConditions.elementToBeClickable(e).apply(driver) != null;
                    } catch (Exception ex) {
                        return false;
                    }
                }).collect(Collectors.toList());

        // 找到特定的三个元素
        List<WebElement> preferredElements = driver.findElements(By.xpath("//div[@class='joblist-box__item clearfix']/a | //div[@class='listsort']/ul/li/a | //div[@class='iteminfo__line1__compname']/span[@class='iteminfo__line1__compname__name']"));

        // 增加特定元素的权重，例如重复添加5次，这样它们就有更大的机会被选中
        for (int i = 0; i < 5; i++) {
            clickableAndVisibleElements.addAll(preferredElements);
        }

        return clickableAndVisibleElements;
    }




    private void scrollToElementWithJS(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    private void waitBeforeClick(Random random) throws InterruptedException {
        int timeToWait = random.nextInt(2000) + 500; // 确保至少等待500毫秒
        Thread.sleep(timeToWait);
    }

    public void stop() {
        running = false;
    }

    private void manageWindows(WebDriver driver) {
        Set<String> windowHandles = driver.getWindowHandles();
        String originalWindowHandle = driver.getWindowHandle();

        if (windowHandles.size() > 3) {
            for (String windowHandle : windowHandles) {
                if (!windowHandle.equals(originalWindowHandle)) {
                    driver.switchTo().window(windowHandle);
                    driver.close();
                }
            }
            driver.switchTo().window(originalWindowHandle);
        }

        String listPageUrl = CookieTestTask.getListPageUrl();
        if (listPageUrl != null) {
            driver.get(listPageUrl);
        }
    }

    private void clickWithJS(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }
}

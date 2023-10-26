package com.example.webmagic.util;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SliderHandler {

    private Random random = new Random();

    public void handleSlider(WebDriver driver) {
        WebElement slider = driver.findElement(By.id("nc_1_n1z"));
        Actions actions = new Actions(driver);

        int baseOffset = 276;  // 基础的位移量
        int randomOffset = random.nextInt(21) - 10;  // 生成一个-10到10的随机数
        int totalOffset = baseOffset + randomOffset;  // 总的位移量是基础位移量加上随机偏移

        int stages = 3;  // 将拖动操作分为3个阶段
        int offsetPerStage = totalOffset / stages;  // 每个阶段的位移量

        actions.clickAndHold(slider).perform();  // 点击并按住滑块

        for (int i = 0; i < stages; i++) {
            // 在每个阶段移动滑块
            actions.moveByOffset(offsetPerStage + random.nextInt(5) - 2, random.nextInt(3) - 1).perform();  // 添加随机偏移
            sleepRandom();  // 随机暂停
        }

        actions.release().perform();  // 释放滑块
    }

    private void sleepRandom() {
        try {
            Thread.sleep(100 + random.nextInt(200));  // 随机暂停100到300毫秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

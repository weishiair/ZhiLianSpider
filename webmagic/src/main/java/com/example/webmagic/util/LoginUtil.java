package com.example.webmagic.util;

import com.example.webmagic.service.WebDriverService;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class LoginUtil {

    @Autowired
    private WebDriverService webDriverService;

    /**
     * 如果用户未登录，则执行登录操作
     */
    public void loginIfNecessary() {
        System.out.println("进入登录方法");
        WebDriver driver = webDriverService.getWebDriver();
        driver.get("https://passport.zhaopin.com/login?bkUrl=%2F%2Fi.zhaopin.com%2Fblank%3Fhttps%3A%2F%2Fwww.zhaopin.com%2Fbeijing%2F");

        // 你可以在这里添加更多的登录逻辑，例如填充用户名和密码字段，然后点击登录按钮。
        // 以下是一个等待用户手动登录的简单示例。

        // 提示用户在浏览器中完成登录操作
        System.out.println("请在浏览器中完成登录，然后在这里输入任意字符并按回车键继续...");

        // 创建一个 Scanner 对象来读取用户的输入
        Scanner scanner = new Scanner(System.in);

        // 等待用户输入任意字符并按回车键
        scanner.next();

        // 关闭 Scanner 对象以释放资源
        scanner.close();
        System.out.println("离开登录方法");
    }
}
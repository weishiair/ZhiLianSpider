package com.example.webmagic;

import com.example.webmagic.util.RandomClicker;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication  // 标记这是一个Spring Boot应用程序
@EnableScheduling  // 启用定时任务支持
@MapperScan("com.example.webmagic.mapper")
public class Application implements CommandLineRunner {

    @Autowired
    private RandomClicker randomClicker;  // 注入RandomClicker类

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);  // 启动Spring Boot应用程序
    }

    @Override
    public void run(String... args) throws Exception {
        // 启动随机点击线程
        new Thread(randomClicker).start();
    }
}

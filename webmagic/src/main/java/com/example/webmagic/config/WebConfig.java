package com.example.webmagic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 对所有的路径应用CORS配置
                        .allowedOrigins("http://localhost:8081") // 允许来自8080端口的跨域请求
                        .allowedMethods("GET", "POST", "PUT", "DELETE"); // 允许的HTTP方法
            }
        };
    }
}

package com.example.webmagic.config;

import org.springframework.stereotype.Component;

@Component
public class SearchConfig {
    private String city;
    private String keyword;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
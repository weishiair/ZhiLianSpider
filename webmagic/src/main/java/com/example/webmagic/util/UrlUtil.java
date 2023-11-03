package com.example.webmagic.util;

import org.springframework.stereotype.Component;

@Component
public class UrlUtil {

    /**
     * 确保给定的 URL 是 HTTPS
     *
     * @param url 原始 URL
     * @return 如果原始 URL 是 HTTP，则返回 HTTPS 版本的 URL；否则返回原始 URL
     */
    public String ensureHttps(String url) {
        if (url != null && url.startsWith("http://")) {
            return "https://" + url.substring(7);
        }
        return url;
    }
}


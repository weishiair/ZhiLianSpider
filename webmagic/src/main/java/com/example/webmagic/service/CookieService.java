package com.example.webmagic.service;

import com.example.webmagic.domain.Cookie;
import com.example.webmagic.mapper.CookieMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CookieService {

    @Autowired
    private CookieMapper cookieMapper;

    @Transactional
    public void saveCookie(Cookie cookie) {
        cookieMapper.insertCookie(cookie);
    }

    @Transactional(readOnly = true)
    public List<Cookie> getCookies(String domain, String path) {
        return cookieMapper.findByDomainAndPath(domain, path);
    }

    @Transactional
    public void deleteCookie(Integer id) {
        cookieMapper.deleteCookie(id);
    }

    @Transactional
    public void deleteExpiredCookies() {
        cookieMapper.deleteExpiredCookies();
    }

    @Transactional(readOnly = true)
    public List<Cookie> getAllCookies() {
        return cookieMapper.findAllCookies();
    }
}
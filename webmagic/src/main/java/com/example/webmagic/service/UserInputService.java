package com.example.webmagic.service;

import org.springframework.stereotype.Service;

import java.util.Scanner;
/*
* 用户输入服务
* */
@Service
public class UserInputService {

    public String getCity() {
        System.out.print("请输入城市: ");
        Scanner scanner = new Scanner(System.in);
        String city = scanner.nextLine();
        return city;
    }

    public String getKeyword() {
        System.out.print("请输入关键字: ");
        Scanner scanner = new Scanner(System.in);
        String keyword = scanner.nextLine();
        return keyword;
    }
}
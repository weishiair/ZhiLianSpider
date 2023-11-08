package com.example.webmagic.controller;

import com.example.webmagic.domain.UserConfig;
import com.example.webmagic.service.UserConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@RestController
@RequestMapping("/user-config")
public class UserConfigController {

    private final UserConfigService userConfigService;

    @Autowired
    public UserConfigController(UserConfigService userConfigService) {
        this.userConfigService = userConfigService;
    }

    // 创建用户配置
    @PostMapping("/")
    public boolean createUserConfig(@RequestBody UserConfig userConfig) {
        // 这里可以添加创建前的逻辑检查
        return userConfigService.save(userConfig);
    }

    // 删除用户配置
    @DeleteMapping("/{id}")
    public boolean deleteUserConfig(@PathVariable("id") Long id) {
        // 这里可以添加删除前的逻辑检查
        return userConfigService.removeById(id);
    }

    // 更新用户配置
    @PutMapping("/")
    public boolean updateUserConfig(@RequestBody UserConfig userConfig) {
        // 这里可以添加更新前的逻辑检查
        return userConfigService.updateById(userConfig);
    }

    // 根据ID获取用户配置
    @GetMapping("/{id}")
    public UserConfig getUserConfig(@PathVariable("id") Long id) {
        // 这里可以添加获取前的逻辑检查
        return userConfigService.getById(id);
    }

    // 分页查询用户配置
    @GetMapping("/page")
    public IPage<UserConfig> getUserConfigPage(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        // 创建分页构造器
        Page<UserConfig> page = new Page<>(pageNo, pageSize);
        // 调用service层的分页查询方法
        return userConfigService.page(page);
    }
}

package com.example.webmagic.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.webmagic.domain.UserConfig;
import com.example.webmagic.service.TaskScheduleService;
import com.example.webmagic.service.UserConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
@CrossOrigin(origins = "http://localhost:8081") // 允许8080端口的跨域请求
@RestController
@RequestMapping("/api/userconfig")
public class UserConfigController {

    private final UserConfigService userConfigService;
    private final TaskScheduleService taskScheduleService;

    @Autowired
    public UserConfigController(UserConfigService userConfigService, TaskScheduleService taskScheduleService) {
        this.userConfigService = userConfigService;
        this.taskScheduleService = taskScheduleService;
    }
    // 创建用户配置
    @PostMapping("/")
    public boolean createUserConfig(@RequestBody UserConfig userConfig) {
        return userConfigService.save(userConfig);
    }

    // 删除用户配置（软删除）
    @DeleteMapping("/{id}")
    public boolean deleteUserConfig(@PathVariable("id") Long id) {
        UserConfig userConfig = userConfigService.getById(id);
        if (userConfig != null) {
            userConfig.setDeleteFlag("Y");
            return userConfigService.updateById(userConfig);
        }
        return false;
    }

    // 更新用户配置
    @PutMapping("/")
    public boolean updateUserConfig(@RequestBody UserConfig userConfig) {
        return userConfigService.updateById(userConfig);
    }

    // 根据ID获取用户配置
    @GetMapping("/{id}")
    public UserConfig getUserConfig(@PathVariable("id") Long id) {
        return userConfigService.getById(id);
    }

    // 分页查询用户配置
    @GetMapping("/page")
    public IPage<UserConfig> getUserConfigPage(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<UserConfig> page = new Page<>(pageNo, pageSize);
        QueryWrapper<UserConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("delete_flag", "N");
        return userConfigService.page(page, queryWrapper);
    }
    // 添加用户配置与任务计划的关联
    @PostMapping("/{userConfigId}/task-schedules/{taskScheduleId}")
    public ResponseEntity<?> addUserConfigTaskSchedule(
            @PathVariable Integer userConfigId,
            @PathVariable Integer taskScheduleId) {
        boolean isSuccess = userConfigService.addTaskScheduleToUserConfig(userConfigId, taskScheduleId);
        return isSuccess ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("Failed to add task schedule to user config.");
    }

    // 删除用户配置与任务计划的关联
    @DeleteMapping("/{userConfigId}/task-schedules/{taskScheduleId}")
    public ResponseEntity<?> removeUserConfigTaskSchedule(
            @PathVariable Integer userConfigId,
            @PathVariable Integer taskScheduleId) {
        boolean isSuccess = userConfigService.removeTaskScheduleFromUserConfig(userConfigId, taskScheduleId);
        return isSuccess ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("Failed to remove task schedule from user config.");
    }
}

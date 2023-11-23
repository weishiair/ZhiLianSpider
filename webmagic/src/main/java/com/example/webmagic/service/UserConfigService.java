package com.example.webmagic.service;

import com.example.webmagic.domain.UserConfig;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 10244
* @description 针对表【user_config(用户配置表)】的数据库操作Service
* @createDate 2023-11-07 16:22:43
*/
public interface UserConfigService extends IService<UserConfig> {

    List<UserConfig> listActiveConfigs();
    boolean addTaskScheduleToUserConfig(Integer userConfigId, Integer taskScheduleId);

    boolean removeTaskScheduleFromUserConfig(Integer userConfigId, Integer taskScheduleId);

    List<UserConfig> searchConfigs(String searchValue);
}


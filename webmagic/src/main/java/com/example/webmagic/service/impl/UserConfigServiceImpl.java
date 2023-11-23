package com.example.webmagic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.webmagic.domain.UserConfig;
import com.example.webmagic.domain.UserConfigSchedule;
import com.example.webmagic.mapper.TaskScheduleMapper;
import com.example.webmagic.mapper.UserConfigScheduleMapper;
import com.example.webmagic.service.UserConfigService;
import com.example.webmagic.mapper.UserConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
* @author 10244
* @description 针对表【user_config(用户配置表)】的数据库操作Service实现
* @createDate 2023-11-07 16:22:43
*/
@Service
public class UserConfigServiceImpl extends ServiceImpl<UserConfigMapper, UserConfig>
    implements UserConfigService{
    private final UserConfigMapper userConfigMapper;
    private final TaskScheduleMapper taskScheduleMapper;
    private final UserConfigScheduleMapper userConfigScheduleMapper;

    @Autowired
    public UserConfigServiceImpl(UserConfigMapper userConfigMapper, TaskScheduleMapper taskScheduleMapper, UserConfigScheduleMapper userConfigScheduleMapper) {
        this.userConfigMapper = userConfigMapper;
        this.taskScheduleMapper = taskScheduleMapper;
        this.userConfigScheduleMapper = userConfigScheduleMapper;
    }
    @Override
    public List<UserConfig> listActiveConfigs() {
        QueryWrapper<UserConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("delete_flag", "N");
        return list(queryWrapper);
    }
    @Override
    public boolean addTaskScheduleToUserConfig(Integer userConfigId, Integer taskScheduleId) {
        // 验证UserConfig是否存在
        if (userConfigMapper.selectById(userConfigId) == null) {
            throw new NoSuchElementException("UserConfig not found with id: " + userConfigId);
        }

        // 验证TaskSchedule是否存在
        if (taskScheduleMapper.selectById(taskScheduleId) == null) {
            throw new NoSuchElementException("TaskSchedule not found with id: " + taskScheduleId);
        }

        // 检查这一对user_config_id和task_schedule_id的关联是否已经存在
        UserConfigSchedule queryResult = userConfigScheduleMapper.selectOne(new QueryWrapper<UserConfigSchedule>()
                .eq("user_config_id", userConfigId)
                .eq("task_schedule_id", taskScheduleId));

        if (queryResult != null) {
            throw new IllegalArgumentException("This association between UserConfig and TaskSchedule already exists.");
        }

        // 添加关联逻辑
        UserConfigSchedule userConfigSchedule = new UserConfigSchedule();
        userConfigSchedule.setUserConfigId(userConfigId);
        userConfigSchedule.setTaskScheduleId(taskScheduleId);
        int result = userConfigScheduleMapper.insert(userConfigSchedule);

        return result > 0;
    }



    @Override
    public boolean removeTaskScheduleFromUserConfig(Integer userConfigId, Integer taskScheduleId) {
        // 查找关联
        UserConfigSchedule queryResult = userConfigScheduleMapper.selectOne(new QueryWrapper<UserConfigSchedule>()
                .eq("user_config_id", userConfigId)
                .eq("task_schedule_id", taskScheduleId));

        if (queryResult == null) {
            throw new NoSuchElementException("The association between UserConfig and TaskSchedule does not exist.");
        }

        // 删除关联
        int result = userConfigScheduleMapper.delete(new QueryWrapper<UserConfigSchedule>()
                .eq("user_config_id", userConfigId)
                .eq("task_schedule_id", taskScheduleId));

        return result > 0;
    }
    @Override
    public List<UserConfig> searchConfigs(String searchValue) {
        QueryWrapper<UserConfig> queryWrapper = new QueryWrapper<>();
        if (searchValue != null && !searchValue.isEmpty()) {
            queryWrapper
                    .like("config_name", searchValue)
                    .or()
                    .like("city", searchValue)
                    .or()
                    .like("keyword", searchValue);
        }
        queryWrapper.eq("delete_flag", "N"); // 只查询未删除的配置
        return list(queryWrapper);
    }

}





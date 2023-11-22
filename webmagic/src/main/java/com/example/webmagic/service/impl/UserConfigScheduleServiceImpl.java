package com.example.webmagic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.webmagic.domain.UserConfigSchedule;
import com.example.webmagic.service.UserConfigScheduleService;
import com.example.webmagic.mapper.UserConfigScheduleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 10244
* @description 针对表【user_config_schedule】的数据库操作Service实现
* @createDate 2023-11-09 09:47:44
*/
@Service
public class UserConfigScheduleServiceImpl extends ServiceImpl<UserConfigScheduleMapper, UserConfigSchedule>
    implements UserConfigScheduleService{

    @Override
    public List<Integer> getTaskScheduleIdsByUserConfigId(Integer userConfigId) {
        return baseMapper.findTaskScheduleIdsByUserConfigId(userConfigId);
    }

}





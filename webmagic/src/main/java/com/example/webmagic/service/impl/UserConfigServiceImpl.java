package com.example.webmagic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.webmagic.domain.UserConfig;
import com.example.webmagic.service.UserConfigService;
import com.example.webmagic.mapper.UserConfigMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 10244
* @description 针对表【user_config(用户配置表)】的数据库操作Service实现
* @createDate 2023-11-07 16:22:43
*/
@Service
public class UserConfigServiceImpl extends ServiceImpl<UserConfigMapper, UserConfig>
    implements UserConfigService{
    @Override
    public List<UserConfig> listActiveConfigs() {
        QueryWrapper<UserConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("delete_flag", "N"); // 假设数据库字段为小写
        return list(queryWrapper);
    }

}





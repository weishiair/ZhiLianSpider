package com.example.webmagic.mapper;

import com.example.webmagic.domain.UserConfigSchedule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 10244
* @description 针对表【user_config_schedule】的数据库操作Mapper
* @createDate 2023-11-09 09:47:44
* @Entity com.example.webmagic.domain.UserConfigSchedule
*/
public interface UserConfigScheduleMapper extends BaseMapper<UserConfigSchedule> {
    @Select("SELECT task_schedule_id FROM user_config_schedule WHERE user_config_id = #{userConfigId}")
    List<Integer> findTaskScheduleIdsByUserConfigId(Integer userConfigId);
}






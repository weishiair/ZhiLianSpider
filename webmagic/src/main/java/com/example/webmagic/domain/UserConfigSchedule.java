package com.example.webmagic.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * 
 * @TableName user_config_schedule
 */
@TableName(value ="user_config_schedule")
public class UserConfigSchedule implements Serializable {
    private static final long serialVersionUID = 1L;

    public Integer getUserConfigId() {
        return userConfigId;
    }

    public void setUserConfigId(Integer userConfigId) {
        this.userConfigId = userConfigId;
    }

    public Integer getTaskScheduleId() {
        return taskScheduleId;
    }

    public void setTaskScheduleId(Integer taskScheduleId) {
        this.taskScheduleId = taskScheduleId;
    }

    private Integer userConfigId;
    private Integer taskScheduleId;

}

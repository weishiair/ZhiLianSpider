package com.example.webmagic.service;

import com.example.webmagic.domain.TaskSchedule;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 10244
* @description 针对表【task_schedule】的数据库操作Service
* @createDate 2023-11-09 09:47:37
*/
public interface TaskScheduleService extends IService<TaskSchedule> {
    boolean updateTaskSchedule(Integer taskScheduleId, TaskSchedule taskScheduleData);

    boolean deleteTaskSchedule(Integer taskScheduleId);

}

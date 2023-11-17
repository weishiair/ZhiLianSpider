package com.example.webmagic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.webmagic.domain.TaskSchedule;
import com.example.webmagic.domain.UserConfigSchedule;
import com.example.webmagic.mapper.TaskScheduleMapper;
import com.example.webmagic.mapper.UserConfigScheduleMapper;
import com.example.webmagic.service.DynamicTaskSchedulingService;
import com.example.webmagic.service.TaskScheduleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
* @author 10244
* @description 针对表【task_schedule】的数据库操作Service实现
* @createDate 2023-11-09 09:47:37
*/
@Service
public class TaskScheduleServiceImpl extends ServiceImpl<TaskScheduleMapper, TaskSchedule>
        implements TaskScheduleService {
    private final DynamicTaskSchedulingService dynamicTaskSchedulingService;

    @Autowired
    public TaskScheduleServiceImpl(DynamicTaskSchedulingService dynamicTaskSchedulingService) {
        this.dynamicTaskSchedulingService = dynamicTaskSchedulingService;
    }

    // 更新任务计划的方法
    public boolean updateTaskSchedule(Integer taskScheduleId, TaskSchedule taskScheduleData) {
        TaskSchedule taskSchedule = this.getById(taskScheduleId);
        if (taskSchedule == null) {
            throw new RuntimeException("TaskSchedule not found with id " + taskScheduleId);
        }

        // 检查cron表达式是否发生了变化
        boolean cronChanged = !taskSchedule.getCronExpression().equals(taskScheduleData.getCronExpression());


        // 更新任务计划数据，忽略id属性
        BeanUtils.copyProperties(taskScheduleData, taskSchedule, "id");
        boolean updated = this.updateById(taskSchedule);
        if (updated && cronChanged) {
            // 如果cron表达式发生了变化，重新调度任务
            dynamicTaskSchedulingService.rescheduleTask(taskScheduleId, taskSchedule.getCronExpression());
        }

        return updated;
    }

    @Autowired
    private UserConfigScheduleMapper userConfigScheduleMapper;

    // 删除任务计划的方法
    public boolean deleteTaskSchedule(Integer taskScheduleId) {
        TaskSchedule taskSchedule = this.getById(taskScheduleId);
        if (taskSchedule == null) {
            throw new RuntimeException("TaskSchedule not found with id " + taskScheduleId);
        }

        // 删除与任务计划相关的用户配置关联
        QueryWrapper<UserConfigSchedule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_schedule_id", taskScheduleId);
        userConfigScheduleMapper.delete(queryWrapper);

        // 取消任务调度
        dynamicTaskSchedulingService.cancelTask(taskScheduleId);

        // 删除任务计划本身
        return this.removeById(taskScheduleId);
    }

    // 创建任务计划的方法
    public boolean createTaskSchedule(TaskSchedule taskSchedule) {
        // 存储新的任务计划
        boolean saved = this.save(taskSchedule);
        if (saved) {
            // 调度新的任务
            dynamicTaskSchedulingService.scheduleTask(
                    taskSchedule.getId(),
                    taskSchedule.getCronExpression()
            );
        }
        return saved;
    }
}





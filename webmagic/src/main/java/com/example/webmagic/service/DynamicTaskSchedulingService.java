package com.example.webmagic.service;

import com.example.webmagic.scheduler.CookieTestTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
public class DynamicTaskSchedulingService {

    private final TaskScheduler taskScheduler;
    private final CookieTestTask cookieTestTask; // 注入CookieTestTask
    private final Map<Integer, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    @Autowired
    public DynamicTaskSchedulingService(TaskScheduler taskScheduler, CookieTestTask cookieTestTask) {
        this.taskScheduler = taskScheduler;
        this.cookieTestTask = cookieTestTask; // 初始化
    }

    public void scheduleTask(Integer taskId, String cronExpression) {
        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(
                () -> {
                    try {
                        cookieTestTask.executeCrawl(); // 调用CookieTestTask的方法
                    } catch (Exception e) {
                        e.printStackTrace(); // 异常处理
                    }
                },
                new CronTrigger(cronExpression)
        );
        scheduledTasks.put(taskId, scheduledTask);
    }

    public void rescheduleTask(Integer taskId, String cronExpression) {
        cancelTask(taskId); // 取消当前任务
        scheduleTask(taskId, cronExpression); // 重新调度任务
    }

    public void cancelTask(Integer taskId) {
        ScheduledFuture<?> scheduledTask = scheduledTasks.get(taskId);
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
            scheduledTasks.remove(taskId);
        }
    }
    public boolean isTaskScheduled(Integer taskId) {
        ScheduledFuture<?> scheduledTask = scheduledTasks.get(taskId);
        return scheduledTask != null && !scheduledTask.isCancelled() && !scheduledTask.isDone();
    }
}


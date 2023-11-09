package com.example.webmagic.controller;

import com.example.webmagic.domain.TaskSchedule;
import com.example.webmagic.service.DynamicTaskSchedulingService;
import com.example.webmagic.service.TaskScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task-schedules")
public class TaskScheduleController {

    private final TaskScheduleService taskScheduleService;
    private final DynamicTaskSchedulingService dynamicTaskSchedulingService;

    @Autowired
    public TaskScheduleController(TaskScheduleService taskScheduleService, DynamicTaskSchedulingService dynamicTaskSchedulingService) {
        this.taskScheduleService = taskScheduleService;
        this.dynamicTaskSchedulingService = dynamicTaskSchedulingService;
    }

    // 获取所有任务计划
    @GetMapping
    public ResponseEntity<?> getAllTaskSchedules() {
        return ResponseEntity.ok(taskScheduleService.list());
    }

    // 根据ID获取单个任务计划
    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskScheduleById(@PathVariable Integer id) {
        TaskSchedule taskSchedule = taskScheduleService.getById(id);
        return taskSchedule != null ? ResponseEntity.ok(taskSchedule) : ResponseEntity.notFound().build();
    }

    // 创建新的任务计划
    @PostMapping
    public ResponseEntity<?> createTaskSchedule(@RequestBody TaskSchedule taskSchedule) {
        boolean isSuccess = taskScheduleService.save(taskSchedule);
        return isSuccess ? ResponseEntity.ok(taskSchedule) : ResponseEntity.badRequest().body("Failed to create task schedule.");
    }

    // 更新任务计划
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTaskSchedule(@PathVariable Integer id, @RequestBody TaskSchedule taskScheduleData) {
        try {
            boolean isSuccess = taskScheduleService.updateTaskSchedule(id, taskScheduleData);
            return isSuccess ? ResponseEntity.ok(taskScheduleData) : ResponseEntity.notFound().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // 删除任务计划
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTaskSchedule(@PathVariable Integer id) {
        try {
            boolean isSuccess = taskScheduleService.deleteTaskSchedule(id);
            return isSuccess ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    // 启动任务
    @PostMapping("/start/{id}")
    public ResponseEntity<?> startTaskSchedule(@PathVariable Integer id) {
        TaskSchedule taskSchedule = taskScheduleService.getById(id);
        if (taskSchedule == null) {
            return ResponseEntity.notFound().build();
        }
        dynamicTaskSchedulingService.scheduleTask(id, taskSchedule.getCronExpression());
        return ResponseEntity.ok().build();
    }

    // 停止任务
    @PostMapping("/stop/{id}")
    public ResponseEntity<?> stopTaskSchedule(@PathVariable Integer id) {
        dynamicTaskSchedulingService.cancelTask(id);
        return ResponseEntity.ok().build();
    }

    // 查看任务状态
    @GetMapping("/status/{id}")
    public ResponseEntity<?> getTaskStatus(@PathVariable Integer id) {
        // 检查任务是否在scheduledTasks映射中，并且是否正在运行
        boolean isScheduled = dynamicTaskSchedulingService.isTaskScheduled(id);
        return ResponseEntity.ok(isScheduled ? "RUNNING" : "STOPPED");
    }
}

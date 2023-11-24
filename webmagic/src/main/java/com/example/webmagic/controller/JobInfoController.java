package com.example.webmagic.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.webmagic.domain.JobInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/jobinfo")
public class JobInfoController {

    @Autowired
    private IService<JobInfo> jobInfoService;

    // 添加工作信息
    @PostMapping("/add")
    public ResponseEntity<?> addJobInfo(@RequestBody JobInfo jobInfo) {
        boolean isSaved = jobInfoService.save(jobInfo);
        return ResponseEntity.ok(isSaved ? "添加成功" : "添加失败");
    }

    // 根据ID删除工作信息
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteJobInfo(@PathVariable Integer id) {
        boolean isRemoved = jobInfoService.removeById(id);
        return ResponseEntity.ok(isRemoved ? "删除成功" : "删除失败");
    }

    // 更新工作信息
    @PutMapping("/update")
    public ResponseEntity<?> updateJobInfo(@RequestBody JobInfo jobInfo) {
        boolean isUpdated = jobInfoService.updateById(jobInfo);
        return ResponseEntity.ok(isUpdated ? "更新成功" : "更新失败");
    }

    // 根据ID获取工作信息
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getJobInfoById(@PathVariable Integer id) {
        JobInfo jobInfo = jobInfoService.getById(id);
        return jobInfo != null ? ResponseEntity.ok(jobInfo) : ResponseEntity.ok("未找到相应工作信息");
    }

    // 获取所有工作信息（分页）
    @GetMapping("/all")
    public ResponseEntity<?> getAllJobInfos(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<JobInfo> jobInfoPage = new Page<>(page, size);
        jobInfoService.page(jobInfoPage);
        return ResponseEntity.ok(jobInfoPage);
    }


}
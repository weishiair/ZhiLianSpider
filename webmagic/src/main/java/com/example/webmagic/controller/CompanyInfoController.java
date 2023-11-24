package com.example.webmagic.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.webmagic.domain.CompanyInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/companyinfo")
public class CompanyInfoController {

    @Autowired
    private IService<CompanyInfo> companyInfoService;

    // 添加公司信息
    @PostMapping("/add")
    public ResponseEntity<?> addCompanyInfo(@RequestBody CompanyInfo companyInfo) {
        boolean isSaved = companyInfoService.save(companyInfo);
        return ResponseEntity.ok(isSaved ? "添加成功" : "添加失败");
    }

    // 根据ID删除公司信息
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCompanyInfo(@PathVariable Integer id) {
        boolean isRemoved = companyInfoService.removeById(id);
        return ResponseEntity.ok(isRemoved ? "删除成功" : "删除失败");
    }

    // 更新公司信息
    @PutMapping("/update")
    public ResponseEntity<?> updateCompanyInfo(@RequestBody CompanyInfo companyInfo) {
        boolean isUpdated = companyInfoService.updateById(companyInfo);
        return ResponseEntity.ok(isUpdated ? "更新成功" : "更新失败");
    }

    // 根据ID获取公司信息
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getCompanyInfoById(@PathVariable Integer id) {
        CompanyInfo companyInfo = companyInfoService.getById(id);
        return companyInfo != null ? ResponseEntity.ok(companyInfo) : ResponseEntity.ok("未找到相应公司信息");
    }

    // 分页获取所有公司信息
    @GetMapping("/all")
    public ResponseEntity<IPage<CompanyInfo>> getAllCompanyInfos(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<CompanyInfo> pageQuery = new Page<>(page, size);
        IPage<CompanyInfo> companyInfos = companyInfoService.page(pageQuery);
        return ResponseEntity.ok(companyInfos);
    }

}
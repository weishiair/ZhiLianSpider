package com.example.webmagic.service;

import com.example.webmagic.domain.*;
import com.example.webmagic.mapper.CompanyInfoMapper;
import com.example.webmagic.mapper.JobInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class DatabaseService {

    @Autowired
    private JobInfoMapper jobInfoMapper;

    @Autowired
    private CompanyInfoMapper companyInfoMapper;

    private final Logger logger = LoggerFactory.getLogger(DatabaseService.class);

    public Map<String, Integer> getJobAndCompanyIds(String jobLocation, String companyWebsite) {
        return jobInfoMapper.getJobAndCompanyIds(jobLocation, companyWebsite);
    }

    @Transactional
    public void processEntries(List<JobCompanyInfo> jobCompanyInfos) {
        if (jobCompanyInfos != null) {
            for (JobCompanyInfo jobCompanyInfo : jobCompanyInfos) {
                CompanyInfo companyInfo = jobCompanyInfo.getCompanyInfo();
                JobInfo jobInfo = jobCompanyInfo.getJobInfo();

                // 通过公司名称查找公司信息
                CompanyInfo existingCompany = companyInfoMapper.findByName(companyInfo.getCompanyName());
                Integer companyId;
                if (existingCompany == null) {
                    // 如果公司不存在，则插入新公司记录
                    companyInfoMapper.insert(companyInfo);
                    companyId = companyInfoMapper.getLastInsertId();  // 获取刚插入的公司ID
                } else {
                    // 如果公司已存在，则使用现有公司的ID
                    companyId = existingCompany.getId();
                }

                jobInfo.setCompanyId(companyId);  // 设置JobInfo的companyId字段

                // 检查职位是否已存在
                JobInfo existingJob = jobInfoMapper.findByUniqueIdentifier(jobInfo.getJobName(), companyId);

                if (existingJob == null) {
                    // 如果职位不存在，则插入新职位记录
                    jobInfoMapper.insert(jobInfo);
                }
            }
        }

    }

    @Transactional(readOnly = true)
    public List<JobDetailInfo> getJobsForDetailScraping() {
        return jobInfoMapper.findJobsForDetailScraping();
    }

    @Transactional
    public void updateJobAndCompanyInfo(Integer jobId, String jobLocation, String jobDescription, Integer companyId, String companyWebsite) {
        System.out.println("Entering updateJobAndCompanyInfo method.");
        if (jobLocation != null || jobDescription != null) {
            jobInfoMapper.updateJobInfo(jobId, jobLocation, jobDescription);
        }
        if (companyWebsite != null) {
            companyInfoMapper.updateCompanyWebsite(companyId, companyWebsite);
        }
        System.out.println("Exiting updateJobAndCompanyInfo method.");
    }


    @Transactional
    public void processEntry(JobCompanyInfo jobCompanyInfo) {
        if (jobCompanyInfo != null) {
            CompanyInfo companyInfo = jobCompanyInfo.getCompanyInfo();
            JobInfo jobInfo = jobCompanyInfo.getJobInfo();

            // 通过公司名称查找公司信息
            CompanyInfo existingCompany = companyInfoMapper.findByName(companyInfo.getCompanyName());
            Integer companyId;
            if (existingCompany == null) {
                // 如果公司不存在，则插入新公司记录
                int companyResult = companyInfoMapper.insert(companyInfo);
                if (companyResult > 0) {
                    logger.info("Successfully inserted company info.");
                } else {
                    logger.warn("Failed to insert company info.");
                }
                companyId = companyInfoMapper.getLastInsertId();  // 获取刚插入的公司ID
            } else {
                // 如果公司已存在，则使用现有公司的ID
                companyId = existingCompany.getId();
            }


            jobInfo.setCompanyId(companyId);  // 设置JobInfo的companyId字段

            // 检查职位是否已存在
            JobInfo existingJob = jobInfoMapper.findByUniqueIdentifier(jobInfo.getJobName(), companyId);

            if (existingJob == null) {
                // 如果职位不存在，则插入新职位记录
                int jobResult = jobInfoMapper.insert(jobInfo);
                if (jobResult > 0) {
                    logger.info("Successfully inserted job info.");
                } else {
                    logger.warn("Failed to insert job info.");
                }
            }
        }

    }
    @Transactional(readOnly = true)
    public List<CompanyDetailInfo> getCompaniesForDetailScraping() {
        // 这个方法应该返回需要爬取详情的公司列表
        return companyInfoMapper.findCompaniesForDetailScraping();
    }

    @Transactional
    public void updateCompanyDetails(Integer companyId, Date establishmentDate,
                                     String registeredCapital, String legalRepresentative,
                                     String companyAddress,String companyIntroduce) {
        System.out.println("Entering updateCompanyDetails method.");

        // 创建一个新的 CompanyInfo 对象来保存更新的数据
        CompanyInfo companyInfo = new CompanyInfo();
        companyInfo.setId(companyId);  // 设置公司ID
        companyInfo.setEstablishmentDate(establishmentDate);  // 设置成立日期
        companyInfo.setRegisteredCapital(registeredCapital);  // 设置注册资本
        companyInfo.setLegalRepresentative(legalRepresentative);  // 设置法人代表
        companyInfo.setCompanyAddress(companyAddress);  // 设置公司地址
        companyInfo.setCompanyIntroduce(companyIntroduce);

        // 调用 Mapper 方法来更新数据库
        companyInfoMapper.updateCompanyDetails(companyInfo);

        System.out.println("Exiting updateCompanyDetails method.");
    }


}

package com.example.webmagic.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName job_info
 */
public class JobInfo implements Serializable {
    /**
     *
     */
    private Integer id;

    /**
     * 职位名
     */
    private String jobName;

    /**
     * 公司id
     */
    private Integer companyId;

    /**
     * 薪资
     */
    private String salary;

    /**
     * 城市
     */
    private String city;

    /**
     * 经验
     */
    private String experience;

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    /**
     * 标签
     */
    private String labels;

    /**
     * 最后更新时间
     */
    private Date lastUpdateTime;

    /**
     * 搜索关键字
     */
    private String searchKeywords;

    /**
     * 职位描述
     */
    private String jobDescription;

    /**
     * 工作地点
     */
    private String jobLocation;

    /**
     * 删除标志
     */
    private String deleteFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 详情页链接
     */
    private String jobDetails;

    /**
     * 学历
     */
    private String education;


    private String companyName; // 新增公司名称属性

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 职位名
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * 职位名
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    /**
     * 公司id
     */
    public Integer getCompanyId() {
        return companyId;
    }

    /**
     * 公司id
     */
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    /**
     * 薪资
     */
    public String getSalary() {
        return salary;
    }

    /**
     * 薪资
     */
    public void setSalary(String salary) {
        this.salary = salary;
    }

    /**
     * 城市
     */
    public String getCity() {
        return city;
    }

    /**
     * 城市
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 经验
     */
    public String getExperience() {
        return experience;
    }

    /**
     * 经验
     */
    public void setExperience(String experience) {
        this.experience = experience;
    }

    /**
     * 最后更新时间
     */
    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * 最后更新时间
     */
    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * 搜索关键字
     */
    public String getSearchKeywords() {
        return searchKeywords;
    }

    /**
     * 搜索关键字
     */
    public void setSearchKeywords(String searchKeywords) {
        this.searchKeywords = searchKeywords;
    }

    /**
     * 职位描述
     */
    public String getJobDescription() {
        return jobDescription;
    }

    /**
     * 职位描述
     */
    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    /**
     * 工作地点
     */
    public String getJobLocation() {
        return jobLocation;
    }

    /**
     * 工作地点
     */
    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    /**
     * 删除标志
     */
    public String getDeleteFlag() {
        return deleteFlag;
    }

    /**
     * 删除标志
     */
    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    /**
     * 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 更新人
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * 更新人
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 创建人
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 创建人
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * 详情页链接
     */
    public String getJobDetails() {
        return jobDetails;
    }

    /**
     * 详情页链接
     */
    public void setJobDetails(String jobDetails) {
        this.jobDetails = jobDetails;
    }

    /**
     * 学历
     */
    public String getEducation() {
        return education;
    }

    /**
     * 学历
     */
    public void setEducation(String education) {
        this.education = education;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}
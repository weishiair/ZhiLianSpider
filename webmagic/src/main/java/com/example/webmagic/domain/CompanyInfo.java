package com.example.webmagic.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName company_info
 */
public class CompanyInfo implements Serializable {
    /**
     * 公司id
     */
    private Integer id;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 公司规模
     */
    private String companySize;

    /**
     * 公司性质
     */
    private String companyNature;

    /**
     * 最后更新时间
     */
    private Date lastUpdateTime;

    /**
     * 删除标记（Y:已删除N:未删除）
     */
    private String deleteFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 成立时间
     */
    private Date establishmentDate;

    /**
     * 注册资本
     */
    private String registeredCapital;

    /**
     * 公司地址
     */
    private String companyAddress;

    /**
     * 公司主页
     */
    private String companyWebsite;

    /**
     * 法人代表
     */
    private String legalRepresentative;

    /**
     * 公司信息
     */
    private String companyIntroduce;

    private static final long serialVersionUID = 1L;

    /**
     * 公司id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 公司id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 公司名称
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * 公司名称
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * 公司规模
     */
    public String getCompanySize() {
        return companySize;
    }

    /**
     * 公司规模
     */
    public void setCompanySize(String companySize) {
        this.companySize = companySize;
    }

    /**
     * 公司性质
     */
    public String getCompanyNature() {
        return companyNature;
    }

    /**
     * 公司性质
     */
    public void setCompanyNature(String companyNature) {
        this.companyNature = companyNature;
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
     * 删除标记（Y:已删除N:未删除）
     */
    public String getDeleteFlag() {
        return deleteFlag;
    }

    /**
     * 删除标记（Y:已删除N:未删除）
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
     * 成立时间
     */
    public Date getEstablishmentDate() {
        return establishmentDate;
    }

    /**
     * 成立时间
     */
    public void setEstablishmentDate(Date establishmentDate) {
        this.establishmentDate = establishmentDate;
    }

    /**
     * 注册资本
     */
    public String getRegisteredCapital() {
        return registeredCapital;
    }

    /**
     * 注册资本
     */
    public void setRegisteredCapital(String registeredCapital) {
        this.registeredCapital = registeredCapital;
    }

    /**
     * 公司地址
     */
    public String getCompanyAddress() {
        return companyAddress;
    }

    /**
     * 公司地址
     */
    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    /**
     * 公司主页
     */
    public String getCompanyWebsite() {
        return companyWebsite;
    }

    /**
     * 公司主页
     */
    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    /**
     * 法人代表
     */
    public String getLegalRepresentative() {
        return legalRepresentative;
    }

    /**
     * 法人代表
     */
    public void setLegalRepresentative(String legalRepresentative) {
        this.legalRepresentative = legalRepresentative;
    }

    /**
     * 公司信息
     */
    public String getCompanyIntroduce() {
        return companyIntroduce;
    }

    /**
     * 公司信息
     */
    public void setCompanyIntroduce(String companyIntroduce) {
        this.companyIntroduce = companyIntroduce;
    }


}
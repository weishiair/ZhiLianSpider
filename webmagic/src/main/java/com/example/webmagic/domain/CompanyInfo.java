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

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        CompanyInfo other = (CompanyInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCompanyName() == null ? other.getCompanyName() == null : this.getCompanyName().equals(other.getCompanyName()))
            && (this.getCompanySize() == null ? other.getCompanySize() == null : this.getCompanySize().equals(other.getCompanySize()))
            && (this.getCompanyNature() == null ? other.getCompanyNature() == null : this.getCompanyNature().equals(other.getCompanyNature()))
            && (this.getLastUpdateTime() == null ? other.getLastUpdateTime() == null : this.getLastUpdateTime().equals(other.getLastUpdateTime()))
            && (this.getDeleteFlag() == null ? other.getDeleteFlag() == null : this.getDeleteFlag().equals(other.getDeleteFlag()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
            && (this.getEstablishmentDate() == null ? other.getEstablishmentDate() == null : this.getEstablishmentDate().equals(other.getEstablishmentDate()))
            && (this.getRegisteredCapital() == null ? other.getRegisteredCapital() == null : this.getRegisteredCapital().equals(other.getRegisteredCapital()))
            && (this.getCompanyAddress() == null ? other.getCompanyAddress() == null : this.getCompanyAddress().equals(other.getCompanyAddress()))
            && (this.getCompanyWebsite() == null ? other.getCompanyWebsite() == null : this.getCompanyWebsite().equals(other.getCompanyWebsite()))
            && (this.getLegalRepresentative() == null ? other.getLegalRepresentative() == null : this.getLegalRepresentative().equals(other.getLegalRepresentative()))
            && (this.getCompanyIntroduce() == null ? other.getCompanyIntroduce() == null : this.getCompanyIntroduce().equals(other.getCompanyIntroduce()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCompanyName() == null) ? 0 : getCompanyName().hashCode());
        result = prime * result + ((getCompanySize() == null) ? 0 : getCompanySize().hashCode());
        result = prime * result + ((getCompanyNature() == null) ? 0 : getCompanyNature().hashCode());
        result = prime * result + ((getLastUpdateTime() == null) ? 0 : getLastUpdateTime().hashCode());
        result = prime * result + ((getDeleteFlag() == null) ? 0 : getDeleteFlag().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getEstablishmentDate() == null) ? 0 : getEstablishmentDate().hashCode());
        result = prime * result + ((getRegisteredCapital() == null) ? 0 : getRegisteredCapital().hashCode());
        result = prime * result + ((getCompanyAddress() == null) ? 0 : getCompanyAddress().hashCode());
        result = prime * result + ((getCompanyWebsite() == null) ? 0 : getCompanyWebsite().hashCode());
        result = prime * result + ((getLegalRepresentative() == null) ? 0 : getLegalRepresentative().hashCode());
        result = prime * result + ((getCompanyIntroduce() == null) ? 0 : getCompanyIntroduce().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", companyName=").append(companyName);
        sb.append(", companySize=").append(companySize);
        sb.append(", companyNature=").append(companyNature);
        sb.append(", lastUpdateTime=").append(lastUpdateTime);
        sb.append(", deleteFlag=").append(deleteFlag);
        sb.append(", createTime=").append(createTime);
        sb.append(", createBy=").append(createBy);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", establishmentDate=").append(establishmentDate);
        sb.append(", registeredCapital=").append(registeredCapital);
        sb.append(", companyAddress=").append(companyAddress);
        sb.append(", companyWebsite=").append(companyWebsite);
        sb.append(", legalRepresentative=").append(legalRepresentative);
        sb.append(", companyIntroduce=").append(companyIntroduce);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
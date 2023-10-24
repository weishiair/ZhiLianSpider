package com.example.webmagic.domain;

public class CompanyDetailInfo {

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyWebsite() {
        return CompanyWebsite;
    }

    public void setCompanyWebsite(String companyWebsite) {
        CompanyWebsite = companyWebsite;
    }

    @Override
    public String toString() {
        return "CompanyDetailInfo{" +
                "companyId=" + companyId +
                ", CompanyWebsite='" + CompanyWebsite + '\'' +
                '}';
    }

    private Integer companyId;  // 公司ID
    private String CompanyWebsite;//公司详情页url


}
package com.example.webmagic.domain;

public class JobCompanyInfo {
    public JobInfo getJobInfo() {
        return jobInfo;
    }

    public void setJobInfo(JobInfo jobInfo) {
        this.jobInfo = jobInfo;
    }

    public CompanyInfo getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyInfo(CompanyInfo companyInfo) {
        this.companyInfo = companyInfo;
    }

    @Override
    public String toString() {
        return "JobCompanyInfo{" +
                "jobInfo=" + jobInfo +
                ", companyInfo=" + companyInfo +
                '}';
    }

    private JobInfo jobInfo;
    private CompanyInfo companyInfo;
}
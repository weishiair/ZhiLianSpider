package com.example.webmagic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.webmagic.domain.JobDetailInfo;
import com.example.webmagic.domain.JobInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author 10244
* @description 针对表【job_info】的数据库操作Mapper
* @createDate 2023-10-16 11:12:47
* @Entity com.example.webmagic.domain.JobInfo
*/
@Mapper
public interface JobInfoMapper extends BaseMapper<JobInfo> {
    @Select("SELECT * FROM job_info WHERE job_name = #{jobName} AND company_id = #{companyId}")
    JobInfo findByUniqueIdentifier(@Param("jobName") String jobName, @Param("companyId") Integer companyId);


    @Select("SELECT id AS jobId, company_id AS companyId, job_details AS jobDetails FROM job_info WHERE job_description IS NULL OR job_location IS NULL")
    List<JobDetailInfo> findJobsForDetailScraping();
    @Update("UPDATE job_info SET job_location = #{jobLocation}, job_description = #{jobDescription},last_update_time = #{lastUpdateTime}, labels = #{labels} WHERE id = #{jobId}")
    void updateJobInfo(@Param("jobId") Integer jobId, @Param("jobLocation") String jobLocation, @Param("jobDescription") String jobDescription, @Param("labels") String labels,@Param("lastUpdateTime") Date lastUpdateTime);
    @Select("SELECT job_info.id, job_info.company_id FROM job_info " +
            "JOIN company_info ON job_info.company_id = company_info.id " +
            "WHERE job_info.job_location = #{jobLocation} AND company_info.company_website = #{companyWebsite}")
    Map<String, Integer> getJobAndCompanyIds(String jobLocation, String companyWebsite);

    @Select("SELECT job_info.*, company_info.company_name as company_name FROM job_info " +
            "JOIN company_info ON job_info.company_id = company_info.id " +
            "WHERE job_info.id = #{id}")
    JobInfo findJobInfoWithCompanyNameById(@Param("id") Integer id);

    // 修改分页查询方法
    @Select("SELECT job_info.*, company_info.company_name as company_name FROM job_info " +
            "JOIN company_info ON job_info.company_id = company_info.id")
    IPage<JobInfo> getAllJobInfos(IPage<?> page);
}

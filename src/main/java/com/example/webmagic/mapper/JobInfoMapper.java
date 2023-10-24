package com.example.webmagic.mapper;

import com.example.webmagic.domain.JobInfo;

/**
* @author 10244
* @description 针对表【job_info】的数据库操作Mapper
* @createDate 2023-10-23 09:55:33
* @Entity com.example.webmagic.domain.JobInfo
*/
public interface JobInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(JobInfo record);

    int insertSelective(JobInfo record);

    JobInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(JobInfo record);

    int updateByPrimaryKey(JobInfo record);

}

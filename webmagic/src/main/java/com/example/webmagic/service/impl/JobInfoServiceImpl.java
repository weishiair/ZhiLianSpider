package com.example.webmagic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.webmagic.domain.JobInfo;
import com.example.webmagic.service.JobInfoService;
import com.example.webmagic.mapper.JobInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author 10244
* @description 针对表【job_info】的数据库操作Service实现
* @createDate 2023-11-23 13:49:23
*/
@Service
public class JobInfoServiceImpl extends ServiceImpl<JobInfoMapper, JobInfo>
    implements JobInfoService{

}





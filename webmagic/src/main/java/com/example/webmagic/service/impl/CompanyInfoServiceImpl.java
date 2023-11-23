package com.example.webmagic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.webmagic.domain.CompanyInfo;
import com.example.webmagic.service.CompanyInfoService;
import com.example.webmagic.mapper.CompanyInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author 10244
* @description 针对表【company_info】的数据库操作Service实现
* @createDate 2023-11-23 13:53:39
*/
@Service
public class CompanyInfoServiceImpl extends ServiceImpl<CompanyInfoMapper, CompanyInfo>
    implements CompanyInfoService{

}





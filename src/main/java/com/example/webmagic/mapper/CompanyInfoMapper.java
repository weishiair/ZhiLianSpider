package com.example.webmagic.mapper;

import com.example.webmagic.domain.CompanyInfo;

/**
* @author 10244
* @description 针对表【company_info】的数据库操作Mapper
* @createDate 2023-10-24 14:23:25
* @Entity com.example.webmagic.domain.CompanyInfo
*/
public interface CompanyInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(CompanyInfo record);

    int insertSelective(CompanyInfo record);

    CompanyInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CompanyInfo record);

    int updateByPrimaryKey(CompanyInfo record);

}

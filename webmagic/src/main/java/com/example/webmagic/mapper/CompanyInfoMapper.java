package com.example.webmagic.mapper;

import com.example.webmagic.domain.CompanyDetailInfo;
import com.example.webmagic.domain.CompanyInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


/**
* @author 10244
* @description 针对表【company_info】的数据库操作Mapper
* @createDate 2023-10-16 11:15:13
* @Entity com.example.webmagic.domain.CompanyInfo
*/
@Mapper
public interface CompanyInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(CompanyInfo record);

    int insertSelective(CompanyInfo record);

    CompanyInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CompanyInfo record);

    int updateByPrimaryKey(CompanyInfo record);

    @Select("SELECT LAST_INSERT_ID()")
    Integer getLastInsertId();

    @Select("SELECT * FROM company_info WHERE company_name = #{companyName}")
    CompanyInfo findByName(String companyName);

    @Update("UPDATE company_info SET company_website = #{companyWebsite} WHERE id = #{companyId}")
    void updateCompanyWebsite(@Param("companyId") Integer companyId, @Param("companyWebsite") String companyWebsite);

    @Select("SELECT * FROM company_info WHERE company_website IS NOT NULL AND company_website != ''")
    List<CompanyInfo> findCompaniesWithWebsite();

    @Update("UPDATE company_info SET establishment_date = #{establishmentDate}, registered_capital = #{registeredCapital}, " +
            "legal_representative = #{legalRepresentative}, company_address = #{companyAddress}, company_introduce = #{companyIntroduce},last_update_time = #{lastUpdateTime} WHERE id = #{id}")
    int updateCompanyDetails(CompanyInfo companyInfo);


    @Select("SELECT id AS companyId, company_website AS CompanyWebsite FROM company_info WHERE company_introduce IS NULL OR company_introduce = ''")
    List<CompanyDetailInfo> findCompaniesForDetailScraping();


}

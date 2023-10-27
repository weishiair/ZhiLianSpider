package com.example.webmagic.mapper;

import com.example.webmagic.domain.Cookie;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CookieMapper {

    @Insert("INSERT INTO cookies (domain, path, name, value, expiry, secure, http_only, created_at, updated_at) VALUES (#{domain}, #{path}, #{name}, #{value}, #{expiry}, #{secure}, #{httpOnly}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertCookie(Cookie cookie);

    @Select("SELECT * FROM cookies WHERE domain = #{domain} AND path = #{path}")
    List<Cookie> findByDomainAndPath(@Param("domain") String domain, @Param("path") String path);

    @Delete("DELETE FROM cookies WHERE id = #{id}")
    void deleteCookie(@Param("id") Integer id);

    @Delete("DELETE FROM cookies WHERE expiry < NOW()")
    void deleteExpiredCookies();

    @Select("SELECT * FROM cookies")
    List<Cookie> findAllCookies();

    @Delete("DELETE FROM cookies")
    void deleteAllCookies();
}
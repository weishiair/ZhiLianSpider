package com.example.webmagic.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户配置表
 * @TableName user_config
 */
@TableName(value ="user_config")
public class UserConfig implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String configName;

    /**
     * 
     */
    private Integer userId;

    /**
     * 
     */
    private String city;

    /**
     * 
     */
    private String keyword;

    /**
     * Y表示配置已被删除, N表示配置有效
     */
    private String deleteFlag;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     */
    public String getConfigName() {
        return configName;
    }

    /**
     * 
     */
    public void setConfigName(String configName) {
        this.configName = configName;
    }

    /**
     * 
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 
     */
    public String getCity() {
        return city;
    }

    /**
     * 
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * 
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Y表示配置已被删除, N表示配置有效
     */
    public String getDeleteFlag() {
        return deleteFlag;
    }

    /**
     * Y表示配置已被删除, N表示配置有效
     */
    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    /**
     * 
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


}
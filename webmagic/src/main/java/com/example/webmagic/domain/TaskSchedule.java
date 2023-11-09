package com.example.webmagic.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName task_schedule
 */
@TableName(value ="task_schedule")
public class TaskSchedule implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String scheduleName;

    /**
     * 
     */
    private String cronExpression;

    /**
     * 
     */
    private String status;

    /**
     * 
     */
    private Date lastRunTime;

    /**
     * 
     */
    private Date nextRunTime;

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
    public String getScheduleName() {
        return scheduleName;
    }

    /**
     * 
     */
    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    /**
     * 
     */
    public String getCronExpression() {
        return cronExpression;
    }

    /**
     * 
     */
    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    /**
     * 
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 
     */
    public Date getLastRunTime() {
        return lastRunTime;
    }

    /**
     * 
     */
    public void setLastRunTime(Date lastRunTime) {
        this.lastRunTime = lastRunTime;
    }

    /**
     * 
     */
    public Date getNextRunTime() {
        return nextRunTime;
    }

    /**
     * 
     */
    public void setNextRunTime(Date nextRunTime) {
        this.nextRunTime = nextRunTime;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", scheduleName=").append(scheduleName);
        sb.append(", cronExpression=").append(cronExpression);
        sb.append(", status=").append(status);
        sb.append(", lastRunTime=").append(lastRunTime);
        sb.append(", nextRunTime=").append(nextRunTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
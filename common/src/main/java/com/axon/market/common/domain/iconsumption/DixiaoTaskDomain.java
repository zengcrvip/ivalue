package com.axon.market.common.domain.iconsumption;

import java.util.Date;

/**
 * Created by zhuwen on 2017/7/24.
 */
public class DixiaoTaskDomain {
    //ID
    private long taskid;
    //月份编码
    private String monthcode;
    //活动编码
    private String saleid;
    //活动名称
    private String salename;
    //活动描述
    private String saledesc;
    //活动地市
    private String sale_eparchy_code;
    //波次编码
    private String boid;
    //目标客户群编码
    private String aim_sub_id;
    //目标客户群名称
    private String aim_sub_name;
    //开始时间
    private String start_date;
    //结束时间
    private String end_date;
    //创建时间
    private Date createtime;
    //更新时间
    private Date updatetime;
    //活动文件名
    private String config_file_name;
    //用户文件名
    private String user_file_name;
    //ftp通知
    private Integer notify_ftp;
    //档位类型
    private String ranktype;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    private Integer status;

    public long getTaskid() {
        return taskid;
    }

    public void setTaskid(long taskid) {
        this.taskid = taskid;
    }

    public String getMonthcode() {
        return monthcode;
    }

    public void setMonthcode(String monthcode) {
        this.monthcode = monthcode;
    }

    public String getSaleid() {
        return saleid;
    }

    public void setSaleid(String saleid) {
        this.saleid = saleid;
    }

    public String getSalename() {
        return salename;
    }

    public void setSalename(String salename) {
        this.salename = salename;
    }

    public String getSaledesc() {
        return saledesc;
    }

    public void setSaledesc(String saledesc) {
        this.saledesc = saledesc;
    }

    public String getSale_eparchy_code() {
        return sale_eparchy_code;
    }

    public void setSale_eparchy_code(String sale_eparchy_code) {
        this.sale_eparchy_code = sale_eparchy_code;
    }

    public String getBoid() {
        return boid;
    }

    public void setBoid(String boid) {
        this.boid = boid;
    }

    public String getAim_sub_id() {
        return aim_sub_id;
    }

    public void setAim_sub_id(String aim_sub_id) {
        this.aim_sub_id = aim_sub_id;
    }

    public String getAim_sub_name() {
        return aim_sub_name;
    }

    public void setAim_sub_name(String aim_sub_name) {
        this.aim_sub_name = aim_sub_name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getConfig_file_name() {
        return config_file_name;
    }

    public void setConfig_file_name(String config_file_name) {
        this.config_file_name = config_file_name;
    }

    public String getUser_file_name() {
        return user_file_name;
    }

    public void setUser_file_name(String user_file_name) {
        this.user_file_name = user_file_name;
    }

    public Integer getNotify_ftp() {
        return notify_ftp;
    }

    public void setNotify_ftp(Integer notify_ftp) {
        this.notify_ftp = notify_ftp;
    }

    public String getRanktype() {
        return ranktype;
    }

    public void setRanktype(String ranktype) {
        this.ranktype = ranktype;
    }
}

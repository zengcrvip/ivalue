package com.axon.market.common.domain.iconsumption;

import java.util.Date;

/**
 * Created by zhuwen on 2017/7/21.
 */
public class DixiaoResultDomain {
    //id
    private long id;
    //taskid
    private long taskid;
    //月份编码
    private String monthcode;
    //档位id
    private String rankid;
    //档位类型
    private String ranktype;
    //地区编码
    private String area;
    //匹配人数
    private long matchno;
    //是否线上
    private int isonline;
    //合作团队编码
    private String partnercode;
    //推送方式
    private int method;
    //状态
    private int status;
    //是否最新
    private int isnewest;
    //创建时间
    private Date createtime;
    //更新时间
    private Date updatetime;
    //是否ftp到话+
    private int ftpflag;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getRankid() {
        return rankid;
    }

    public void setRankid(String rankid) {
        this.rankid = rankid;
    }

    public String getRanktype() {
        return ranktype;
    }

    public void setRanktype(String ranktype) {
        this.ranktype = ranktype;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public long getMatchno() {
        return matchno;
    }

    public void setMatchno(long matchno) {
        this.matchno = matchno;
    }

    public int getIsonline() {
        return isonline;
    }

    public void setIsonline(int isonline) {
        this.isonline = isonline;
    }

    public String getPartnercode() {
        return partnercode;
    }

    public void setPartnercode(String partnercode) {
        this.partnercode = partnercode;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsnewest() {
        return isnewest;
    }

    public void setIsnewest(int isnewest) {
        this.isnewest = isnewest;
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

    public int getFtpflag() {
        return ftpflag;
    }

    public void setFtpflag(int ftpflag) {
        this.ftpflag = ftpflag;
    }
}

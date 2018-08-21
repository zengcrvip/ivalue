package com.axon.market.common.domain.iconsumption;


import java.util.Date;
/**低销波次统计结果实体类
 * Created by zhuwen on 2017/6/28.
 */
public class PINResultDomain {
    //id
    private long id;
    //月份编码
    private String monthcode;
    //波次编码
    private String batchno;
    //产品id
    private String rankid;
    //产品名称
    private String rankname;
    //产品类型
    private String ranktype;
    //地区编码
    private String area;
    //匹配人数
    private long matchno;
    //状态
    private int status;
    //更新时间
    private Date updatetime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMonthcode() {
        return monthcode;
    }

    public void setMonthcode(String monthcode) {
        this.monthcode = monthcode;
    }

    public String getBatchno() {
        return batchno;
    }

    public void setBatchno(String batchno) {
        this.batchno = batchno;
    }

    public String getRankid() {
        return rankid;
    }

    public void setRankid(String rankid) {
        this.rankid = rankid;
    }

    public String getRankname() {
        return rankname;
    }

    public void setRankname(String rankname) {
        this.rankname = rankname;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}

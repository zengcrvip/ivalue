package com.axon.market.common.domain.iconsumption;

import java.util.Date;

/**低销产品分配的地市实体类
 * Created by zhuwen on 2017/6/28.
 */
public class PINRankAreaDomain {
    //id
    private long id;
    //月份编码
    private String monthcode;
    //波次编码
    private String batchno;
    //产品id
    private String rankId;
    //生效时间
    private Date applytime;
    //产品名称
    private String rankname;
    //产品类型
    private String rankType;
    //分配地市
    private String area;
    //匹配人数
    private long matchno;;
    //分配时间
    private Date allocatetime;
    //状态
    private int status;

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

    public String getRankId() {
        return rankId;
    }

    public void setRankId(String rankId) {
        this.rankId = rankId;
    }

    public Date getApplytime() {
        return applytime;
    }

    public void setApplytime(Date applytime) {
        this.applytime = applytime;
    }

    public String getRankname() {
        return rankname;
    }

    public void setRankname(String rankname) {
        this.rankname = rankname;
    }

    public String getRankType() {
        return rankType;
    }

    public void setRankType(String rankType) {
        this.rankType = rankType;
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

    public Date getAllocatetime() {
        return allocatetime;
    }

    public void setAllocatetime(Date allocatetime) {
        this.allocatetime = allocatetime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

package com.axon.market.common.domain.iconsumption;

import java.util.Date;
/**低消产品实体类
 * Created by zhuwen on 2017/6/28.
 */
public class PINRankInfoDomain {

    //产品ID
    private String rankId;
    //产品名称
    private String rankName;
    //产品类型
    private String rankType;
    //状态
    private int status;
    //生效时间
    private Date applytime;
    //失效时间
    private Date expiretime;

    public String getRankId() {
        return rankId;
    }

    public void setRankId(String rankId) {
        this.rankId = rankId;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public String getRankType() {
        return rankType;
    }

    public void setRankType(String rankType) {
        this.rankType = rankType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getApplytime() {
        return applytime;
    }

    public void setApplytime(Date applytime) {
        this.applytime = applytime;
    }

    public Date getExpiretime() {
        return expiretime;
    }

    public void setExpiretime(Date expiretime) {
        this.expiretime = expiretime;
    }
}

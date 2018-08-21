package com.axon.market.common.domain.ishopKeeper;

/**
 * Created by Zhuwen on 2017/8/17.
 */
public class KeeperCashflowDomain {
    //数据日期
    private String setdate;
    //统计日期
    private String sumdate;
    //员工ID
    private Integer userid;
    //当日现金流
    private long cashflow;
    //出账现金流
    private long czcashflow;
    //当日现金流变化趋势  1:上升  0:持平  -1:下降
    private Integer trend;
    //本月上升天数
    private Integer risedays;
    //本月下降天数
    private Integer dropdays;

    public String getSetdate() {
        return setdate;
    }

    public void setSetdate(String setdate) {
        this.setdate = setdate;
    }

    public String getSumdate() {
        return sumdate;
    }

    public void setSumdate(String sumdate) {
        this.sumdate = sumdate;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public long getCashflow() {
        return cashflow;
    }

    public void setCashflow(long cashflow) {
        this.cashflow = cashflow;
    }

    public long getCzcashflow() {
        return czcashflow;
    }

    public void setCzcashflow(long czcashflow) {
        this.czcashflow = czcashflow;
    }

    public Integer getTrend() {
        return trend;
    }

    public void setTrend(Integer trend) {
        this.trend = trend;
    }

    public Integer getRisedays() {
        return risedays;
    }

    public void setRisedays(Integer risedays) {
        this.risedays = risedays;
    }

    public Integer getDropdays() {
        return dropdays;
    }

    public void setDropdays(Integer dropdays) {
        this.dropdays = dropdays;
    }
}

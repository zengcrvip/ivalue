package com.axon.market.common.domain.ishop;

/**
 * Created by dtt on 2017/7/11.
 * 炒店2.0省份日报模型类
 */
public class ShopProvinceDailyDomain {
    private String timest;
    private Integer cityCode;
    private String cityName;
    private Integer totalBaseNum;
    private Integer effectBaseNum;
    private Integer provinceTaskNum;
    private Integer cityTaskNum ;
    private Integer baseTaskNum;
    private Integer executeTaskNum ;
    private Integer businessTypeNum;
    private Integer cityTargetUser ;
    private Integer appointUser ;
    private Integer cityResidentUser;
    private Integer baseCoverUser ;
    private Integer sendUser;
    private Integer recvSuccUser ;

    public String getTimest() {
        return timest;
    }
    public void setTimest(String timest) {
        this.timest = timest;
    }

    public Integer getCityCode() {
        return cityCode;
    }
    public void setCityCode(Integer cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getTotalBaseNum() {
        return totalBaseNum;
    }
    public void setTotalBaseNum(Integer totalBaseNum) {
        this.totalBaseNum = totalBaseNum;
    }

    public Integer getEffectBaseNum() {
        return effectBaseNum;
    }
    public void setEffectBaseNum(Integer effectBaseNum) {
        this.effectBaseNum = effectBaseNum;
    }

    public Integer getProvinceTaskNum() {
        return provinceTaskNum;
    }
    public void setProvinceTaskNum(Integer provinceTaskNum) {
        this.provinceTaskNum = provinceTaskNum;
    }

    public Integer getCityTaskNum() {
        return cityTaskNum;
    }
    public void setCityTaskNum(Integer cityTaskNum) {
        this.cityTaskNum = cityTaskNum;
    }

    public Integer getBaseTaskNum() {
        return baseTaskNum;
    }
    public void setBaseTaskNum(Integer baseTaskNum) {
        this.baseTaskNum = baseTaskNum;
    }

    public Integer getExecuteTaskNum() {
        return executeTaskNum;
    }
    public void setExecuteTaskNum(Integer executeTaskNum) {
        this.executeTaskNum = executeTaskNum;
    }

    public Integer getBusinessTypeNum() {
        return businessTypeNum;
    }
    public void setBusinessTypeNum(Integer businessTypeNum) {
        this.businessTypeNum = businessTypeNum;
    }

    public Integer getCityTargetUser() {
        return cityTargetUser;
    }
    public void setCityTargetUser(Integer cityTargetUser) {
        this.cityTargetUser = cityTargetUser;
    }

    public Integer getAppointUser() {
        return appointUser;
    }
    public void setAppointUser(Integer appointUser) {
        this.appointUser = appointUser;
    }

    public Integer getCityResidentUser() {
        return cityResidentUser;
    }
    public void setCityResidentUser(Integer cityResidentUser) {
        this.cityResidentUser = cityResidentUser;
    }

    public Integer getBaseCoverUser() {
        return baseCoverUser;
    }
    public void setBaseCoverUser(Integer baseCoverUser) {
        this.baseCoverUser = baseCoverUser;
    }

    public Integer getSendUser() {
        return sendUser;
    }
    public void setSendUser(Integer sendUser) {
        this.sendUser = sendUser;
    }

    public Integer getRecvSuccUser() {
        return recvSuccUser;
    }
    public void setRecvSuccUser(Integer recvSuccUser) {
        this.recvSuccUser = recvSuccUser;
    }


}

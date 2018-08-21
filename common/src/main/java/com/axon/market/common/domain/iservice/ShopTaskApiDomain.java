package com.axon.market.common.domain.iservice;

/**
 * 大屏展示炒店任务API返回的任务实体
 * Created by zengcr on 2017/7/29.
 */
public class ShopTaskApiDomain {
    //任务ID
    private String taskId;
    //城市编码
    private String cityCode;
    //城市名称
    private String cityName;
    //地区编码
    private String districtCode;
    //地区名称
    private String districtName;
    //炒店类型ID
    private String locationTypeId;
    //炒店类型名称
    private String locationTypeName;
    //营业厅名称
    private String storeName;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getLocationTypeId() {
        return locationTypeId;
    }

    public void setLocationTypeId(String locationTypeId) {
        this.locationTypeId = locationTypeId;
    }

    public String getLocationTypeName() {
        return locationTypeName;
    }

    public void setLocationTypeName(String locationTypeName) {
        this.locationTypeName = locationTypeName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}

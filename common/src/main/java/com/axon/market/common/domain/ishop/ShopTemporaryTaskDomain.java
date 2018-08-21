package com.axon.market.common.domain.ishop;

import java.util.Date;

/**
 * 炒店任务实体类
 * Created by zengcr on 2017/2/14.
 */
public class ShopTemporaryTaskDomain extends ShopTaskDomain{
    // 场馆地址
    private String addressDetail;
    // 经度
    private String longitude;
    // 纬度
    private String latitude;
    // 半径
    private Integer radius;
    // 城市ID
    private Integer cityId;
    private  Integer cityCode;
    // 地区ID
    private Integer areaId;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public Integer getCityCode() {
        return cityCode;
    }

    public void setCityCode(Integer cityCode) {
        this.cityCode = cityCode;
    }

}

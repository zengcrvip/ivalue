package com.axon.market.common.domain.iscene;

/**
 * 位置场景基站站点对象实体
 * Created by zengcr on 2016/12/2.
 */
public class PositionBaseDomain
{
    //基站ID
    private Integer baseId;
    //基站名称
    private String baseName;
    //基站位置点类型
    private String locationType;
    //所属城市
    private String cityName;
    //经度
    private String lng;
    //纬度
    private String lat;
    //半径
    private String radius;
    //详细地址
    private String address;
    //城市编码
    private Integer cityCode;
    //位置点类型ID
    private Integer locationTypeId;
    //状态
    private Integer status;
    //营业厅编码
    private String businessHallCode;
    //操作类型
    private Integer operate;
    //所属公司编码
    private Integer companyCode;

    private String addDate;

    public Integer getBaseId()
    {
        return baseId;
    }

    public void setBaseId(Integer baseId)
    {
        this.baseId = baseId;
    }

    public String getBaseName()
    {
        return baseName;
    }

    public void setBaseName(String baseName)
    {
        this.baseName = baseName;
    }

    public String getLocationType()
    {
        return locationType;
    }

    public void setLocationType(String locationType)
    {
        this.locationType = locationType;
    }

    public String getCityName()
    {
        return cityName;
    }

    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }

    public String getLng()
    {
        return lng;
    }

    public void setLng(String lng)
    {
        this.lng = lng;
    }

    public String getLat()
    {
        return lat;
    }

    public void setLat(String lat)
    {
        this.lat = lat;
    }

    public String getRadius()
    {
        return radius;
    }

    public void setRadius(String radius)
    {
        this.radius = radius;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public Integer getCityCode()
    {
        return cityCode;
    }

    public void setCityCode(Integer cityCode)
    {
        this.cityCode = cityCode;
    }

    public Integer getLocationTypeId()
    {
        return locationTypeId;
    }

    public void setLocationTypeId(Integer locationTypeId)
    {
        this.locationTypeId = locationTypeId;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getBusinessHallCode()
    {
        return businessHallCode;
    }

    public void setBusinessHallCode(String businessHallCode)
    {
        this.businessHallCode = businessHallCode;
    }

    public String getAddDate()
    {
        return addDate;
    }

    public void setAddDate(String addDate)
    {
        this.addDate = addDate;
    }

    public Integer getOperate()
    {
        return operate;
    }

    public void setOperate(Integer operate)
    {
        this.operate = operate;
    }

    public Integer getCompanyCode()
    {
        return companyCode;
    }

    public void setCompanyCode(Integer companyCode)
    {
        this.companyCode = companyCode;
    }
}

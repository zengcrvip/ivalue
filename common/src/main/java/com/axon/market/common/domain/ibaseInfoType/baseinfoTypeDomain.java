package com.axon.market.common.domain.ibaseInfoType;

/**
 * 我的炒店类型实体
 * 创建人:邵炜
 * 创建时间:2017年2月20日17:13:17
 * Created by gloomysw on 2017/02/20.
 */
public class baseinfoTypeDomain
{
    // 主键
    private int locationTypeId;

    // 类型名称
    private String locationType;

    public int getLocationTypeId()
    {
        return locationTypeId;
    }

    public void setLocationTypeId(int locationTypeId)
    {
        this.locationTypeId = locationTypeId;
    }

    public String getLocationType()
    {
        return locationType;
    }

    public void setLocationType(String locationType)
    {
        this.locationType = locationType;
    }
}

package com.axon.market.common.domain.ishop;


/**
 * Created by gloomysw on 2017/02/17.
 */
public class ShopListDomain
{
    public ShopListDomain(){
        setCityName("");
    }

    // 主键
    private long baseId;

    // 营业厅名称
    private String baseName;

    // 营业厅地址
    private String address;

    // 经度
    private String lng;

    // 纬度
    private String lat;

    // 营业厅电话
    private String fixedTelePhone;

    // 营业厅编码
    private String businessHallCode;

    // 城市编码
    private int cityCode;

    // 地区外键
    private int districtCode;

    // 类型外键
    private int locationTypeId;

    // 半径
    private int radius;

    // 任务发送配额
    private int quotaNumber;

    // 炒店状态 -1删除 0:未注册 1:在线 2:待审核 3:未通过
  private String status;

  // 城市外键
  private int cityAreaCode;

  // 修改时间
  private String updateDate;

  // 城市名称
  private String cityName;

  // 类型名称
  private String locationTypeNames;

  // 审核人id
  private Integer reviewUserId;

  // 审核人来自用户id
  private Integer reviewUserIdFrom;

  // 是否可以编辑
  private int isUpdate;

  // 短信签名
  private String messageAutograph;

    // 创建用户id
    private Integer createUserid;

    public String getMessageAutograph()
    {
        return messageAutograph;
    }

    public void setMessageAutograph(String messageAutograph)
    {
        this.messageAutograph = messageAutograph;
    }

    public int getIsUpdate()
    {
        return isUpdate;
    }

    public void setIsUpdate(int isUpdate)
    {
        this.isUpdate = isUpdate;
    }

    public Integer getReviewUserId() {
        return reviewUserId;
    }

    public void setReviewUserId(Integer reviewUserId) {
        this.reviewUserId = reviewUserId;
    }

    public Integer getReviewUserIdFrom() {
        return reviewUserIdFrom;
    }

    public void setReviewUserIdFrom(Integer reviewUserIdFrom) {
        this.reviewUserIdFrom = reviewUserIdFrom;
    }

    public String getCityName()
    {
        return cityName;
    }

    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }

    public String getLocationTypeNames()
    {
        return locationTypeNames;
    }

    public void setLocationTypeNames(String locationTypeNames)
    {
        this.locationTypeNames = locationTypeNames;
    }

    public int getCityAreaCode()
    {
        return cityAreaCode;
    }

    public void setCityAreaCode(int cityAreaCode)
    {
        this.cityAreaCode = cityAreaCode;
    }

    public long getBaseId()
    {
        return baseId;
    }

    public void setBaseId(long baseId)
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

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
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

    public String getFixedTelePhone()
    {
        return fixedTelePhone;
    }

    public void setFixedTelePhone(String fixedTelePhone)
    {
        this.fixedTelePhone = fixedTelePhone;
    }

    public String getBusinessHallCode()
    {
        return businessHallCode;
    }

    public void setBusinessHallCode(String businessHallCode)
    {
        this.businessHallCode = businessHallCode;
    }

    public int getCityCode()
    {
        return cityCode;
    }

    public void setCityCode(int cityCode)
    {
        this.cityCode = cityCode;
    }

    public int getDistrictCode()
    {
        return districtCode;
    }

    public void setDistrictCode(int districtCode)
    {
        this.districtCode = districtCode;
    }

    public int getLocationTypeId()
    {
        return locationTypeId;
    }

    public void setLocationTypeId(int locationTypeId)
    {
        this.locationTypeId = locationTypeId;
    }

    public int getRadius()
    {
        return radius;
    }

    public void setRadius(int radius)
    {
        this.radius = radius;
    }

    public int getQuotaNumber()
    {
        return quotaNumber;
    }

    public void setQuotaNumber(int quotaNumber)
    {
        this.quotaNumber = quotaNumber;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getUpdateDate()
    {
        return updateDate;
    }

    public void setUpdateDate(String updateDate)
    {
        this.updateDate = updateDate;
    }
    public Integer getcreateUserid()
    {
        return createUserid;
    }

    public void setcreateUserid(Integer createUserid)
    {
        this.createUserid = createUserid;
    }
}

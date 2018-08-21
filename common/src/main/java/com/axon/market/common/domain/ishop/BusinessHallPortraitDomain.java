package com.axon.market.common.domain.ishop;

/**
 * Created by wangtt on 2017/9/11.
 */
public class BusinessHallPortraitDomain
{
    private String businessHallCoding; //  营业厅编码
    private String name; // 营业厅名称
    private String address; // 营业厅地址
    private String locationType; // 分类(营业厅类型) 分别有自有/合作/战略合作
    private String monthTraffic; // 月均人流量，单位人
    private String openDate; // 开厅时间
    private String decorateTime; // 装修时间
    private String areaSize; // 面积大小 单位：平方米
    private String internetSpeed; // 网速 单位：KB/s
    private int wifiNumber; // 智能WIFI个数
    private int wifiUserNumber; // 智能WIFI使用的人数
    private int lightboxNumber; // 灯箱数
    private int taixiNumber; // 台席数
    private int desktopPCNumber; // 台式机数
    private int cloudterminalEqptNumber; // 云终端数
    private int paperlessEqptNumber; // 无纸化设备数
    private String paperlessMonthBusinessNum; // 无纸化设备月均业务量
    private String businessType1; // 可办理业务大类
    private String businessType2; // 可办理业务小类
    private String personalServiceItems; // 个性化服务项枚举
    private int hallpeopleNumber; // 厅内人数
    private String averageAge; // 营业厅人员平均年龄
    private String serviceSatisfaction; // 服务器满意度
    private int kuandaiBusinessNum; // 宽带业务受理量
    private String terminalStoreStatus; // 有无终端产品专卖店
    private String createTime; // 创建时间
    private String createUserId; // 任务创建人id
    private String updateTime; // 更新时间
    private String updateUserId; // 信息更新人的id


    public BusinessHallPortraitDomain()
    {
        setAddress("");
    }

    public BusinessHallPortraitDomain(String businessHallCoding, String name, String address, String locationType, String monthTraffic, String openDate, String decorateTime, String areaSize, String internetSpeed, int wifiNumber, int wifiUserNumber, int lightboxNumber, int taixiNumber, int desktopPCNumber, int cloudterminalEqptNumber, int paperlessEqptNumber, String paperlessMonthBusinessNum, String businessType1, String businessType2, String personalServiceItems, int hallpeopleNumber, String averageAge, String serviceSatisfaction, int kuandaiBusinessNum, String terminalStoreStatus, String createTime, String createUserId, String updateTime, String updateUserId)
    {
        this.businessHallCoding = businessHallCoding;
        this.name = name;
        this.address = address;
        this.locationType = locationType;
        this.monthTraffic = monthTraffic;
        this.openDate = openDate;
        this.decorateTime = decorateTime;
        this.areaSize = areaSize;
        this.internetSpeed = internetSpeed;
        this.wifiNumber = wifiNumber;
        this.wifiUserNumber = wifiUserNumber;
        this.lightboxNumber = lightboxNumber;
        this.taixiNumber = taixiNumber;
        this.desktopPCNumber = desktopPCNumber;
        this.cloudterminalEqptNumber = cloudterminalEqptNumber;
        this.paperlessEqptNumber = paperlessEqptNumber;
        this.paperlessMonthBusinessNum = paperlessMonthBusinessNum;
        this.businessType1 = businessType1;
        this.businessType2 = businessType2;
        this.personalServiceItems = personalServiceItems;
        this.hallpeopleNumber = hallpeopleNumber;
        this.averageAge = averageAge;
        this.serviceSatisfaction = serviceSatisfaction;
        this.kuandaiBusinessNum = kuandaiBusinessNum;
        this.terminalStoreStatus = terminalStoreStatus;
        this.createTime = createTime;
        this.createUserId = createUserId;
        this.updateTime = updateTime;
        this.updateUserId = updateUserId;
    }

    public String getBusinessHallCoding()
    {
        return businessHallCoding;
    }

    public void setBusinessHallCoding(String businessHallCoding)
    {
        this.businessHallCoding = businessHallCoding;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getLocationType()
    {
        return locationType;
    }

    public void setLocationType(String locationType)
    {
        this.locationType = locationType;
    }

    public String getMonthTraffic()
    {
        return monthTraffic;
    }

    public void setMonthTraffic(String monthTraffic)
    {
        this.monthTraffic = monthTraffic;
    }

    public String getOpenDate()
    {
        return openDate;
    }

    public void setOpenDate(String openDate)
    {
        this.openDate = openDate;
    }

    public String getDecorateTime()
    {
        return decorateTime;
    }

    public void setDecorateTime(String decorateTime)
    {
        this.decorateTime = decorateTime;
    }

    public String getAreaSize()
    {
        return areaSize;
    }

    public void setAreaSize(String areaSize)
    {
        this.areaSize = areaSize;
    }

    public String getInternetSpeed()
    {
        return internetSpeed;
    }

    public void setInternetSpeed(String internetSpeed)
    {
        this.internetSpeed = internetSpeed;
    }

    public int getWifiNumber()
    {
        return wifiNumber;
    }

    public void setWifiNumber(int wifiNumber)
    {
        this.wifiNumber = wifiNumber;
    }

    public int getWifiUserNumber()
    {
        return wifiUserNumber;
    }

    public void setWifiUserNumber(int wifiUserNumber)
    {
        this.wifiUserNumber = wifiUserNumber;
    }

    public int getLightboxNumber()
    {
        return lightboxNumber;
    }

    public void setLightboxNumber(int lightboxNumber)
    {
        this.lightboxNumber = lightboxNumber;
    }

    public int getTaixiNumber()
    {
        return taixiNumber;
    }

    public void setTaixiNumber(int taixiNumber)
    {
        this.taixiNumber = taixiNumber;
    }

    public int getDesktopPCNumber()
    {
        return desktopPCNumber;
    }

    public void setDesktopPCNumber(int desktopPCNumber)
    {
        this.desktopPCNumber = desktopPCNumber;
    }

    public int getCloudterminalEqptNumber()
    {
        return cloudterminalEqptNumber;
    }

    public void setCloudterminalEqptNumber(int cloudterminalEqptNumber)
    {
        this.cloudterminalEqptNumber = cloudterminalEqptNumber;
    }

    public int getPaperlessEqptNumber()
    {
        return paperlessEqptNumber;
    }

    public void setPaperlessEqptNumber(int paperlessEqptNumber)
    {
        this.paperlessEqptNumber = paperlessEqptNumber;
    }

    public String getPaperlessMonthBusinessNum()
    {
        return paperlessMonthBusinessNum;
    }

    public void setPaperlessMonthBusinessNum(String paperlessMonthBusinessNum)
    {
        this.paperlessMonthBusinessNum = paperlessMonthBusinessNum;
    }

    public String getBusinessType1()
    {
        return businessType1;
    }

    public void setBusinessType1(String businessType1)
    {
        this.businessType1 = businessType1;
    }

    public String getBusinessType2()
    {
        return businessType2;
    }

    public void setBusinessType2(String businessType2)
    {
        this.businessType2 = businessType2;
    }

    public String getPersonalServiceItems()
    {
        return personalServiceItems;
    }

    public void setPersonalServiceItems(String personalServiceItems)
    {
        this.personalServiceItems = personalServiceItems;
    }

    public int getHallpeopleNumber()
    {
        return hallpeopleNumber;
    }

    public void setHallpeopleNumber(int hallpeopleNumber)
    {
        this.hallpeopleNumber = hallpeopleNumber;
    }

    public String getAverageAge()
    {
        return averageAge;
    }

    public void setAverageAge(String averageAge)
    {
        this.averageAge = averageAge;
    }

    public String getServiceSatisfaction()
    {
        return serviceSatisfaction;
    }

    public void setServiceSatisfaction(String serviceSatisfaction)
    {
        this.serviceSatisfaction = serviceSatisfaction;
    }

    public int getKuandaiBusinessNum()
    {
        return kuandaiBusinessNum;
    }

    public void setKuandaiBusinessNum(int kuandaiBusinessNum)
    {
        this.kuandaiBusinessNum = kuandaiBusinessNum;
    }

    public String getTerminalStoreStatus()
    {
        return terminalStoreStatus;
    }

    public void setTerminalStoreStatus(String terminalStoreStatus)
    {
        this.terminalStoreStatus = terminalStoreStatus;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public String getCreateUserId()
    {
        return createUserId;
    }

    public void setCreateUserId(String createUserId)
    {
        this.createUserId = createUserId;
    }

    public String getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getUpdateUserId()
    {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId)
    {
        this.updateUserId = updateUserId;
    }
}

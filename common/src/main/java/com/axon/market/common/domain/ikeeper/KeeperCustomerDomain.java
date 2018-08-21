package com.axon.market.common.domain.ikeeper;

/**
 * Created by yuanfei on 2017/8/17.
 */
public class KeeperCustomerDomain
{
    /**
     * 姓名
     */
    private String userName;

    /**
     * 手机号码
     */
    private String telephone;

    /**
     * 地市编码
     */
    private String cityCode;

    /**
     * 地市名称
     */
    private String cityName;

    /**
     * 归属系统：1、bss；2、cbss
     */
    private String cbssBssTag;

    /**
     * 用户网别：2、2G；3、3G；4、4G
     */
    private String uType;

    /**
     * 主套餐名称
     */
    private String mainPkg;

    /**
     * 上月arpu值
     */
    private Integer arpuBefor1mon;

    /**
     * 上上月arpu值
     */
    private Integer arpuBefor2mon;

    /**
     * 上上上月arpu值
     */
    private Integer arpuBefor3mon;

    /**
     * 近三个月平均流量
     */
    private Integer avgFlow3mon;

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getTelephone()
    {
        return telephone;
    }

    public void setTelephone(String telephone)
    {
        this.telephone = telephone;
    }

    public String getCityCode()
    {
        return cityCode;
    }

    public void setCityCode(String cityCode)
    {
        this.cityCode = cityCode;
    }

    public String getCityName()
    {
        return cityName;
    }

    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }

    public String getCbssBssTag()
    {
        return cbssBssTag;
    }

    public void setCbssBssTag(String cbssBssTag)
    {
        this.cbssBssTag = cbssBssTag;
    }

    public String getuType()
    {
        return uType;
    }

    public void setuType(String uType)
    {
        this.uType = uType;
    }

    public String getMainPkg()
    {
        return mainPkg;
    }

    public void setMainPkg(String mainPkg)
    {
        this.mainPkg = mainPkg;
    }

    public Integer getArpuBefor1mon()
    {
        return arpuBefor1mon;
    }

    public void setArpuBefor1mon(Integer arpuBefor1mon)
    {
        this.arpuBefor1mon = arpuBefor1mon;
    }

    public Integer getArpuBefor2mon()
    {
        return arpuBefor2mon;
    }

    public void setArpuBefor2mon(Integer arpuBefor2mon)
    {
        this.arpuBefor2mon = arpuBefor2mon;
    }

    public Integer getArpuBefor3mon()
    {
        return arpuBefor3mon;
    }

    public void setArpuBefor3mon(Integer arpuBefor3mon)
    {
        this.arpuBefor3mon = arpuBefor3mon;
    }

    public Integer getAvgFlow3mon()
    {
        return avgFlow3mon;
    }

    public void setAvgFlow3mon(Integer avgFlow3mon)
    {
        this.avgFlow3mon = avgFlow3mon;
    }
}

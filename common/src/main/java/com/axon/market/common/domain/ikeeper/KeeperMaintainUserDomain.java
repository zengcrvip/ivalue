package com.axon.market.common.domain.ikeeper;

/**
 * Created by wangtt on 2017/8/23.
 */
public class KeeperMaintainUserDomain
{
    private String userName;   // 用户姓名
    private String userPhone;// 用户手机号
    private String userCode; // 用户编码
    private String userAreaCode; // 用户地区编码
    private String maintainUserName;// 维系员工姓名
    private String maintainUserPhone;// 维系员工手机号
    private String userQQ; // 用户qq
    private String userWangWang;// 用户旺旺
    private String userWeiXin;// 用户微信
    private String userWeiBo;// 用户微博


    public KeeperMaintainUserDomain()
    {
    }

    public KeeperMaintainUserDomain(String userName, String userPhone,String userCode, String userAreaCode, String maintainUserName, String maintainUserPhone,String userWeiXin,String userQQ,String userWangWang,String userWeiBo)
    {
        this.userName = userName;
        this.userPhone = userPhone;
        this.userCode  = userCode;
        this.userAreaCode = userAreaCode;
        this.maintainUserName = maintainUserName;
        this.maintainUserPhone = maintainUserPhone;
        this.userWeiXin = userWeiXin;
        this.userQQ = userQQ;
        this.userWangWang = userWangWang;
        this.userWeiBo = userWeiBo;
    }

    public String getUserCode()
    {
        return userCode;
    }

    public void setUserCode(String userCode)
    {
        this.userCode = userCode;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserPhone()
    {
        return userPhone;
    }

    public void setUserPhone(String userPhone)
    {
        this.userPhone = userPhone;
    }

    public String getUserAreaCode()
    {
        return userAreaCode;
    }

    public void setUserAreaCode(String userAreaCode)
    {
        this.userAreaCode = userAreaCode;
    }

    public String getMaintainUserName()
    {
        return maintainUserName;
    }

    public void setMaintainUserName(String maintainUserName)
    {
        this.maintainUserName = maintainUserName;
    }

    public String getMaintainUserPhone()
    {
        return maintainUserPhone;
    }

    public void setMaintainUserPhone(String maintainUserPhone)
    {
        this.maintainUserPhone = maintainUserPhone;
    }

    public String getUserQQ()
    {
        return userQQ;
    }

    public void setUserQQ(String userQQ)
    {
        this.userQQ = userQQ;
    }

    public String getUserWangWang()
    {
        return userWangWang;
    }

    public void setUserWangWang(String userWangWang)
    {
        this.userWangWang = userWangWang;
    }

    public String getUserWeiXin()
    {
        return userWeiXin;
    }

    public void setUserWeiXin(String userWeiXin)
    {
        this.userWeiXin = userWeiXin;
    }

    public String getUserWeiBo()
    {
        return userWeiBo;
    }

    public void setUserWeiBo(String userWeiBo)
    {
        this.userWeiBo = userWeiBo;
    }
}

package com.axon.market.common.domain.ikeeper;

/**
 * Created by wangtt on 2017/8/10.
 */
public class UserMaintainDomain
{
    // 用户编号
    private Integer id;
    // 用户名称
    private String userName;
    // 用户手机
    private String userPhone;
    // 用户编码
    private String userCode;
    // 用户地区编码
    private String userAreaCode;
    // 维系用户Id
    private Integer maintainUserId;
    // 维系用户名
    private String maintainUserName;
    //维系用户手机
    private String maintainUserPhone;
    // 用户微信
    private String userWeiXin;
    // 用户微博
    private String userWeiBo;
    // 用户QQ
    private String userQQ;
    // 用户旺旺号
    private String userWangWang;
    // 用户状态
    private String status;
    // 描述
    private String desc;

    public String getUserCode()
    {
        return userCode;
    }

    public void setUserCode(String userCode)
    {
        this.userCode = userCode;
    }

    public String getMaintainUserPhone()
    {
        return maintainUserPhone;
    }

    public void setMaintainUserPhone(String maintainUserPhone)
    {
        this.maintainUserPhone = maintainUserPhone;
    }

    public String getMaintainUserName()
    {
        return maintainUserName;
    }

    public void setMaintainUserName(String maintainUserName)
    {
        this.maintainUserName = maintainUserName;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
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

    public Integer getMaintainUserId()
    {
        return maintainUserId;
    }

    public void setMaintainUserId(Integer maintainUserId)
    {
        this.maintainUserId = maintainUserId;
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

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }
}

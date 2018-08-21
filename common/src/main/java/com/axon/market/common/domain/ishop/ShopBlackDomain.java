package com.axon.market.common.domain.ishop;

/**
 * 炒店黑名单实体类
 * Created by zengcr on 2017/4/12.
 */
public class ShopBlackDomain
{
    //主键
    private Long id;
    //号码
    private String phone;
    //屏蔽开始时间
    private String hideStartTime;
    //屏蔽结束时间
    private String hideEndTime;
    //屏蔽地区
    private String hideArea;
    //屏蔽的炒店
    private String hideBases;
    //创建人
    private Integer createUser;
    //屏蔽范围
    private String hideContent;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getHideStartTime()
    {
        return hideStartTime;
    }

    public void setHideStartTime(String hideStartTime)
    {
        this.hideStartTime = hideStartTime;
    }

    public String getHideEndTime()
    {
        return hideEndTime;
    }

    public void setHideEndTime(String hideEndTime)
    {
        this.hideEndTime = hideEndTime;
    }

    public String getHideArea()
    {
        return hideArea;
    }

    public void setHideArea(String hideArea)
    {
        this.hideArea = hideArea;
    }

    public String getHideBases()
    {
        return hideBases;
    }

    public void setHideBases(String hideBases)
    {
        this.hideBases = hideBases;
    }

    public Integer getCreateUser()
    {
        return createUser;
    }

    public void setCreateUser(Integer createUser)
    {
        this.createUser = createUser;
    }

    public String getHideContent()
    {
        return hideContent;
    }

    public void setHideContent(String hideContent)
    {
        this.hideContent = hideContent;
    }
}

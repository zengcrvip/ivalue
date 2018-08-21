package com.axon.market.common.domain.icommon.market;

/**
 * Created by chenyu on 2017/2/15.
 */
public class JumpLinkDomain
{
    private String uId;

    private String longLink;

    private Long mob;

    private String province;

    private Integer taskId;

    public String getuId()
    {
        return uId;
    }

    public void setuId(String uId)
    {
        this.uId = uId;
    }

    public String getLongLink()
    {
        return longLink;
    }

    public void setLongLink(String longLink)
    {
        this.longLink = longLink;
    }

    public Long getMob()
    {
        return mob;
    }

    public void setMob(Long mob)
    {
        this.mob = mob;
    }

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public Integer getTaskId()
    {
        return taskId;
    }

    public void setTaskId(Integer taskId)
    {
        this.taskId = taskId;
    }
}

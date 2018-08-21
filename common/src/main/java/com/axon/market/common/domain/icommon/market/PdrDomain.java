package com.axon.market.common.domain.icommon.market;

/**
 * Created by chenyu on 2017/2/15.
 */
public class PdrDomain
{
    private Integer pId;

    private String uId;

    private Long mob;

    private String smsUrl;

    private String smsContent;

    private Long clickTime;

    private Integer clickFlag;

    private Integer taskId;

    private Long sendTime;

    public Integer getpId()
    {
        return pId;
    }

    public void setpId(Integer pId)
    {
        this.pId = pId;
    }

    public String getuId()
    {
        return uId;
    }

    public void setuId(String uId)
    {
        this.uId = uId;
    }

    public Long getMob()
    {
        return mob;
    }

    public void setMob(Long mob)
    {
        this.mob = mob;
    }

    public String getSmsUrl()
    {
        return smsUrl;
    }

    public void setSmsUrl(String smsUrl)
    {
        this.smsUrl = smsUrl;
    }

    public String getSmsContent()
    {
        return smsContent;
    }

    public void setSmsContent(String smsContent)
    {
        this.smsContent = smsContent;
    }

    public Long getClickTime()
    {
        return clickTime;
    }

    public void setClickTime(Long clickTime)
    {
        this.clickTime = clickTime;
    }

    public Integer getClickFlag()
    {
        return clickFlag;
    }

    public void setClickFlag(Integer clickFlag)
    {
        this.clickFlag = clickFlag;
    }

    public Integer getTaskId()
    {
        return taskId;
    }

    public void setTaskId(Integer taskId)
    {
        this.taskId = taskId;
    }

    public Long getSendTime()
    {
        return sendTime;
    }

    public void setSendTime(Long sendTime)
    {
        this.sendTime = sendTime;
    }
}

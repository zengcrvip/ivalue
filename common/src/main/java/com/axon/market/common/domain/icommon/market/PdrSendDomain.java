package com.axon.market.common.domain.icommon.market;

/**
 * Created by chenyu on 2017/2/15.
 */
public class PdrSendDomain
{
    private Long phone;

    private Integer sendFlag;

    private Integer taskId;

    private Long sendTime;

    private Integer pId;

    private Integer cId;

    private Long serialNum;

    private String message;

    private Integer recvFlag;

    public Long getPhone()
    {
        return phone;
    }

    public void setPhone(Long phone)
    {
        this.phone = phone;
    }

    public Integer getSendFlag()
    {
        return sendFlag;
    }

    public void setSendFlag(Integer sendFlag)
    {
        this.sendFlag = sendFlag;
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

    public Integer getpId()
    {
        return pId;
    }

    public void setpId(Integer pId)
    {
        this.pId = pId;
    }

    public Integer getcId()
    {
        return cId;
    }

    public void setcId(Integer cId)
    {
        this.cId = cId;
    }

    public Long getSerialNum()
    {
        return serialNum;
    }

    public void setSerialNum(Long serialNum)
    {
        this.serialNum = serialNum;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public Integer getRecvFlag()
    {
        return recvFlag;
    }

    public void setRecvFlag(Integer recvFlag)
    {
        this.recvFlag = recvFlag;
    }
}

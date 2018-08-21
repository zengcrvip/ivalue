package com.axon.market.common.domain.iscene;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by wangtt on 2017/3/6.
 */
public class UserBlackListDomain
{
    private BigInteger id;//ID
    private String mobile;//黑名单用户手机号
    private String taskId;//任务ID(-1全部屏蔽||不是-1按照设置屏蔽)
    private Date blockStart;//黑名单屏蔽开始时间
    private Date blockEnd;//黑名单屏蔽结束时间
    private int isDelete;//是否删除
    private int blockType;//屏蔽类型(1 周期||2 间隔)
    private String taskName;//任务名称

    public String getTaskName()
    {
        return taskName;
    }

    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }

    public UserBlackListDomain()
    {
    }

    public UserBlackListDomain(BigInteger id, String mobile, String taskId, Date blockStart, Date blockEnd, int isDelete, int blockType)
    {
        this.id = id;
        this.mobile = mobile;
        this.taskId = taskId;
        this.blockStart = blockStart;
        this.blockEnd = blockEnd;
        this.isDelete = isDelete;
        this.blockType = blockType;
    }

    public BigInteger getId()
    {
        return id;
    }

    public void setId(BigInteger id)
    {
        this.id = id;
    }

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public String getTaskId()
    {
        return taskId;
    }

    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }

    public Date getBlockStart()
    {
        return blockStart;
    }

    public void setBlockStart(Date blockStart)
    {
        this.blockStart = blockStart;
    }

    public Date getBlockEnd()
    {
        return blockEnd;
    }

    public void setBlockEnd(Date blockEnd)
    {
        this.blockEnd = blockEnd;
    }

    public int getIsDelete()
    {
        return isDelete;
    }

    public void setIsDelete(int isDelete)
    {
        this.isDelete = isDelete;
    }

    public int getBlockType()
    {
        return blockType;
    }

    public void setBlockType(int blockType)
    {
        this.blockType = blockType;
    }
}

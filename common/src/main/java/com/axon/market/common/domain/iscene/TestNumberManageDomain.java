package com.axon.market.common.domain.iscene;

/**
 * Created by DELL on 2016/12/12.
 */
public class TestNumberManageDomain
{
    private String mob;//手机号

    private String taskId;//任务ID

    public TestNumberManageDomain()
    {
    }

    public TestNumberManageDomain(String mob, String taskId)
    {
        this.mob = mob;
        this.taskId = taskId;
    }

    public String getMob()
    {
        return mob;
    }

    public void setMob(String mob)
    {
        this.mob = mob;
    }

    public String getTaskId()
    {
        return taskId;
    }

    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }
}

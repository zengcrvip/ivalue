package com.axon.market.common.domain.iscene;

import java.util.Date;

/**
 * Created by xuan on 2017/4/6.
 */
public class StatsDomain
{
    private int id;
    private int taskId;
    private String name;
    private int pilotShowPV;
    private int pilotShowUV;
    private int pilotClickPV;
    private int pilotClickUV;
    private int pilotClosePV;
    private int pilotCloseUV;
    private Date updateDatetime;
    private int apiInvokePV;
    private String extStopCond;
    private int pilotShowPVTotal;
    private int pilotShowUVTotal;
    private int countUser;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getTaskId()
    {
        return taskId;
    }

    public void setTaskId(int taskId)
    {
        this.taskId = taskId;
    }
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    public int getPilotShowPV()
    {
        return pilotShowPV;
    }

    public void setPilotShowPV(int pilotShowPV)
    {
        this.pilotShowPV = pilotShowPV;
    }

    public int getPilotShowUV()
    {
        return pilotShowUV;
    }

    public void setPilotShowUV(int pilotShowUV)
    {
        this.pilotShowUV = pilotShowUV;
    }

    public int getPilotClickPV()
    {
        return pilotClickPV;
    }

    public void setPilotClickPV(int pilotClickPV)
    {
        this.pilotClickPV = pilotClickPV;
    }

    public int getPilotClickUV()
    {
        return pilotClickUV;
    }

    public void setPilotClickUV(int pilotClickUV)
    {
        this.pilotClickUV = pilotClickUV;
    }

    public int getPilotClosePV()
    {
        return pilotClosePV;
    }

    public void setPilotClosePV(int pilotClosePV)
    {
        this.pilotClosePV = pilotClosePV;
    }

    public int getPilotCloseUV()
    {
        return pilotCloseUV;
    }

    public void setPilotCloseUV(int pilotCloseUV)
    {
        this.pilotCloseUV = pilotCloseUV;
    }

    public Date getUpdateDatetime()
    {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime)
    {
        this.updateDatetime = updateDatetime;
    }

    public int getApiInvokePV()
    {
        return apiInvokePV;
    }

    public void setApiInvokePV(int apiInvokePV)
    {
        this.apiInvokePV = apiInvokePV;
    }

    public String getExtStopCond()
    {
        return extStopCond;
    }

    public void setExtStopCond(String extStopCond)
    {
        this.extStopCond = extStopCond;
    }

    public int getPilotShowPVTotal()
    {
        return pilotShowPVTotal;
    }

    public void setPilotShowPVTotal(int pilotShowPVTotal)
    {
        this.pilotShowPVTotal = pilotShowPVTotal;
    }

    public int getPilotShowUVTotal()
    {
        return pilotShowUVTotal;
    }

    public void setPilotShowUVTotal(int pilotShowUVTotal)
    {
        this.pilotShowUVTotal = pilotShowUVTotal;
    }

    public int getcountUser()
    {
        return countUser;
    }

    public void setcountUser(int countUser)
    {
        this.countUser = countUser;
    }
}

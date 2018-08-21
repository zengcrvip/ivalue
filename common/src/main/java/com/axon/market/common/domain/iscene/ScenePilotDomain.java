package com.axon.market.common.domain.iscene;

/**
 * Created by xuan on 2017/2/8.
 */
public class ScenePilotDomain
{
    private int id;
    private String name;
    private int pilotType;
    private String pilotUrl;
    private int intervarTime;
    private String onLineTm;
    private String offLineTm;
    private int blockMode;
    private String sceneIds;
    private String urlGroupIds;
    private  String locationGroupIds;
    private String urlGroupNames;
    private String locationGroupNames;
    private String imgUrl;
    private String extStopCond;
    private int isDelete;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getPilotType()
    {
        return pilotType;
    }

    public void setPilotType(int pilotType)
    {
        this.pilotType = pilotType;
    }

    public String getPilotUrl()
    {
        return pilotUrl;
    }

    public void setPilotUrl(String pilotUrl)
    {
        this.pilotUrl = pilotUrl;
    }

    public int getIntervarTime()
    {
        return intervarTime;
    }

    public void setIntervarTime(int intervarTime)
    {
        this.intervarTime = intervarTime;
    }

    public String getOnLineTm()
    {
        return onLineTm;
    }

    public void setOnLineTm(String onLineTm)
    {
        this.onLineTm = onLineTm;
    }

    public String getOffLineTm()
    {
        return offLineTm;
    }

    public void setOffLineTm(String offLineTm)
    {
        this.offLineTm = offLineTm;
    }

    public int getBlockMode()
    {
        return blockMode;
    }

    public void setBlockMode(int blockMode)
    {
        this.blockMode = blockMode;
    }

    public String getSceneIds()
    {
        return sceneIds;
    }

    public void setSceneIds(String sceneIds)
    {
        this.sceneIds = sceneIds;
    }

    public String getUrlGroupIds()
    {
        return urlGroupIds;
    }

    public void setUrlGroupIds(String urlGroupIds)
    {
        this.urlGroupIds = urlGroupIds;
    }

    public String getLocationGroupIds()
    {
        return locationGroupIds;
    }

    public void setLocationGroupIds(String locationGroupIds)
    {
        this.locationGroupIds = locationGroupIds;
    }

    public String getUrlGroupNames()
    {
        return urlGroupNames;
    }

    public void setUrlGroupNames(String urlGroupNames)
    {
        this.urlGroupNames = urlGroupNames;
    }

    public String getLocationGroupNames()
    {
        return locationGroupNames;
    }

    public void setLocationGroupNames(String locationGroupNames)
    {
        this.locationGroupNames = locationGroupNames;
    }

    public String getImgUrl()
    {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl)
    {
        this.imgUrl = imgUrl;
    }

    public String getExtStopCond()
    {
        return extStopCond;
    }

    public void setExtStopCond(String extStopCond)
    {
        this.extStopCond = extStopCond;
    }

    public int getIsDelete()
    {
        return isDelete;
    }

    public void setIsDelete(int isDelete)
    {
        this.isDelete = isDelete;
    }
}

package com.axon.market.core.rule;

import java.util.List;

/**
 * Created by yangyang on 2016/1/27.
 */
public class ExtraFilterCondition
{
    private String dataDate;

    private List<String> areaId;

    private String imsi;

    public String getDataDate()
    {
        return dataDate;
    }

    public void setDataDate(String dataDate)
    {
        this.dataDate = dataDate;
    }

    public List<String> getAreaId()
    {
        return areaId;
    }

    public void setAreaId(List<String> areaId)
    {
        this.areaId = areaId;
    }

    public String getImsi()
    {
        return imsi;
    }

    public void setImsi(String imsi)
    {
        this.imsi = imsi;
    }
}

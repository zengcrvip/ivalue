package com.axon.market.common.domain.iservice;

/**
 * 返回给客户端的基站信息
 * Created by zengcr on 2017/1/21.
 */
public class BaseInfo
{
    //操作类型
    private Integer operate;
    //基站点名称
    private String baseName;
    //基站点ID
    private Integer baseId;

    public Integer getOperate()
    {
        return operate;
    }

    public void setOperate(Integer operate)
    {
        this.operate = operate;
    }

    public String getBaseName()
    {
        return baseName;
    }

    public void setBaseName(String baseName)
    {
        this.baseName = baseName;
    }

    public Integer getBaseId()
    {
        return baseId;
    }

    public void setBaseId(Integer baseId)
    {
        this.baseId = baseId;
    }

}

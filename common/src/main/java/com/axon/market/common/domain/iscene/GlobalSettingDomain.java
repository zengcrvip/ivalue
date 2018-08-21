package com.axon.market.common.domain.iscene;

/**
 * Created by Administrator on 2016/12/1.
 */
public class GlobalSettingDomain
{
    private Integer id;

    private Integer type;

    private Integer num;

    //private Long isDel;

    public GlobalSettingDomain()
    {
    }

    public GlobalSettingDomain(Integer id, Integer type, Integer num)
    {
        this.id = id;
        this.type = type;
        this.num = num;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getNum()
    {
        return num;
    }

    public void setNum(Integer num)
    {
        this.num = num;
    }
}

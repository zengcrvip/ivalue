package com.axon.market.common.constant.iscene;

/**
 * Created by Administrator on 2016/12/9.
 */
public enum ShieldTypeEnum
{
    PERIOD("周期屏蔽", 0),
    INTERVAL("间隔屏蔽", 1);

    private String name;

    private int index;

    ShieldTypeEnum(String name, int index)
    {
        this.name = name;
        this.index = index;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }
}

package com.axon.market.common.constant.iscene;

/**
 * Created by Administrator on 2016/12/9.
 */
public enum PeriodEnum
{
    ONE("1个月", 1),
    TWO("2个月", 2),
    SIX("6个月", 3),
    TWELVE("1年", 4);

    private String name;

    private int index;

    PeriodEnum(String name, int index)
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

package com.axon.market.common.constant.iscene;

/**
 * Created by xuan on 2016/12/9.
 */
public enum NetWorkEnum
{
    TWOORTHREEG("2G/3G", 1),
    FOURG("4G", 2);

    private String name;

    private int index;

    NetWorkEnum(String name, int index)
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

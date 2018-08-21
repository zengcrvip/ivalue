package com.axon.market.common.constant.iscene;

/**
 * Created by xuan on 2016/12/7.
 */
public enum PushConfigEnum
{
    FlowPackage("流量包", 1),
    Apps("应用", 2);

    private String name;

    private int index;

    PushConfigEnum(String name, int index)
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

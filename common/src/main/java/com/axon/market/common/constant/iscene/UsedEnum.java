package com.axon.market.common.constant.iscene;

/**
 * Created by xuan on 2016/12/8.
 */
public enum UsedEnum
{
    USED("启用", 1),
    NOTUSED("不启用", 0);

    private String name;

    private int index;

    UsedEnum(String name, int index)
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

package com.axon.market.common.constant.iscene;

/**
 * Created by xuan on 2016/12/26.
 */
public enum BlockTypeEnum
{
    WHITE("白名单", 0), BLACK("黑名单", 1);
    private String name;
    private int index;

    BlockTypeEnum(String name, int index)
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

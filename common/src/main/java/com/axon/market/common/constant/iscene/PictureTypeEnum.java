package com.axon.market.common.constant.iscene;

/**
 * Created by xuan on 2016/12/8.
 */
public enum PictureTypeEnum
{
    MULTI("多图模式", 1),
    SINGLE("单图模式", 2);

    private String name;

    private int index;

    PictureTypeEnum(String name, int index)
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

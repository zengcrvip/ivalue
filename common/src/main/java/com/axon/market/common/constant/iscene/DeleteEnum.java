package com.axon.market.common.constant.iscene;

/**
 * Created by Administrator on 2016/12/15.
 */
public enum DeleteEnum
{
    NOTDELETE("未删除", 0),
    DELETED("已删除", 1);

    private String name;

    private int index;

    DeleteEnum(String name, int index)
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

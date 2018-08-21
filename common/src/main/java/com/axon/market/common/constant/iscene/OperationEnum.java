package com.axon.market.common.constant.iscene;

/**
 * Created by Administrator on 2016/12/12.
 */
public enum OperationEnum
{
    EDIT("修改", 1),
    DELETE("删除", 2);

    private String name;

    private int index;

    OperationEnum(String name, int index)
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

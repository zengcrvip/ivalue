package com.axon.market.common.constant.iscene;

/**
 * Created by xuan on 2016/12/27.
 */
public enum TaskTypeEnum
{
    VNAV("场景导航", 2), ALLPAGE("全页面导航", 1);
    private String name;
    private int index;

    TaskTypeEnum(String name, int index)
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

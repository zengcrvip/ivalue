package com.axon.market.common.constant.iscene;

/**
 * Created by xuan on 2016/12/26.
 */
public enum TaskStateEnum
{
    RUNING("正在执行", 0), PAUSE("暂停", 1),PREPARE("计划执行", 2), CHECK("待审核", 3);
    private String name;
    private int index;

    TaskStateEnum(String name, int index)
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

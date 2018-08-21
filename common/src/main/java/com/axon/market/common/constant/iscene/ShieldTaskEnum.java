package com.axon.market.common.constant.iscene;

/**
 * Created by Administrator on 2016/12/9.
 */
public enum ShieldTaskEnum
{
    ALLTASK("屏蔽所有任务", 0),
    SELECTTASK("屏蔽选择任务", 1);

    private String name;

    private int index;

    ShieldTaskEnum(String name, int index)
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

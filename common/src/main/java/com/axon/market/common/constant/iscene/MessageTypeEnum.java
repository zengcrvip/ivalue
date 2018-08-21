package com.axon.market.common.constant.iscene;

/**
 * Created by xuan on 2016/12/27.
 */
public enum MessageTypeEnum
{
    RUNNING("执行", 51), PAUSE("暂停", 52),USERBLACKADD("用户黑名单新增", 53), USERBLACKDELETE("用户黑名单删除", 54),AUDIT("审核", 55);
    private String name;
    private int index;

    MessageTypeEnum(String name, int index)
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

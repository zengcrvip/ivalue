package com.axon.market.common.constant.icommon;

/**
 * Created by yuanfei on 2017/1/20.
 */
public enum CategoryTypeEnum
{
    CT_META_DATA(1),//元数据
    CT_MODEL(2),//模型
    CT_TAG(3),//标签
    CT_MARKETING_TASK(4),//营销任务
    CT_CONTENT(5),
    CT_PRODUCT(6),
    CT_SCENE(7);

    private Integer value;

    private CategoryTypeEnum(Integer value)
    {
        this.value = value;
    }

    public Integer getValue()
    {
        return value;
    }
}

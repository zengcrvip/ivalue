package com.axon.market.common.constant.ishop;

/**
 * Created by zw on 2017/6/16.
 */
public enum ShopTaskClassifyEnum {
    TASK_SHOP(1),//炒店任务
    TASK_TEMP_PROMOTION_XY(2),//临时摊点（校园）
    TASK_TEMP_PROMOTION_JK(3),//临时摊点（集客）
    TASK_TEMP_PROMOTION_GZ(4);//临时摊点（公众）


    private Integer value;

    private ShopTaskClassifyEnum(Integer value)
    {
        this.value = value;
    }

    public Integer getValue()
    {
        return value;
    }
}

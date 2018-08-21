package com.axon.market.common.constant.iconsumption;

/**
 * Created by Zhuwen on 2017/8/2.
 */
public enum DixiaoTaskStatusEnum {
    TASK_INIT(0),//初始
    TASK_IMPORT(1),//入库
    TASK_IMPORT_FAIL(-1),//入库失败
    TASK_RANKTYPE_CHOOSE(2),//档位类型选择完毕
    TASK_RANKTYPE_CHOOSE_FAIL(-2),//档位类型选择失败
    TASK_CITY_CHOOSE(3),//地市待分配
    TASK_CITY_CHOOSE_FAIL(-3),//地市待分配失败
    TASK_CITY_CONFIRM(4),//地市分配锁定
    TASK_CITY_CONFIRM_FAIL(-4),//地市分配锁定失败
    TASK_CHOOSE_FINISH_OFFLINE(5),//线下分配结束，等待通知话+
    TASK_CHOOSE_FINISH_OFFLINE_FAIL(-5),//线下分配结束，等待通知话+失败
    TASK_PUSH_OFFLINE(6),//线下推送话+成功
    TASK_PUSH_OFFLINE_FAIL(-6),//线下推送话+失败
    TASK_CHOOSE_FINISH_ONLINE(7),//线上分配结束，等待通知话+
    TASK_CHOOSE_FINISH_ONLINE_FAIL(-7),//线上分配结束，等待通知话+失败
    TASK_PUSH_ONLINE(8),//线上推送话+成功
    TASK_PUSH_ONLINE_FAIL(-8),//线上推送话+失败
    TASK_EXPIRE(10);//任务失效


    private Integer value;

    private DixiaoTaskStatusEnum(Integer value)
    {
        this.value = value;
    }

    public Integer getValue()
    {
        return value;
    }

}

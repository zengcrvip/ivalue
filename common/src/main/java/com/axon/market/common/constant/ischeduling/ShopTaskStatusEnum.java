package com.axon.market.common.constant.ischeduling;

/**
 * 炒店任务状态枚举
 * Created by zengcr on 2017/2/16.
 */
public enum ShopTaskStatusEnum
{
    TASK_DELETE(-1),//已删除
    TASK_DRAFT(0),//草稿
    TASK_AUDITING(1),//审批中
    TASK_READY(2),//审批通过/准备就绪
    TASK_FAIL(3),//审核失败
    TASK_PAUSE(4),//暂停中
    TASK_FAILURE(5),//已失效
    TASK_STOP(6),//已终止
    TASK_MARKET_SEND(20),//营销请求发送中
    TASK_MARKET_ALL_SUCCESS(24), //常驻用户场景跟流动用户场景都成功
    TASK_MARKET_ALL_FAIL(16),//常驻用户场景跟流动用户场景都失败
    TASK_MARKET_ALL_PER_SUCCESS(22),//常驻用户场景失败，流动用户场景成功
    TASK_MARKET_ALL_FLOW_SUCCESS(18),//常驻用户场景成功，流动用户场景失败
    TASK_MARKET_PER_SUCCESS(21),//常驻用户场景成功
    TASK_MARKET_FLOW_SUCCESS(23),//流动用户场景成功
    TASK_MARKET_PER_FAIL(19),//常驻用户场景失败
    TASK_MARKET_FLOW_FAIL(17),//流动用户场景失败
    TASK_MARKET_FINISHED(35),// 营销任务营销结束
    TASK_MARKET_FOR_DEAL(30),//待处理
    TASK_MARKET_FAIL(36),//营销失败
    TASK_EXECUTE(40);//自动执行任务已点过执行


    private Integer value;

    private ShopTaskStatusEnum(Integer value)
    {
        this.value = value;
    }

    public Integer getValue()
    {
        return value;
    }
}

package com.axon.market.common.constant.ishop;

/**
 * Created by Duzm on 2017/8/12.
 * 这里定义的维系规则常量是各种任务维系规则的并集，任务规则不允许一样
 * 任务规则在数据库对应的属性为：EXP_BIRTHDAY_TASK_RULE、EXP_TWO2FOUR_RULE、EXP_SCENE_CARE_RULE、EXP_DISCOUNT_EXPIRY_RULE
 * 本常量类定义的就是这些属性对应的属性值market_attr_value.inner_value，这些值不能出现相同的值
 */
public interface TaskRuleConstants {
    
    //生日前三天
    String BIRTHDAY_RULE_BEFORE_THREE = "3";
    
    //换机排名TOP10
    String TWO2FOUR_RULE_TOP10 = "10";
    
    //用户套内流量使用超过80%
    String SCENE_CARE_PKG_OVER_80 = "80";
    
    //优惠到期续航
    String DISCOUNT_EXPIRY_ENDURANCE = "endurance";
    
    //优惠到期搭桥
    String DISCOUNT_EXPIRY_CROSSLINK = "crosslink";
    
    //优惠到期合约到期
    String DISCOUNT_EXPIRY_EXPIRING = "expiring";
}

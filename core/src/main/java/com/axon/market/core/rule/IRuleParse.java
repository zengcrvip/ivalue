package com.axon.market.core.rule;

import java.util.List;

/**
 * Created by yangyang on 2016/1/26.
 */
public interface IRuleParse
{

    /**
     * @param ruleNodes
     * @param extraFilterCondition
     * @return
     */
    String parseRuleToSql(List<RuleNode> ruleNodes, ExtraFilterCondition extraFilterCondition);

    /**
     * jsonRule:List<RuleNode>形成的json规则
     * @param jsonRule
     * @param extraFilterCondition
     * @return
     */
    String parseRuleToSql(String jsonRule, ExtraFilterCondition extraFilterCondition);

}

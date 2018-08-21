package com.axon.market.core.rule;

import java.util.List;

/**
 * Created by chenyu on 2016/7/12.
 */
public class RuleParseImpl implements IRuleParse
{
    private IRuleParse ruleParse;

    public void setRuleParse(IRuleParse ruleParse)
    {
        this.ruleParse = ruleParse;
    }

    @Override
    public String parseRuleToSql(List<RuleNode> ruleNodes, ExtraFilterCondition extraFilterCondition)
    {
        return ruleParse.parseRuleToSql(ruleNodes, extraFilterCondition);
    }

    @Override
    public String parseRuleToSql(String jsonRule, ExtraFilterCondition extraFilterCondition)
    {
        return ruleParse.parseRuleToSql(jsonRule, extraFilterCondition);
    }
}

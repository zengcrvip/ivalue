package com.axon.market.core.rule.impl;

import com.axon.market.core.rule.ExtraFilterCondition;
import com.axon.market.core.rule.RuleNode;

/**
 * Created by yangyang on 2016/1/26.
 */
public interface INodeParse
{
    /**
     *
     * @param node
     * @param extraFilterCondition
     * @return
     */
    String nodeToSql(RuleNode node, ExtraFilterCondition extraFilterCondition);
}

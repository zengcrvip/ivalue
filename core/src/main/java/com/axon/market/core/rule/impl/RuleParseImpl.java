package com.axon.market.core.rule.impl;

import com.axon.market.common.util.JsonUtil;
import com.axon.market.core.rule.ExtraFilterCondition;
import com.axon.market.core.rule.IRuleParse;
import com.axon.market.core.rule.RuleNode;
import com.axon.market.core.rule.RuleNodeConstant;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangyang on 2016/1/25.
 */
public class RuleParseImpl implements IRuleParse
{

    private static final Logger LOG = Logger.getLogger(RuleParseImpl.class.getName());

    /**
     * jsonRule解析后的RuleNode列表
     */
    private List<RuleNode> ruleNodes;

    /**
     * 针对某表的额外附加条件（用于过滤特定周期、特定区域用户）
     */
    private ExtraFilterCondition extraFilterCondition;

    //属性节点转换为sql,spring注入
    private Map<String, INodeParse> nodeParseMap;

    public void setNodeParseMap(Map<String, INodeParse> nodeParseMap)
    {
        this.nodeParseMap = nodeParseMap;
    }

    /**
     * 需要使用GreenPlum的sql关键字 Union all、Intersect all 、Except
     * <br>
     * 优点：不需要全量用户表，代码简洁
     * <br>
     * 缺点：性能可能不如left join + 组合条件过滤
     *
     * @param ruleNodes
     * @param extraFilterCondition
     * @return
     */
    @Override
    public String parseRuleToSql(List<RuleNode> ruleNodes, ExtraFilterCondition extraFilterCondition)
    {
        if (ruleNodes == null)
        {
            throw new IllegalArgumentException("Rule Nodes Must Be Not Null");
        }
        else
        {
            this.ruleNodes = ruleNodes;
        }

        this.extraFilterCondition = extraFilterCondition;

        RuleNode root = getRootRuleNode();

        return recursiveParseSql(root, staticNodes());
    }

    /**
     * 需要使用GreenPlum的sql关键字 Union all、Intersect all 、Except
     * <br>
     * 优点：不需要全量用户表，代码简洁
     * <br>
     * 缺点：性能可能不如left join + 组合条件过滤
     *
     * @param jsonRule
     * @param extraFilterCondition
     * @return
     */
    @Override
    public String parseRuleToSql(String jsonRule, ExtraFilterCondition extraFilterCondition)
    {
        try
        {
            this.ruleNodes = JsonUtil.stringToObject(jsonRule, new TypeReference<List<RuleNode>>()
            {
            });
        }
        catch (IOException e)
        {
            LOG.error("Parse Rule To Sql Error. " + e);
            throw new RuntimeException("Parse Rule To Sql Error : " + jsonRule, e);
        }
        return parseRuleToSql(this.ruleNodes, extraFilterCondition);
    }

    /**
     * @param root
     * @param staticNodes
     * @return
     */
    private String recursiveParseSql(RuleNode root, final Map<String, List<RuleNode>> staticNodes)
    {
        if (root.getNodeType().equals(RuleNodeConstant.NODE_TYPE_LOGIC))
        {
            List<RuleNode> childNodes = staticNodes.get(root.getId());

            if (childNodes == null)
            {
                throw new RuntimeException("Logic Node Has No Child : " + root);
            }

            List<String> result = new ArrayList<String>();

            for (RuleNode ruleNode : childNodes)
            {
                result.add(recursiveParseSql(ruleNode, staticNodes));
            }

            if (RuleNodeConstant.NODE_TYPE_LOGIC_OPR_SYMBOL_AND.equals(root.getOperateSymbol()))
            {
                return "(" + StringUtils.join(result, " intersect all ") + ")";
            }
            else if (RuleNodeConstant.NODE_TYPE_LOGIC_OPR_SYMBOL_OR.equals(root.getOperateSymbol()))
            {
                return "(" + StringUtils.join(result, " union all ") + ")";
            }
            else if (RuleNodeConstant.NODE_TYPE_LOGIC_OPR_SYMBOL_NOT.equals(root.getOperateSymbol()))
            {
                //不能是EXCEPT all，否则剔除不全
                return "(" + StringUtils.join(result, " except ") + ")";
            }
            else
            {
                throw new RuntimeException("Not Support Logic Operate Symbol : " + root.getOperateSymbol());
            }
        }
        else if (root.getNodeType().equals(RuleNodeConstant.NODE_TYPE_DATA))
        {
            return createDataRuleNodeSql(root);
        }
        else
        {
            throw new RuntimeException("Not Support Node Type : " + root.getNodeType());
        }
    }

    /**
     * @param ruleNode
     * @return
     */
    private String createDataRuleNodeSql(RuleNode ruleNode)
    {
        String operateSymbol = ruleNode.getOperateSymbol();

        INodeParse iNodeParse = nodeParseMap.get(operateSymbol);

        if (iNodeParse != null)
        {
            return iNodeParse.nodeToSql(ruleNode, extraFilterCondition);
        }

        throw new RuntimeException("Not Support Operate Symbol : " + operateSymbol);
    }

    /**
     * @return
     */
    private RuleNode getRootRuleNode()
    {
        for (RuleNode node : this.ruleNodes)
        {
            if (RuleNodeConstant.ROOT_NODE_ID.equals(node.getPid()))
            {
                return node;
            }
        }
        throw new RuntimeException("Can Not Find Root Node : " + this.ruleNodes);
    }

    /**
     * @return
     */
    private Map<String, List<RuleNode>> staticNodes()
    {
        Map<String, List<RuleNode>> result = new HashMap<String, List<RuleNode>>();

        for (RuleNode node : this.ruleNodes)
        {
            List<RuleNode> childNodes = result.get(node.getPid());
            if (childNodes == null)
            {
                childNodes = new ArrayList<RuleNode>();
                result.put(node.getPid(), childNodes);
            }
            childNodes.add(node);
        }
        return result;
    }

}

package com.axon.market.core.rule.impl;

import com.axon.market.common.domain.itag.PropertyDomain;
import com.axon.market.core.rule.ExtraFilterCondition;
import com.axon.market.core.rule.RuleNode;
import com.axon.market.core.rule.RuleNodeConstant;
import com.axon.market.core.service.itag.PropertyService;
import com.axon.market.core.service.itag.TagService;

import java.util.Map;

/**
 * Created by Administrator on 2016/1/26.
 */
public class NumNodeParse implements INodeParse
{
    /**
     * spring注入，查询属性信息
     */
    private PropertyService propertyService;

    private TagService tagService;

    public void setPropertyService(PropertyService propertyService)
    {
        this.propertyService = propertyService;
    }

    public void setTagService(TagService tagService)
    {
        this.tagService = tagService;
    }

    @Override
    public String nodeToSql(RuleNode node, ExtraFilterCondition extraFilterCondition)
    {
        String operateSymbol = node.getOperateSymbol();

        Map<String, String> operateParas = node.getOperateParams();

        String properId = operateParas.get(RuleNodeConstant.NODE_TYPE_DATA_OPR_PARAS_ID);

        PropertyDomain propertyDomain = propertyService.queryPropertyById(Integer.parseInt(properId));

        String compareValue = node.getOperateParams().get("compareValue");

        String symbol = null;

        if (operateSymbol.equalsIgnoreCase("num_gt"))
        {
            symbol = ">";
        }
        else if (operateSymbol.equalsIgnoreCase("num_ge"))
        {
            symbol = ">=";
        }
        else if (operateSymbol.equalsIgnoreCase("num_lt"))
        {
            symbol = "<";
        }
        else if (operateSymbol.equalsIgnoreCase("num_le"))
        {
            symbol = "<=";
        }
        else if (operateSymbol.equalsIgnoreCase("num_eq"))
        {
            symbol = "=";
        }
        else if (operateSymbol.equalsIgnoreCase("num_neq"))
        {
            symbol = "!=";
        }
        else
        {
            throw new RuntimeException("Num Node Not Support Operate Symbol : " + operateSymbol);
        }

        String phone = tagService.queryTagByTableName(propertyDomain.getTableName()).getPhoneColumnName();

        String tableName = propertyDomain.getTableName();

        return "select distinct " + tableName + "." + phone + " as phone from " + tableName + " where " + tableName + "." + propertyDomain.getColumnName() + symbol + compareValue;

    }
}

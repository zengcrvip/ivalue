package com.axon.market.core.rule.impl;

import com.axon.market.common.domain.itag.PropertyDomain;
import com.axon.market.core.rule.ExtraFilterCondition;
import com.axon.market.core.rule.RuleNode;
import com.axon.market.core.rule.RuleNodeConstant;
import com.axon.market.core.service.itag.PropertyService;
import com.axon.market.core.service.itag.TagService;

import java.util.Map;

/**
 * Created by chenyu on 2016/6/29.
 */
public class DateNodeParse implements INodeParse
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

        PropertyDomain metaPropertyDomain = propertyService.queryPropertyById(Integer.parseInt(properId));

        String compareValue = node.getOperateParams().get("compareValue");

        String symbol = null;

        if (operateSymbol.equalsIgnoreCase("date_gt"))
        {
            symbol = ">";
        }
        else if (operateSymbol.equalsIgnoreCase("date_ge"))
        {
            symbol = ">=";
        }
        else if (operateSymbol.equalsIgnoreCase("date_lt"))
        {
            symbol = "<";
        }
        else if (operateSymbol.equalsIgnoreCase("date_le"))
        {
            symbol = "<=";
        }
        else if (operateSymbol.equalsIgnoreCase("date_eq"))
        {
            symbol = "=";
        }
        else if (operateSymbol.equalsIgnoreCase("date_neq"))
        {
            symbol = "!=";
        }
        else
        {
            throw new RuntimeException("Date Node Not Support Operate Symbol : " + operateSymbol);
        }

        String phone = tagService.queryTagByTableName(metaPropertyDomain.getTableName()).getPhoneColumnName();

        String tableName = metaPropertyDomain.getTableName();

        return "select distinct " + tableName + "." + phone + " as phone from " + tableName + " where " + "to_char(" + tableName + "." + metaPropertyDomain.getColumnName() + ",'YYYY-MM-DD')" + symbol + "'" + compareValue + "'";

    }
}

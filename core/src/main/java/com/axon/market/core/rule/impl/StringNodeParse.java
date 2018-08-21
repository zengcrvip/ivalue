package com.axon.market.core.rule.impl;

import com.axon.market.common.domain.itag.PropertyDomain;
import com.axon.market.core.rule.ExtraFilterCondition;
import com.axon.market.core.rule.RuleNode;
import com.axon.market.core.rule.RuleNodeConstant;
import com.axon.market.core.service.itag.PropertyService;
import com.axon.market.core.service.itag.TagService;

/**
 * Created by Administrator on 2016/1/26.
 */
public class StringNodeParse implements INodeParse
{
    //spring注入，查询属性信息
    private PropertyService propertyService;

    private TagService tagService;

    public void setPropertyService(PropertyService productService)
    {
        this.propertyService = productService;
    }

    public void setTagService(TagService tagService)
    {
        this.tagService = tagService;
    }

    @Override
    public String nodeToSql(RuleNode node, ExtraFilterCondition extraFilterCondition)
    {
        String operateSymbol = node.getOperateSymbol();

        String properId = node.getOperateParams().get(RuleNodeConstant.NODE_TYPE_DATA_OPR_PARAS_ID);

        PropertyDomain propertyDomain = propertyService.queryPropertyById(Integer.parseInt(properId));

        String compareValue = node.getOperateParams().get("compareValue");

        String phone = tagService.queryTagByTableName(propertyDomain.getTableName()).getPhoneColumnName();

        String tableName = propertyDomain.getTableName();

        if (operateSymbol.equalsIgnoreCase("string_eq"))
        {
            return "select distinct " + tableName + "." + phone + " as phone from " + propertyDomain.getTableName() + " where " + tableName + "." + propertyDomain.getColumnName() + "='" + compareValue + "'";
        }
        else if (operateSymbol.equalsIgnoreCase("string_neq"))
        {
            return "select distinct " + tableName + "." + phone + " as phone from " + propertyDomain.getTableName() + " where " + tableName + "." + propertyDomain.getColumnName() + "!='" + compareValue + "'";
        }
        else if (operateSymbol.equalsIgnoreCase("string_contain"))
        {
            return "select distinct " + tableName + "." + phone + " as phone from " + propertyDomain.getTableName() + " where " + tableName + "." + propertyDomain.getColumnName() + " like " + ("'%" + compareValue + "%'");
        }
        else if (operateSymbol.equalsIgnoreCase("string_notcontain"))
        {
            return "select distinct " + tableName + "." + phone + " as phone from " + propertyDomain.getTableName() + " where " + propertyDomain.getColumnName() + " not like " + ("'%" + compareValue + "%'");
        }
        else if (operateSymbol.equalsIgnoreCase("string_null"))
        {
            return "select distinct " + tableName + "." + phone + " as phone from " + propertyDomain.getTableName() + " where " + propertyDomain.getColumnName() + " is null ";
        }
        else if (operateSymbol.equalsIgnoreCase("string_notnull"))
        {
            return "select distinct " + tableName + "." + phone + " as phone from " + propertyDomain.getTableName() + " where " + propertyDomain.getColumnName() + " is not null ";
        }

        throw new RuntimeException("String Node Not Support Operate Symbol : " + operateSymbol);
    }
}

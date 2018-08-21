package com.axon.market.core.rule.impl;

import com.axon.market.common.domain.imodel.ModelDomain;
import com.axon.market.core.rule.ExtraFilterCondition;
import com.axon.market.core.rule.RuleNode;
import com.axon.market.core.rule.RuleNodeConstant;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import com.axon.market.core.service.imodel.ModelService;

/**
 * Created by yuanfei on 2017/7/23.
 */
public class ModelNodeParse implements INodeParse
{
    //
    private ModelService modelService;

    private GreenPlumOperateService greenPlumOperateService;

    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }

    public void setGreenPlumOperateService(GreenPlumOperateService greenPlumOperateService)
    {
        this.greenPlumOperateService = greenPlumOperateService;
    }

    @Override
    public String nodeToSql(RuleNode node, ExtraFilterCondition extraFilterCondition)
    {
        String operateSymbol = node.getOperateSymbol();

        String modelId = node.getOperateParams().get(RuleNodeConstant.NODE_TYPE_DATA_OPR_PARAS_ID);

        ModelDomain modelDomain = modelService.queryModelById(Integer.parseInt(modelId));

        String tableName = greenPlumOperateService.getModelDataTableName(modelDomain);

        if (operateSymbol.equalsIgnoreCase("match"))
        {
            return "select distinct " + tableName + ".phone as phone from " + tableName;
        }

        throw new RuntimeException("Import Model not support operateSymbol " + operateSymbol);
    }
}

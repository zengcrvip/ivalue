package com.axon.market.core.service.imodel;

import com.axon.market.common.bean.BaseTableBean;
import com.axon.market.common.domain.imodel.ModelDomain;
import com.axon.market.common.domain.imodel.ModelDownloadSettingDomain;
import com.axon.market.common.domain.itag.PropertyDomain;
import com.axon.market.common.util.JsonUtil;
import com.axon.market.core.rule.RuleNode;
import com.axon.market.core.rule.RuleNodeConstant;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import com.axon.market.core.service.itag.PropertyService;
import com.axon.market.core.service.itag.TagService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by chenyu on 2017/2/8.
 */
@Service("modelDataOperateService")
public class ModelDataOperateService
{
    private static final Logger LOG = Logger.getLogger(ModelDataOperateService.class.getName());

    @Autowired
    @Qualifier("greenPlumOperateService")
    private GreenPlumOperateService greenPlumOperateService;

    @Autowired
    @Qualifier("modelDownloadService")
    private ModelDownloadService modelDownloadService;

    @Autowired
    @Qualifier("propertyService")
    private PropertyService propertyService;

    @Autowired
    @Qualifier("tagService")
    private TagService tagService;

    private BaseTableBean baseTableBean = BaseTableBean.getInstance();

    /**
     * 创建下载客户群查询sql
     *
     * @param modelDomain
     * @return
     */
    public String[] createQuerySegmentSql(ModelDomain modelDomain, Integer areaCode, Integer userId)
    {
        // 客户群表名称
        String mainTableName = greenPlumOperateService.getModelDataTableName(modelDomain);
        // 客户群表名称加手机号码字段
        String mainTablePhoneColumn = mainTableName + ".phone";
        boolean isProvincial = areaCode == 99999;

        StringBuffer sql = new StringBuffer();
        String[] result = new String[2];

        ModelDownloadSettingDomain modelDownloadSettingDomain = modelDownloadService.queryMyModelDownloadSettingByModelId(modelDomain.getId(),userId);

        if (modelDownloadSettingDomain != null && StringUtils.isNotEmpty(modelDownloadSettingDomain.getMetaPropertyIds()))
        {
            String metaPropertyIds = modelDownloadSettingDomain.getMetaPropertyIds();
            // 下载客户群字段中文名称
            List<String> columnNames = new LinkedList<String>()
            {
                {
                    add("用户手机");
                }
            };
            // 下载客户群相关属性字段
            List<String> columnInfo = new LinkedList<String>();
            // 下载客户群涉及字段对应的所有表map（需要left join，key为表名称，value为表对应的手机号列名）
            Map<String, String> joinTableNameAndPhoneColumn = new HashMap<String, String>();
            // 地市字段条件list
            List<String> conditions = new ArrayList<String>();
            // 加入客户群筛选条件，排除left join出来的一些重复数据
            List<String> ruleConditions = new ArrayList<String>();

            // 先加入主表地市条件
            if (!isProvincial)
            {
                conditions.add(baseTableBean.getTableName() + "." + baseTableBean.getAreaColumn() + " = '" + areaCodeTransform(areaCode) + "'");
            }

            // 数据库查询属性字段信息
            List<Map<String, String>> metaList = propertyService.queryMetaPropertiesByIds(metaPropertyIds);
            // 将属性list转化为map（key为属性id，value为属性详细字段信息），便于后面使用
            Map<String, Map<String, String>> idAndProperty = new HashMap<String, Map<String, String>>();
            for (Map<String, String> metaProperty : metaList)
            {
                idAndProperty.put(String.valueOf(metaProperty.get("id")), metaProperty);
            }

            // 根据设置的下载属性，遍历加入相关list中，便于后面sql拼接
            String[] metaIds = metaPropertyIds.split(",");
            for (String metaId : metaIds)
            {
                Map<String, String> property = idAndProperty.get(metaId);
                String tableName = property.get("tableName");
                joinTableNameAndPhoneColumn.put(tableName, property.get("columnInfo"));
                // 转化字段类型为varchar，null为''
                columnInfo.add("coalesce(cast(" + tableName + "." + property.get("columnName") + " as varchar) || '\\t'  , '')");
                columnNames.add(String.valueOf(property.get("name")));
                if (!isProvincial)
                {
                    String areaColumnNames = property.get("areaColumnName");
                    if (StringUtils.isNotEmpty(areaColumnNames))
                    {
                        String[] areaColumnNameArray = areaColumnNames.split(",");
                        List<String> condition = new ArrayList<String>();
                        for (String areaColumnName : areaColumnNameArray)
                        {
                            condition.add("cast(" + tableName + "." + areaColumnName + " as varchar) = " + "'" + areaCodeTransform(areaCode) + "'");
                        }
                        conditions.add(StringUtils.join(condition, " or "));
                    }
                }
            }

           /* // 生成规则创建条件
            if (StringUtils.isNotEmpty(modelDomain.getRule()))
            {
                String[] ruleCondition = createConditionsByRule(modelDomain.getRule());
                if (ruleCondition != null)
                {
                    String[] tableNameAndPhones = ruleCondition[0].split(",");
                    for (String tableNameAndPhone : tableNameAndPhones)
                    {
                        String tableName = tableNameAndPhone.substring(0, tableNameAndPhone.lastIndexOf("."));
                        if (!joinTableNameAndPhoneColumn.containsKey(tableName))
                        {
                            joinTableNameAndPhoneColumn.put(tableName, tableNameAndPhone);
                        }
                    }
                    ruleConditions.add(ruleCondition[1]);
                }
            }*/
            result[0] = createQuerySegmentSql(mainTableName, mainTablePhoneColumn, columnInfo, joinTableNameAndPhoneColumn, conditions, ruleConditions);
            result[1] = StringUtils.join(columnNames, ",") + "\r\n";
        }
        else
        {
            sql.append(createQuerySegmentSql(isProvincial, mainTableName, mainTablePhoneColumn, areaCodeTransform(areaCode)));
            result[0] = sql.toString();
            result[1] = "用户手机" + "\r\n";
        }

        return result;
    }

    /**
     *
     * @param areaCode
     * @return
     */
    private String areaCodeTransform (Integer areaCode)
    {
        if (null != areaCode && String.valueOf(areaCode).length() < 4)
        {
            String newAreaCode = "0000" + String.valueOf(areaCode);
            return newAreaCode.substring(newAreaCode.length() - 4);
        }
        return null;
    }

    private String[] createConditionsByRule(String rule)
    {
        try
        {
            List<RuleNode> nodes = JsonUtil.stringToObject(rule, new TypeReference<List<RuleNode>>() {
            });

            String[] result = recursiveParseCondition(getRootRuleNode(nodes), staticNodes(nodes));
            return result;
        }
        catch (IOException e)
        {
            LOG.error("", e);
        }
        return null;
    }

    /**
     * @return
     */
    private RuleNode getRootRuleNode(List<RuleNode> nodes)
    {
        for (RuleNode node : nodes)
        {
            if (RuleNodeConstant.ROOT_NODE_ID.equals(node.getPid()))
            {
                return node;
            }
        }
        return null;
    }

    /**
     * @return
     */
    private Map<String, List<RuleNode>> staticNodes(List<RuleNode> nodes)
    {
        Map<String, List<RuleNode>> result = new HashMap<String, List<RuleNode>>();

        for (RuleNode node : nodes)
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

    /**
     * 无约束客户群查询sql
     *
     * @param isProvincial
     * @param mainTableName
     * @param mainTablePhoneColumn
     * @param areaCode
     * @return
     */
    private String createQuerySegmentSql(boolean isProvincial, String mainTableName, String mainTablePhoneColumn, String areaCode)
    {
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct(").append(mainTablePhoneColumn).append(") as phone, '' as value from ").append(mainTableName);
        //如果未配置主表则表示查询全部
        if (StringUtils.isNotEmpty(baseTableBean.getTableName()))
        {
            sql.append(" left join ").append(baseTableBean.getTableName()).append(" on ")
                .append("cast(" + baseTableBean.getTableName()).append(".").append(baseTableBean.getPhoneColumn() + " as varchar)").append("=").append(mainTablePhoneColumn);
        }

        if (!isProvincial)
        {
            sql.append(" where ").append(baseTableBean.getTableName()).append(".").append(baseTableBean.getAreaColumn()).append(" = '").append(areaCode).append("'");
        }
        return sql.toString();
    }

    /**
     *
     * @param mainTableName
     * @param mainTablePhoneColumn
     * @param columnInfo
     * @param joinTableNameAndPhoneColumn
     * @param conditions
     * @param ruleConditions
     * @return
     */
    private String createQuerySegmentSql(String mainTableName, String mainTablePhoneColumn, List<String> columnInfo,
                                         Map<String, String> joinTableNameAndPhoneColumn, List<String> conditions, List<String> ruleConditions)
    {
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct ").append(mainTablePhoneColumn).append(" as phone,");
        sql.append(StringUtils.join(columnInfo, " || ',' || ")).append(" as value");
        sql.append(" from ").append(mainTableName);
        if (StringUtils.isNotEmpty(baseTableBean.getTableName()))
        {
            sql.append(" left join ").append(baseTableBean.getTableName()).append(" on ")
                    .append("cast(" + baseTableBean.getTableName()).append(".").append(baseTableBean.getPhoneColumn() + " as varchar)").append("=").append(mainTablePhoneColumn);
        }
        for (Map.Entry<String, String> entry : joinTableNameAndPhoneColumn.entrySet())
        {
            if (!baseTableBean.getTableName().equals(entry.getKey()))
            {
                sql.append(" left join ").append(entry.getKey()).append(" on ").append(entry.getValue())
                        .append(" = ").append(mainTablePhoneColumn);
            }
        }
        if (CollectionUtils.isNotEmpty(conditions))
        {
            sql.append(" where (").append(StringUtils.join(conditions, " or ")).append(")");
            if (CollectionUtils.isNotEmpty(ruleConditions))
            {
                sql.append(" and ").append(StringUtils.join(ruleConditions, " "));
            }
        }
        else if (CollectionUtils.isNotEmpty(ruleConditions))
        {
            sql.append(" where ").append(StringUtils.join(ruleConditions, " "));
        }
        return sql.toString();
    }

    /**
     * @param root
     * @param staticNodes
     * @return
     */
    private String[] recursiveParseCondition(RuleNode root, final Map<String, List<RuleNode>> staticNodes)
    {
        String[] result = new String[2];
        if (root.getNodeType().equals(RuleNodeConstant.NODE_TYPE_LOGIC))
        {
            List<RuleNode> childNodes = staticNodes.get(root.getId());

            if (childNodes == null)
            {
                throw new RuntimeException("logic node has no child:" + root);
            }

            List<String[]> list = new ArrayList<String[]>();
            for (RuleNode ruleNode : childNodes)
            {
                String[] str = recursiveParseCondition(ruleNode, staticNodes);
                if (str != null)
                {
                    list.add(str);
                }
            }

            if (RuleNodeConstant.NODE_TYPE_LOGIC_OPR_SYMBOL_AND.equals(root.getOperateSymbol()))
            {
                for (String[] str : list)
                {
                    if (StringUtils.isEmpty(result[0]))
                    {
                        result[0] = str[0];
                        result[1] = str[1];
                    }
                    else
                    {
                        result[0] = result[0] + "," + str[0];
                        result[1] = result[1] + " and " + str[1];
                    }
                }
            }
            else if (RuleNodeConstant.NODE_TYPE_LOGIC_OPR_SYMBOL_OR.equals(root.getOperateSymbol()))
            {
                for (String[] str : list)
                {
                    if (StringUtils.isEmpty(result[0]))
                    {
                        result[0] = str[0];
                        result[1] = str[1];
                    }
                    else
                    {
                        result[0] = result[0] + "," + str[0];
                        result[1] = result[1] + " or " + str[1];
                    }
                }
            }
            else if (RuleNodeConstant.NODE_TYPE_LOGIC_OPR_SYMBOL_NOT.equals(root.getOperateSymbol()))
            {
                return null;
            }
        }
        else if (root.getNodeType().equals(RuleNodeConstant.NODE_TYPE_DATA))
        {
            result = createRuleCondition(root);
        }
        return result;
    }

    private String[] createRuleCondition(RuleNode node)
    {
        String[] result = null;
        if (node.getNodeType().equals(RuleNodeConstant.NODE_TYPE_DATA))
        {
            result = operateDifferentTypeCondition(node);
        }
        return result;
    }

    private String[] operateDifferentTypeCondition(RuleNode node)
    {
        String[] result = new String[2];

        String operateSymbol = node.getOperateSymbol();
        String properId = node.getOperateParams().get(RuleNodeConstant.NODE_TYPE_DATA_OPR_PARAS_ID);
        PropertyDomain metaPropertyDomain = propertyService.queryPropertyById(Integer.parseInt(properId));
        String compareValue = node.getOperateParams().get("compareValue");

        String tableName = metaPropertyDomain.getTableName();
        String phone = tagService.queryTagByTableName(metaPropertyDomain.getTableName()).getPhoneColumnName();
        String condition = null;
        switch (operateSymbol)
        {
            case "num_gt":
            {
                condition = tableName + "." + metaPropertyDomain.getColumnName() + ">" + compareValue;
                break;
            }
            case "num_ge":
            {
                condition = tableName + "." + metaPropertyDomain.getColumnName() + ">=" + compareValue;
                break;
            }
            case "num_lt":
            {
                condition = tableName + "." + metaPropertyDomain.getColumnName() + "<" + compareValue;
                break;
            }
            case "num_le":
            {
                condition = tableName + "." + metaPropertyDomain.getColumnName() + "<=" + compareValue;
                break;
            }
            case "num_eq":
            {
                condition = tableName + "." + metaPropertyDomain.getColumnName() + "=" + compareValue;
                break;
            }
            case "num_neq":
            {
                condition = tableName + "." + metaPropertyDomain.getColumnName() + "!=" + compareValue;
                break;
            }

            case "string_eq":
            {
                condition = tableName + "." + metaPropertyDomain.getColumnName() + "='" + compareValue + "'";
                break;
            }
            case "string_neq":
            {
                condition = tableName + "." + metaPropertyDomain.getColumnName() + "!='" + compareValue + "'";
                break;
            }
            case "string_contain":
            {
                condition = tableName + "." + metaPropertyDomain.getColumnName() + " like " + ("'%" + compareValue + "%'");
                break;
            }
            case "string_notcontain":
            {
                condition = tableName + "." + metaPropertyDomain.getColumnName() + " not like " + ("'%" + compareValue + "%'");
                break;
            }
            case "string_null":
            {
                condition = tableName + "." + metaPropertyDomain.getColumnName() + " is null ";
                break;
            }
            case "string_notnull":
            {
                condition = tableName + "." + metaPropertyDomain.getColumnName() + " is not null ";
                break;
            }

            case "date_gt":
            {
                condition = "to_char(" + tableName + "." + metaPropertyDomain.getColumnName() + ",'YYYY-MM-DD')" + ">" + "'" + compareValue + "'";
                break;
            }
            case "date_ge":
            {
                condition = "to_char(" + tableName + "." + metaPropertyDomain.getColumnName() + ",'YYYY-MM-DD')" + ">=" + "'" + compareValue + "'";
                break;
            }
            case "date_lt":
            {
                condition = "to_char(" + tableName + "." + metaPropertyDomain.getColumnName() + ",'YYYY-MM-DD')" + "<" + "'" + compareValue + "'";
                break;
            }
            case "date_le":
            {
                condition = "to_char(" + tableName + "." + metaPropertyDomain.getColumnName() + ",'YYYY-MM-DD')" + "<=" + "'" + compareValue + "'";
                break;
            }
            case "date_eq":
            {
                condition = "to_char(" + tableName + "." + metaPropertyDomain.getColumnName() + ",'YYYY-MM-DD')" + "=" + "'" + compareValue + "'";
                break;
            }
            case "date_neq":
            {
                condition = "to_char(" + tableName + "." + metaPropertyDomain.getColumnName() + ",'YYYY-MM-DD')" + "!=" + "'" + compareValue + "'";
                break;
            }
        }
        result[0] = tableName + "." + phone;
        result[1] = condition;
        return result;
    }
}

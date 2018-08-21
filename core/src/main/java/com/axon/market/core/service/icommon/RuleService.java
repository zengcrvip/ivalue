package com.axon.market.core.service.icommon;

import com.axon.market.common.util.JsonUtil;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.core.rule.IRuleParse;
import com.axon.market.core.rule.RuleNode;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by chenyu on 2017/2/17.
 */
@Service("ruleService")
public class RuleService
{
    private static final Logger LOG = Logger.getLogger(RuleService.class.getName());

    @Autowired
    @Qualifier("ruleParseImpl")
    private IRuleParse ruleParser;

    @Autowired
    @Qualifier("greenPlumOperateService")
    private GreenPlumOperateService greenPlumOperateService;

    /**
     * @return
     */
    public static RuleService getInstance()
    {
        return (RuleService) SpringUtil.getSingletonBean("ruleService");
    }

    /**
     * @param jsonRule
     * @return
     */
    public String ruleNodeToSql(String jsonRule)
    {
        try
        {
            return ruleParser.parseRuleToSql(jsonRule, null);
        }
        catch (Exception e)
        {
            LOG.error("Rule Node To Sql error : " + jsonRule, e);
            return null;
        }
    }

    /**
     * @param ruleNodeList
     * @return
     */
    public String ruleNodeToSql(List<RuleNode> ruleNodeList)
    {
        try
        {
            return ruleParser.parseRuleToSql(ruleNodeList, null);
        }
        catch (Exception e)
        {
            LOG.error("Rule Node To Sql error : " + ruleNodeList, e);
            return null;
        }
    }

    /**
     * @param ruleNodeList
     * @return
     */
    public Integer queryMatchRuleUserCounts(List<RuleNode> ruleNodeList)
    {
        String sql = ruleNodeToSql(ruleNodeList);

        if (sql != null)
        {
            try
            {
                LOG.info("RuleNode are " + ruleNodeList);
                if (sql.startsWith("(") && sql.endsWith(")"))
                {
                    sql = "select count(1) from " + sql + " a0";
                }
                else
                {
                    sql = "select count(1) from " + "(" + sql + ")" + " a0";
                }
                LOG.info("sql is " + sql);
                return greenPlumOperateService.queryRecordCount(sql);
            }
            catch (Exception e)
            {
                LOG.error("Query Match Rule User Counts error ", e);
            }
        }
        return null;
    }

    /**
     * @param rules
     * @return
     */
    public Integer queryMatchRuleUserCounts(String rules)
    {
        String sql = ruleNodeToSql(rules);

        if (sql != null)
        {
            try
            {
                LOG.info("RuleNode are " + rules);
                if (sql.startsWith("(") && sql.endsWith(")"))
                {
                    sql = "select count(distinct phone) from " + sql + " a0";
                }
                else
                {
                    sql = "select count(distinct phone) from " + "(" + sql + ")" + " a0";
                }
                LOG.info("sql is " + sql);
                return greenPlumOperateService.queryRecordCount(sql);
            }
            catch (Exception e)
            {
                LOG.error("Query Match Rule User Counts error : ", e);
            }
        }
        return null;
    }

    /**
     * 查询从掌柜平台创建的模型用户人数
     * @param rules
     * @return
     */
    public Integer queryMatchRuleUserCountByKeeper(String rules)
    {
        try
        {
           String sql = "";
            List<Map<String,String>> ruleList = JsonUtil.stringToObject(rules, List.class);
            for (Map<String,String> ruleMap : ruleList)
            {
                Set<String> fields = ruleMap.keySet();
                sql += "select count(distinct phone) from uapp.lab_mobile_tag where 1 = 1 ";
                for (String field : fields)
                {
                    String values = ruleMap.get(field);
                    if (StringUtils.isEmpty(values))
                    {
                        continue;
                    }
                    sql += " and ( ";
                    String[] valueArray = values.split(",");

                    for (String value : valueArray)
                    {
                        sql += field+"='"+value +"' or ";
                    }
                    sql = sql.substring(0,sql.length()-4) + ")";
                }
            }

            return greenPlumOperateService.queryRecordCount(sql);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}

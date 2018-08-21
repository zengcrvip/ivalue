package com.axon.market.core.rule;

import java.util.Map;

/**
 * Created by yangyang on 2016/1/6.
 *
 * 与multiRule.js中对应，不要修改
 *
 */
public class RuleNode
{
    /**
     * it is unique
     */
    private String id;

    /**
     * “root" is the root
     */
    private String pid;

    /**
     * logic、data
     */
    private String nodeType;

    /**
     * logic:[
     * and
     * or
     * not
     * ]
     *
     * tag:[
     * match
     * ]
     *
     * property:[
     * num_gt
     * num_ge
     * num_lt
     * num_le
     * num_eq
     * num_neq
     * string_eq
     * string_neq
     * string_null
     * string_notnull
     * string_contain
     * string_notcontain
     * match
     * ]
     */
    private String operateSymbol;

    /**
     *  和operateSymbol相对应:<br>
     *  logic:null
     *  tag:{
     *
     *  }
     *  property：[
     *  num_gt,num_ge,num_lt,num_le,num_eq,num_neq:{
     *
     *  }
     *
     *  string_eq,string_neq,string_null,string_notnull,string_contain,string_notcontain:{
     *
     *  }
     *  ]
     */
    private Map<String,String> operateParams;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getPid()
    {
        return pid;
    }

    public void setPid(String pid)
    {
        this.pid = pid;
    }

    public String getNodeType()
    {
        return nodeType;
    }

    public void setNodeType(String nodeType)
    {
        this.nodeType = nodeType;
    }

    public String getOperateSymbol()
    {
        return operateSymbol;
    }

    public void setOperateSymbol(String operateSymbol)
    {
        this.operateSymbol = operateSymbol;
    }

    public Map<String, String> getOperateParams()
    {
        return operateParams;
    }

    public void setOperateParams(Map<String, String> operateParams)
    {
        this.operateParams = operateParams;
    }

    @Override
    public String toString()
    {
        return "RuleNode{" +
                "id='" + id + '\'' +
                ", pid='" + pid + '\'' +
                ", nodeType='" + nodeType + '\'' +
                ", operateSymbol='" + operateSymbol + '\'' +
                ", operateParams=" + operateParams +
                '}';
    }
}

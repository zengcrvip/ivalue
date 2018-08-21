package com.axon.market.common.domain.ikeeper;

import com.axon.market.common.domain.icommon.OrgDomain;

import java.util.List;
import java.util.Map;

/**
 * Created by yuanfei on 2017/8/20.
 */
public class TaskShowDomain
{
    private Integer taskId;

    private String taskName;

    private Integer typeId;

    private String effDate;

    private String expDate;

    private String comments;

    private String filedId;

    private String importFileDesc;

    private List<OrgDomain> businessOrgs;

    private List<Map<String,Object>> rules;

    private List<Map<String,Object>> welfares;

    private List<TaskChannelDomain> channels;

    public Integer getTaskId()
    {
        return taskId;
    }

    public void setTaskId(Integer taskId)
    {
        this.taskId = taskId;
    }

    public Integer getTypeId()
    {
        return typeId;
    }

    public void setTypeId(Integer typeId)
    {
        this.typeId = typeId;
    }

    public String getTaskName()
    {
        return taskName;
    }

    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }

    public String getEffDate()
    {
        return effDate;
    }

    public void setEffDate(String effDate)
    {
        this.effDate = effDate;
    }

    public String getExpDate()
    {
        return expDate;
    }

    public void setExpDate(String expDate)
    {
        this.expDate = expDate;
    }

    public String getcomments()
    {
        return comments;
    }

    public void setcomments(String comments)
    {
        this.comments = comments;
    }

    public String getFiledId()
    {
        return filedId;
    }

    public void setFiledId(String filedId)
    {
        this.filedId = filedId;
    }

    public List<OrgDomain> getBusinessOrgs()
    {
        return businessOrgs;
    }

    public void setBusinessOrgs(List<OrgDomain> businessOrgs)
    {
        this.businessOrgs = businessOrgs;
    }

    public List<Map<String, Object>> getRules()
    {
        return rules;
    }

    public void setRules(List<Map<String, Object>> rules)
    {
        this.rules = rules;
    }

    public List<Map<String, Object>> getWelfares()
    {
        return welfares;
    }

    public void setWelfares(List<Map<String, Object>> welfares)
    {
        this.welfares = welfares;
    }

    public List<TaskChannelDomain> getChannels()
    {
        return channels;
    }

    public void setChannels(List<TaskChannelDomain> channels)
    {
        this.channels = channels;
    }

    public String getImportFileDesc()
    {
        return importFileDesc;
    }

    public void setImportFileDesc(String importFileDesc)
    {
        this.importFileDesc = importFileDesc;
    }
}

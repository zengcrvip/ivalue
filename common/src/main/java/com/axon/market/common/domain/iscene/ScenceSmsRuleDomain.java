package com.axon.market.common.domain.iscene;

/**
 * 场景规则实体对象
 * Created by zengcr on 2016/11/9.
 */
public class ScenceSmsRuleDomain
{
    //场景业务规则ID
    private Integer id;
    //场景业务规则名称
    private String scenceName;
    //场景业务规则类型
    private Integer scenceType;
    //场景业务规则类型名称
    private String scenceTypeName;
    //渠道名称
    private String channelName;
    //发送次数
    private Integer accessNum;
    //开始时间
    private String beginTime;
    //结束时间
    private String endTime;
    //是否匹配客户端
    private Integer matchClient;
    //客户端ID，逗号分隔
    private String clientIds;
    //客户端名称，逗号分隔
    private String clientNames;
    //是否匹配关键字
    private Integer matchKeywords;
    //关键字
    private String keywords;
    //是否匹配位置
    private Integer matchPosition;
    //位置
    private String positions;
    //是否匹配网站
    private Integer matchSite;
    //网站
    private String webSits;
    //是否匹配终端
    private Integer matchTerminal;
    //终端ID
    private String terminals;
    //终端名称
    private String terminalNames;
    //状态
    private Integer status;
    //年月日时分秒16位字符串
    private String serialNum;
    //操作人
    private String editor;

    public String getSerialNum()
    {
        return serialNum;
    }

    public void setSerialNum(String serialNum)
    {
        this.serialNum = serialNum;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getScenceName()
    {
        return scenceName;
    }

    public void setScenceName(String scenceName)
    {
        this.scenceName = scenceName;
    }

    public Integer getScenceType()
    {
        return scenceType;
    }

    public void setScenceType(Integer scenceType)
    {
        this.scenceType = scenceType;
    }

    public String getChannelName()
    {
        return channelName;
    }

    public void setChannelName(String channelName)
    {
        this.channelName = channelName;
    }

    public Integer getMatchClient()
    {
        return matchClient;
    }

    public void setMatchClient(Integer matchClient)
    {
        this.matchClient = matchClient;
    }

    public String getClientIds()
    {
        return clientIds;
    }

    public void setClientIds(String clientIds)
    {
        this.clientIds = clientIds;
    }

    public Integer getMatchPosition()
    {
        return matchPosition;
    }

    public void setMatchPosition(Integer matchPosition)
    {
        this.matchPosition = matchPosition;
    }

    public String getPositions()
    {
        return positions;
    }

    public void setPositions(String positions)
    {
        this.positions = positions;
    }

    public Integer getMatchSite()
    {
        return matchSite;
    }

    public void setMatchSite(Integer matchSite)
    {
        this.matchSite = matchSite;
    }

    public String getWebSits()
    {
        return webSits;
    }

    public void setWebSits(String webSits)
    {
        this.webSits = webSits;
    }

    public Integer getMatchTerminal()
    {
        return matchTerminal;
    }

    public void setMatchTerminal(Integer matchTerminal)
    {
        this.matchTerminal = matchTerminal;
    }

    public String getTerminals()
    {
        return terminals;
    }

    public void setTerminals(String terminals)
    {
        this.terminals = terminals;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getScenceTypeName()
    {
        return scenceTypeName;
    }

    public void setScenceTypeName(String scenceTypeName)
    {
        this.scenceTypeName = scenceTypeName;
    }

    public Integer getMatchKeywords()
    {
        return matchKeywords;
    }

    public void setMatchKeywords(Integer matchKeywords)
    {
        this.matchKeywords = matchKeywords;
    }

    public String getKeywords()
    {
        return keywords;
    }

    public void setKeywords(String keywords)
    {
        this.keywords = keywords;
    }

    public String getClientNames()
    {
        return clientNames;
    }

    public void setClientNames(String clientNames)
    {
        this.clientNames = clientNames;
    }

    public String getTerminalNames()
    {
        return terminalNames;
    }

    public void setTerminalNames(String terminalNames)
    {
        this.terminalNames = terminalNames;
    }

    public Integer getAccessNum()
    {
        return accessNum;
    }

    public void setAccessNum(Integer accessNum)
    {
        this.accessNum = accessNum;
    }

    public String getBeginTime()
    {
        return beginTime;
    }

    public void setBeginTime(String beginTime)
    {
        this.beginTime = beginTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public String getEditor()
    {
        return editor;
    }

    public void setEditor(String editor)
    {
        this.editor = editor;
    }
}

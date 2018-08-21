package com.axon.market.common.bean;

import com.axon.market.common.util.SpringUtil;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

/**
 * 系统配置 bean
 * Created by chenyu on 2016/6/6.
 */
public class SystemConfigBean
{
    private String monitorPath;

    private String localFilePath;

    private String dmcFilePath;

    private String channelFileLocalPath;

    private String dixiaoFileLocalPath;

    private int dixiaoBackDay;

    private boolean isForceRefreshRuleModel;

    private String forceRefreshModelTime;

    private String sendSmsUrl;

    private String sendSmsPwd;

    private String jumpLinkUrl;

    private String shortLinkPrefix;

    private String positionTaskUrl;

    private String sendSmsWebServiceURL;

    private String sendSmsWebServicePWD;

    //常驻，流动拜访，指定用户短信发送接口
    private String yunSmsServiceURL;

    //系统上线地区
    private String province;

    //kafka相关配置
    //kafka服务配置
    private String kafkaServers;
    //kafka组ID
    private String kafkaGroupId;
    private String kafkaLogGroupId;

    //精细化任务topic
    private String kafkaFineTopic;

    //同一家每天限制短信发送量
    private Integer baseSmsLimit;

    @PostConstruct
    public void initMonitorPath() throws IOException
    {
        File file = new File(monitorPath);
        if (!file.exists())
        {
            file = new File("../monitor");
            file.mkdir();
            monitorPath = file.getCanonicalPath() + File.separator;
        }
    }

    @PostConstruct
    public void initLocalFilePath() throws IOException
    {
        File file = new File(localFilePath);
        if (!file.exists())
        {
            file = new File("../upload");
            file.mkdir();
            localFilePath = file.getCanonicalPath() + File.separator;
        }
    }

    @PostConstruct
    public void initChannelFileLocalPathPath() throws IOException
    {
        File file = new File(channelFileLocalPath);
        if (!file.exists())
        {
            file = new File("../channel");
            file.mkdir();
            channelFileLocalPath = file.getCanonicalPath() + File.separator;
        }
    }

    @PostConstruct
    public void initDixiaoFileLocalPathPath() throws IOException
    {
        File file = new File(dixiaoFileLocalPath);
        if (!file.exists())
        {
            file = new File("../dixiao");
            file.mkdir();
            dixiaoFileLocalPath = file.getCanonicalPath() + File.separator;
        }
    }

    @PostConstruct
    public void initDmcFilePath() throws IOException
    {
        File file = new File(dmcFilePath);
        if (!file.exists())
        {
            file = new File("../dmc");
            file.mkdir();
            dmcFilePath = file.getCanonicalPath() + File.separator;
        }
    }

    public static SystemConfigBean getInstance()
    {
        return (SystemConfigBean) SpringUtil.getSingletonBean("systemConfigBean");
    }

    public String getMonitorPath()
    {
        return monitorPath;
    }

    public void setMonitorPath(String monitorPath)
    {
        this.monitorPath = monitorPath;
    }

    public String getLocalFilePath()
    {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath)
    {
        this.localFilePath = localFilePath;
    }

    public String getDmcFilePath()
    {
        return dmcFilePath;
    }

    public void setDmcFilePath(String dmcFilePath)
    {
        this.dmcFilePath = dmcFilePath;
    }

    public String getChannelFileLocalPath()
    {
        return channelFileLocalPath;
    }

    public void setChannelFileLocalPath(String channelFileLocalPath)
    {
        this.channelFileLocalPath = channelFileLocalPath;
    }

    public boolean isForceRefreshRuleModel()
    {
        return isForceRefreshRuleModel;
    }

    public void setIsForceRefreshRuleModel(boolean isForceRefreshRuleModel)
    {
        this.isForceRefreshRuleModel = isForceRefreshRuleModel;
    }

    public String getForceRefreshModelTime()
    {
        return forceRefreshModelTime;
    }

    public void setForceRefreshModelTime(String forceRefreshModelTime)
    {
        this.forceRefreshModelTime = forceRefreshModelTime;
    }

    public String getSendSmsUrl()
    {
        return sendSmsUrl;
    }

    public void setSendSmsUrl(String sendSmsUrl)
    {
        this.sendSmsUrl = sendSmsUrl;
    }

    public String getSendSmsPwd()
    {
        return sendSmsPwd;
    }

    public void setSendSmsPwd(String sendSmsPwd)
    {
        this.sendSmsPwd = sendSmsPwd;
    }

    public String getJumpLinkUrl()
    {
        return jumpLinkUrl;
    }

    public void setJumpLinkUrl(String jumpLinkUrl)
    {
        this.jumpLinkUrl = jumpLinkUrl;
    }

    public String getShortLinkPrefix()
    {
        return shortLinkPrefix;
    }

    public void setShortLinkPrefix(String shortLinkPrefix)
    {
        this.shortLinkPrefix = shortLinkPrefix;
    }

    public String getPositionTaskUrl()
    {
        return positionTaskUrl;
    }

    public void setPositionTaskUrl(String positionTaskUrl)
    {
        this.positionTaskUrl = positionTaskUrl;
    }

    public String getSendSmsWebServiceURL()
    {
        return sendSmsWebServiceURL;
    }

    public void setSendSmsWebServiceURL(String sendSmsWebServiceURL)
    {
        this.sendSmsWebServiceURL = sendSmsWebServiceURL;
    }

    public String getSendSmsWebServicePWD()
    {
        return sendSmsWebServicePWD;
    }

    public void setSendSmsWebServicePWD(String sendSmsWebServicePWD)
    {
        this.sendSmsWebServicePWD = sendSmsWebServicePWD;
    }

    public String getYunSmsServiceURL()
    {
        return yunSmsServiceURL;
    }

    public void setYunSmsServiceURL(String yunSmsServiceURL)
    {
        this.yunSmsServiceURL = yunSmsServiceURL;
    }

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public String getKafkaServers()
    {
        return kafkaServers;
    }

    public void setKafkaServers(String kafkaServers)
    {
        this.kafkaServers = kafkaServers;
    }

    public String getKafkaGroupId()
    {
        return kafkaGroupId;
    }

    public void setKafkaGroupId(String kafkaGroupId)
    {
        this.kafkaGroupId = kafkaGroupId;
    }

    public String getKafkaFineTopic()
    {
        return kafkaFineTopic;
    }

    public void setKafkaFineTopic(String kafkaFineTopic)
    {
        this.kafkaFineTopic = kafkaFineTopic;
    }

    public Integer getBaseSmsLimit()
    {
        return baseSmsLimit;
    }

    public void setBaseSmsLimit(Integer baseSmsLimit)
    {
        this.baseSmsLimit = baseSmsLimit;
    }

    public String getDixiaoFileLocalPath() {
        return dixiaoFileLocalPath;
    }

    public void setDixiaoFileLocalPath(String dixiaoFileLocalPath) {
        this.dixiaoFileLocalPath = dixiaoFileLocalPath;
    }

    public int getDixiaoBackDay() {
        return dixiaoBackDay;
    }

    public void setDixiaoBackDay(int dixiaoBackDay) {
        this.dixiaoBackDay = dixiaoBackDay;
    }

    public String getKafkaLogGroupId() {
        return kafkaLogGroupId;
    }

    public void setKafkaLogGroupId(String kafkaLogGroupId) {
        this.kafkaLogGroupId = kafkaLogGroupId;
    }
}

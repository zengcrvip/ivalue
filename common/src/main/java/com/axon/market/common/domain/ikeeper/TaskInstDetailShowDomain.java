package com.axon.market.common.domain.ikeeper;

import java.util.List;
import java.util.Map;

/**
 * 是keeper_task_inst和keeper_task_inst_task的内容合并，主要用户app端显示任务详情信息
 *
 * Created by yuanfei on 2017/8/17.
 */
public class TaskInstDetailShowDomain
{
    private Integer detailId;

    private String taskName;

    /**
     * 任务实例生效时间
     */
    private String expDate;

    /**
     * 任务实例失效时间
     */
    private String effDate;

    /**
     * 维系客户的名称
     */
    private String customerName;

    /**
     * 维系客户的号码
     */
    private String telephone;

    /**
     * 匹配keeper.keeper_user_maintain 的id
     */
    private Integer customerId;

    /**
     * 维系短信模板
     */
    private String smsContent;

    /**
     * 电话营销术语模板
     */
    private String callContent;

    /**
     * 福利产品
     */
    private List<Map<String,Object>> welfare;

    /**
     * 维系动作短信发送结果 0：未执行，1：已执行
     */
    private Integer smsResult;

    /**
     * 维系动作电话结果： 0：未执行外呼或失败，1：呼叫完成(超过外呼限制后自动改成完成状态)
     */
    private Integer callResult;

    public Integer getDetailId()
    {
        return detailId;
    }

    public void setDetailId(Integer detailId)
    {
        this.detailId = detailId;
    }

    public String getTaskName()
    {
        return taskName;
    }

    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }

    public String getExpDate()
    {
        return expDate;
    }

    public void setExpDate(String expDate)
    {
        this.expDate = expDate;
    }

    public String getEffDate()
    {
        return effDate;
    }

    public void setEffDate(String effDate)
    {
        this.effDate = effDate;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    public String getTelephone()
    {
        return telephone;
    }

    public void setTelephone(String telephone)
    {
        this.telephone = telephone;
    }

    public Integer getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Integer customerId)
    {
        this.customerId = customerId;
    }

    public String getSmsContent()
    {
        return smsContent;
    }

    public void setSmsContent(String smsContent)
    {
        this.smsContent = smsContent;
    }

    public String getCallContent()
    {
        return callContent;
    }

    public void setCallContent(String callContent)
    {
        this.callContent = callContent;
    }

    public List<Map<String, Object>> getWelfare()
    {
        return welfare;
    }

    public void setWelfare(List<Map<String, Object>> welfare)
    {
        this.welfare = welfare;
    }

    public Integer getSmsResult()
    {
        return smsResult;
    }

    public void setSmsResult(Integer smsResult)
    {
        this.smsResult = smsResult;
    }

    public Integer getCallResult()
    {
        return callResult;
    }

    public void setCallResult(Integer callResult)
    {
        this.callResult = callResult;
    }
}

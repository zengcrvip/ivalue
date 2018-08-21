package com.axon.market.common.domain.iStatistics;

import java.util.Date;

/**
 * Created by wangtt on 2017/4/17.
 */
public class MessageMarketingDomain
{
    private String timest; //统计时间
    private Integer taskId; //任务Id
    private String modelId;//模型Id
    private Integer activiteId;//营销活动ID
    private String activiteName;//营销活动名称
    private Integer contentId;//内容Id
    private String contentDesc;//营销内容
    private String areaIds;//营销地市编码
    private String areaNames;//营销地市名称
    private String create_user;//任务提交人名称
    private Integer areaId;//任务提交人归属地市
    private Integer target_usernum;//任务目标人数
    private Integer send_num;//发送人数
    private Integer send_succ_usernum;//发送成功人数
    private Integer send_fail_usernum;//发送失败人数
    private Integer recv_succ_usernum;//到达人数
    private Integer feedback_usernum;//反馈人数
    private Integer feedback_usercnt;//反馈次数
    private Integer product_order_cnt;//订购笔数
    private Integer product_ordersucc_cnt;//订购成功笔数
    private Integer product_orderfail_cnt;//订购失败笔数
    private Integer product_order_user;//订购人数
    private Integer product_ordersucc_user;//订购成功人数
    private Integer product_orderfail_user;//订购失败人数
    private String market_type;//营销触点标示


    public String getModelId()
    {
        return modelId;
    }

    public void setModelId(String modelId)
    {
        this.modelId = modelId;
    }

    public String getTimest()
    {
        return timest;
    }

    public void setTimest(String timest)
    {
        this.timest = timest;
    }

    public Integer getTaskId()
    {
        return taskId;
    }

    public void setTaskId(Integer taskId)
    {
        this.taskId = taskId;
    }

    public Integer getActiviteId()
    {
        return activiteId;
    }

    public void setActiviteId(Integer activiteId)
    {
        this.activiteId = activiteId;
    }

    public String getActiviteName()
    {
        return activiteName;
    }

    public void setActiviteName(String activiteName)
    {
        this.activiteName = activiteName;
    }

    public Integer getContentId()
    {
        return contentId;
    }

    public void setContentId(Integer contentId)
    {
        this.contentId = contentId;
    }

    public String getContentDesc()
    {
        return contentDesc;
    }

    public void setContentDesc(String contentDesc)
    {
        this.contentDesc = contentDesc;
    }

    public String getAreaIds()
    {
        return areaIds;
    }

    public void setAreaIds(String areaIds)
    {
        this.areaIds = areaIds;
    }

    public String getAreaNames()
    {
        return areaNames;
    }

    public void setAreaNames(String areaNames)
    {
        this.areaNames = areaNames;
    }

    public String getCreate_user()
    {
        return create_user;
    }

    public void setCreate_user(String create_user)
    {
        this.create_user = create_user;
    }

    public Integer getAreaId()
    {
        return areaId;
    }

    public void setAreaId(Integer areaId)
    {
        this.areaId = areaId;
    }

    public Integer getTarget_usernum()
    {
        return target_usernum;
    }

    public void setTarget_usernum(Integer target_usernum)
    {
        this.target_usernum = target_usernum;
    }

    public Integer getSend_num()
    {
        return send_num;
    }

    public void setSend_num(Integer send_num)
    {
        this.send_num = send_num;
    }

    public Integer getSend_succ_usernum()
    {
        return send_succ_usernum;
    }

    public void setSend_succ_usernum(Integer send_succ_usernum)
    {
        this.send_succ_usernum = send_succ_usernum;
    }

    public Integer getSend_fail_usernum()
    {
        return send_fail_usernum;
    }

    public void setSend_fail_usernum(Integer send_fail_usernum)
    {
        this.send_fail_usernum = send_fail_usernum;
    }

    public Integer getRecv_succ_usernum()
    {
        return recv_succ_usernum;
    }

    public void setRecv_succ_usernum(Integer recv_succ_usernum)
    {
        this.recv_succ_usernum = recv_succ_usernum;
    }

    public Integer getFeedback_usernum()
    {
        return feedback_usernum;
    }

    public void setFeedback_usernum(Integer feedback_usernum)
    {
        this.feedback_usernum = feedback_usernum;
    }

    public Integer getFeedback_usercnt()
    {
        return feedback_usercnt;
    }

    public void setFeedback_usercnt(Integer feedback_usercnt)
    {
        this.feedback_usercnt = feedback_usercnt;
    }

    public Integer getProduct_order_cnt()
    {
        return product_order_cnt;
    }

    public void setProduct_order_cnt(Integer product_order_cnt)
    {
        this.product_order_cnt = product_order_cnt;
    }

    public Integer getProduct_ordersucc_cnt()
    {
        return product_ordersucc_cnt;
    }

    public void setProduct_ordersucc_cnt(Integer product_ordersucc_cnt)
    {
        this.product_ordersucc_cnt = product_ordersucc_cnt;
    }

    public Integer getProduct_orderfail_cnt()
    {
        return product_orderfail_cnt;
    }

    public void setProduct_orderfail_cnt(Integer product_orderfail_cnt)
    {
        this.product_orderfail_cnt = product_orderfail_cnt;
    }

    public Integer getProduct_order_user()
    {
        return product_order_user;
    }

    public void setProduct_order_user(Integer product_order_user)
    {
        this.product_order_user = product_order_user;
    }

    public Integer getProduct_ordersucc_user()
    {
        return product_ordersucc_user;
    }

    public void setProduct_ordersucc_user(Integer product_ordersucc_user)
    {
        this.product_ordersucc_user = product_ordersucc_user;
    }

    public Integer getProduct_orderfail_user()
    {
        return product_orderfail_user;
    }

    public void setProduct_orderfail_user(Integer product_orderfail_user)
    {
        this.product_orderfail_user = product_orderfail_user;
    }

    public String getMarket_type()
    {
        return market_type;
    }

    public void setMarket_type(String market_type)
    {
        this.market_type = market_type;
    }
}

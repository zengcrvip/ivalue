package com.axon.market.common.bean;

/**
 * Created by chenyu on 2016/11/1.
 */
public class SmsConfigBean
{
    /**
     * 定义SN3数值最大数
     */
    private Long maxOperateNumber;

    /**
     * sp num通知接口
     */
    private String spNumUrl;

    /**
     * task通知接口
     */
    private String taskUrl;

    /**
     * 客户群更新操作短信通知内容
     */
    private String segmentModifyNoticeSmsContent;

    /**
     * 第三方短信营销通知内容
     */
    private String thirdPartyChannelMarketSmsContent;

    /**
     * 发送短信验证码内容
     */
    private String verificationCodeSmsContent;

    /**
     * 审核后通知短信
     */
    private String auditNoticeSmsContent;

    /**
     * 催单通知短信
     */
    private String reminderNoticeSmsContent;

    //低消通知市级分配短信
    private String dixiaoRemindertoCitySmsContent;
    //通知市级人员手机名单
    private String dixiaoRemindertoCityContact;
    //低消市级分配完毕通知省级短信
    private String dixiaoRemindertoProvinceSmsContent;
    //通知省级人员手机名单
    private String dixiaoRemindertoProvinceContact;
    //低消线上分配完毕通知线上分配
    private String dixiaoRemindertoOnlineProvinceSmsContent;
    //通知线上省级人员手机名单
    private String dixiaoRemindertoOnlineProvinceContact;


    //通知八天未执行营业厅相关管理员，账号被自动禁用
    private String reminderDisableSmsContent;

    public Long getMaxOperateNumber()
    {
        return maxOperateNumber;
    }

    public void setMaxOperateNumber(Long maxOperateNumber)
    {
        this.maxOperateNumber = maxOperateNumber;
    }

    public String getSpNumUrl()
    {
        return spNumUrl;
    }

    public void setSpNumUrl(String spNumUrl)
    {
        this.spNumUrl = spNumUrl;
    }

    public String getTaskUrl()
    {
        return taskUrl;
    }

    public void setTaskUrl(String taskUrl)
    {
        this.taskUrl = taskUrl;
    }

    public String getSegmentModifyNoticeSmsContent()
    {
        return segmentModifyNoticeSmsContent;
    }

    public void setSegmentModifyNoticeSmsContent(String segmentModifyNoticeSmsContent)
    {
        this.segmentModifyNoticeSmsContent = segmentModifyNoticeSmsContent;
    }

    public String getThirdPartyChannelMarketSmsContent()
    {
        return thirdPartyChannelMarketSmsContent;
    }

    public void setThirdPartyChannelMarketSmsContent(String thirdPartyChannelMarketSmsContent)
    {
        this.thirdPartyChannelMarketSmsContent = thirdPartyChannelMarketSmsContent;
    }

    public String getVerificationCodeSmsContent()
    {
        return verificationCodeSmsContent;
    }

    public void setVerificationCodeSmsContent(String verificationCodeSmsContent)
    {
        this.verificationCodeSmsContent = verificationCodeSmsContent;
    }

    public String getAuditNoticeSmsContent()
    {
        return auditNoticeSmsContent;
    }

    public void setAuditNoticeSmsContent(String auditNoticeSmsContent)
    {
        this.auditNoticeSmsContent = auditNoticeSmsContent;
    }

    public String getReminderNoticeSmsContent()
    {
        return reminderNoticeSmsContent;
    }

    public void setReminderNoticeSmsContent(String reminderNoticeSmsContent)
    {
        this.reminderNoticeSmsContent = reminderNoticeSmsContent;
    }

    public String getDixiaoRemindertoCitySmsContent() {
        return dixiaoRemindertoCitySmsContent;
    }

    public void setDixiaoRemindertoCitySmsContent(String dixiaoRemindertoCitySmsContent) {
        this.dixiaoRemindertoCitySmsContent = dixiaoRemindertoCitySmsContent;
    }

    public String getDixiaoRemindertoCityContact() {
        return dixiaoRemindertoCityContact;
    }

    public void setDixiaoRemindertoCityContact(String dixiaoRemindertoCityContact) {
        this.dixiaoRemindertoCityContact = dixiaoRemindertoCityContact;
    }

    public String getDixiaoRemindertoProvinceSmsContent() {
        return dixiaoRemindertoProvinceSmsContent;
    }

    public void setDixiaoRemindertoProvinceSmsContent(String dixiaoRemindertoProvinceSmsContent) {
        this.dixiaoRemindertoProvinceSmsContent = dixiaoRemindertoProvinceSmsContent;
    }

    public String getDixiaoRemindertoProvinceContact() {
        return dixiaoRemindertoProvinceContact;
    }

    public void setDixiaoRemindertoProvinceContact(String dixiaoRemindertoProvinceContact) {
        this.dixiaoRemindertoProvinceContact = dixiaoRemindertoProvinceContact;
    }

    public String getReminderDisableSmsContent()
    {
        return reminderDisableSmsContent;
    }

    public void setReminderDisableSmsContent(String reminderDisableSmsContent)
    {
        this.reminderDisableSmsContent = reminderDisableSmsContent;
    }

    public String getDixiaoRemindertoOnlineProvinceSmsContent() {
        return dixiaoRemindertoOnlineProvinceSmsContent;
    }

    public void setDixiaoRemindertoOnlineProvinceSmsContent(String dixiaoRemindertoOnlineProvinceSmsContent) {
        this.dixiaoRemindertoOnlineProvinceSmsContent = dixiaoRemindertoOnlineProvinceSmsContent;
    }

    public String getDixiaoRemindertoOnlineProvinceContact() {
        return dixiaoRemindertoOnlineProvinceContact;
    }

    public void setDixiaoRemindertoOnlineProvinceContact(String dixiaoRemindertoOnlineProvinceContact) {
        this.dixiaoRemindertoOnlineProvinceContact = dixiaoRemindertoOnlineProvinceContact;
    }
}

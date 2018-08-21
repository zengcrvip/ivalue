package com.axon.market.core.service.icommon;

import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.domain.icommon.market.JumpLinkDomain;
import com.axon.market.common.domain.iresource.SmsContentDomain;
import com.axon.market.common.util.AxonEncryptUtil;
import com.axon.market.common.util.DisposeLinkUtil;
import com.axon.market.common.util.HttpUtil;
import com.axon.market.common.util.JsonUtil;
import com.axon.market.core.service.iresource.SmsContentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by chenyu on 2016/11/9.
 */
@Component("sendSmsService")
public class SendSmsService
{
    private static final Logger LOG = Logger.getLogger(SendSmsService.class.getName());

    @Autowired
    @Qualifier("systemConfigBean")
    private SystemConfigBean systemConfigBean;

    @Autowired
    @Qualifier("smsContentService")
    private SmsContentService smsContentService;

    @Autowired
    @Qualifier("axonEncrypt")
    private AxonEncryptUtil axonEncrypt;

    @Autowired
    @Qualifier("smsMarketService")
    private SmsMarketService smsMarketService;

    private HttpUtil httpUtil = HttpUtil.getInstance();

    /**
     * 登录接口三网通的接入号
     * @param phone
     * @param message
     * @return
     */
    public boolean sendVerificationCodeSms(String phone, String message,String province)
    {
        try
        {
            Map<String, String> param = new HashMap<String, String>();
            param.put("mob", phone);
            param.put("msg", message);
            param.put("pswd", systemConfigBean.getSendSmsPwd());
            String response = httpUtil.sendHttpPost(systemConfigBean.getSendSmsUrl(), param);
            LOG.info("sendVerificationCodeSms response:"+response);

            //江苏和广西短信发送接口返回结果不一样
            if ("JS".equals(province))
            {
                Map<String, String> result = JsonUtil.stringToObject(response, Map.class);
                if (result != null)
                {
                    if ("00000".equals(result.get("ResultCode")))
                    {
                        return true;
                    }
                }
            }
            else
            {
                Document document = DocumentHelper.parseText(response);
                Element root = document.getRootElement();
                LOG.info("sendVerificationCodeSms Info:" + root.getText());
                if ("0".equals(root.getText()))
                {
                    return true;
                }
            }

        }
        catch (Exception e)
        {
            LOG.error("Send Verification Code error. ", e);
            return false;
        }

        return false;
    }

    /**
     * @param phones
     * @param message
     */
    public void sendMarketJobTestSms(List<String> phones, String message,String contentId)
    {
        try
        {
            List<Map<String, String>> requestList = new ArrayList<Map<String, String>>();
            List<JumpLinkDomain> jumpLinkList = new ArrayList<JumpLinkDomain>();

            for (String phone : phones)
            {
                if (StringUtils.isNotEmpty(contentId))
                {
                    String shortLink = DisposeLinkUtil.getShortLink();
                    SmsContentDomain smsContentDomain = smsContentService.querySmsContentById(Integer.valueOf(contentId));
                    String smsUrl = smsContentDomain.getUrl();
                    if (StringUtils.isNotEmpty(smsUrl))
                    {
                        packageSmsLinkDomain(phone, smsUrl, shortLink, requestList, jumpLinkList);
                        sendSmsHandle(phone, MessageFormat.format(message, systemConfigBean.getShortLinkPrefix() + shortLink));

                        // 最后统一发送长短链映射请求
                        if (requestList.size() == phones.size())
                        {
                            jumpLinksOperate(requestList,jumpLinkList);
                        }
                        continue;
                    }
                }
                sendSmsHandle(phone,message);
            }
        }
        catch (Exception e)
        {
            LOG.error("Send Market Job Test Sms error. ", e);
        }
    }

    /**
     * 发送客户群模型更新提醒
     * @param phones
     * @param message
     * @throws IOException
     */
    public void sendSmsOfModelNotice(List<String> phones, String message) throws IOException
    {
        Map<String, String> param = new HashMap<String, String>();
        for (String phone : phones)
        {
            param.put("mob", phone);
            param.put("msg", message);
            param.put("pswd", systemConfigBean.getSendSmsPwd());
            String response = httpUtil.sendHttpPost(systemConfigBean.getSendSmsUrl(), param);
            Map<String, String> result = JsonUtil.stringToObject(response, Map.class);
            if (result != null) {
                if (!"00000".equals(result.get("ResultCode"))) {
                    LOG.info("Send Model Create/Update Sms Notice Unsuccessful : response -> " + response + " , phone -> " + phone);
                }
            }
        }

    }

    /**
     * 组装短链信息对象
     * @param phone
     * @param smsUrl
     * @param requestList
     * @param jumpLinkList
     */
    private void packageSmsLinkDomain(String phone,String smsUrl,String shortLink, List<Map<String, String>> requestList, List<JumpLinkDomain> jumpLinkList)
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("Short", shortLink);
        map.put("Long", smsUrl);
        map.put("phone", axonEncrypt.decrypt(phone));
        map.put("province", systemConfigBean.getProvince());
        map.put("taskid", "-1");
        requestList.add(map);

        // 构成短链存储数据库bean
        JumpLinkDomain jumpLinkDomain = new JumpLinkDomain();
        jumpLinkDomain.setuId(shortLink);
        jumpLinkDomain.setLongLink(smsUrl);
        jumpLinkDomain.setMob(Long.parseLong(axonEncrypt.encrypt(phone)));
        jumpLinkDomain.setProvince(systemConfigBean.getProvince());
        jumpLinkDomain.setTaskId(-1);
        jumpLinkList.add(jumpLinkDomain);
    }

    /**
     * 短信发送公共方法
     * @param phone
     * @param message
     */
    private void sendSmsHandle(String phone, String message) throws IOException, DocumentException {
        Map<String, String> param = new HashMap<String, String>();
        param.put("mob", phone);
        param.put("msg", ("GX".equals(systemConfigBean.getProvince())?"" : "【智能营销】") + message);
        param.put("pswd", systemConfigBean.getSendSmsPwd());
        String response = httpUtil.sendHttpPost(systemConfigBean.getSendSmsUrl(), param);
        LOG.info("sendVerificationCodeSms response:"+response);

        Document document = DocumentHelper.parseText(response);
        Element root = document.getRootElement();
        if (!"0".equals(root.getText()))
        {
            LOG.info("Send Market Job Test Sms Unsuccessful : response -> " + response + " , phone -> " + phone);
        }
    }

    /**
     * @param phone
     * @param message
     */
    public void sendAuditNoticeSms(String phone, String message)
    {
        try
        {
            Map<String, String> param = new HashMap<String, String>();
            param.put("mob", phone);
            param.put("msg", message);
            param.put("pswd", systemConfigBean.getSendSmsPwd());
            String response = httpUtil.sendHttpPost(systemConfigBean.getSendSmsUrl(), param);

            Map<String, String> result = JsonUtil.stringToObject(response, Map.class);
            if (result != null)
            {
                if (!"00000".equals(result.get("ResultCode")))
                {
                    LOG.info("Send Audit Notice Sms Unsuccessful : response -> " + response + " , phone -> " + phone);
                }
            }
        }
        catch (Exception e)
        {
            LOG.error("Send Audit Notice Sms error. ", e);
        }
    }

    /**登录，提醒短信调用接口
     * @param phone
     * @param message
     */
    public String sendReminderNoticeSms(String phone, String message)
    {
        try
        {
            Map<String, String> param = new HashMap<String, String>();
            param.put("mob", phone);
            param.put("msg", message);
            param.put("callPswd", systemConfigBean.getSendSmsWebServicePWD());
            String response = httpUtil.sendHttpPost(systemConfigBean.getSendSmsWebServiceURL(), param);
            Document document = DocumentHelper.parseText(response);
            Element root = document.getRootElement();
            LOG.info("Send reminder notice Sms result:"+phone + ":" + root.getText());
            /*if (!"0".equals(root.getText()))
            {
                LOG.info("Send reminder Notice Sms Unsuccessful : responseCode -> " + root.getText() + " , phone -> " + phone);
            }*/
            return root.getText();
        }
        catch (Exception e)
        {
            LOG.error("Send reminder Notice Sms error. ", e);
        }
        return null;
    }

    /**
     * 长短链配对
     * @param requestList
     * @param jumpLinkList
     */
    private void jumpLinksOperate(List<Map<String, String>> requestList, List<JumpLinkDomain> jumpLinkList)
    {
        try
        {
            if (CollectionUtils.isNotEmpty(jumpLinkList))
            {
                Map<String, String> param = new HashMap<String, String>();
                String jumpLinks = "{\"links\":" + JsonUtil.objectToString(requestList) + "}";
                param.put("passWord", "axon@2016!");
                param.put("jumpLinks", jumpLinks);
                String a = httpUtil.sendHttpPost(systemConfigBean.getJumpLinkUrl(), param);

                smsMarketService.createJumpLink(jumpLinkList);
            }
        }
        catch (JsonProcessingException e)
        {
            LOG.error("", e);
        }
    }
}
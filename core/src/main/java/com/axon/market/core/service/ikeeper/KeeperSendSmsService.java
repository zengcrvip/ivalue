package com.axon.market.core.service.ikeeper;

//import com.axon.icloud.common.httpclient.HttpClientSession;
//import com.axon.icloud.common.httpclient.HttpClientTemplate;
//import com.axon.icloud.scene.sms.common.SmsRequest;
//import com.axon.icloud.scene.sms.common.SmsResponse;
import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.util.AxonEncryptUtil;
import com.axon.market.common.util.JsonUtil;
import com.axon.market.dao.mapper.ikeeper.IKeeperTaskAppMapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuanfei on 2017/8/20.
 */
@Service("keeperSendSmsService")
public class KeeperSendSmsService
{
    private static final Logger LOG = Logger.getLogger(KeeperSendSmsService.class.getName());

    //批量短信发送上限
    private static final Integer BASE_SMS_LIMIT = 10000;

    //掌柜类型
    private static final Integer KEEPER_TASK_TYPE = 5;

//    @Autowired
//    @Qualifier("httpClientTemplate")
//    private HttpClientTemplate httpClientTemplate;

    @Autowired
    @Qualifier("systemConfigBean")
    private SystemConfigBean systemConfigBean;

    public Map<String,Object> sendBatchSms(List<Map<String,Object>> taskInstDetailList,Map<String,Object> taskSmsDetailMap, String smsSignature)
    {
        Map<String,Object> result = new HashMap<String,Object>();
        int count = 0;
        List<Integer> detailIdList = new ArrayList<Integer>();

        // 保存发送成功的实例详情id
        List<Integer> successDetailIdList = new ArrayList<Integer>();
//        for (Map<String,Object> taskInstDetail : taskInstDetailList)
//        {
//            String telephone = AxonEncryptUtil.getInstance().decryptWithoutCountrycode(String.valueOf(taskInstDetail.get("telephone")));
//            String taskId = String.valueOf(taskInstDetail.get("taskId"));
//            String smsContent = String.valueOf(taskInstDetail.get("smsContent"));
//            String accessNumber = String.valueOf(taskInstDetail.get("accessNumber"));
//            String detailId = String.valueOf(taskInstDetail.get("detailId"));
//
//            String autograph = String.valueOf(taskSmsDetailMap.get(taskId));
//            String message = smsContent + ("1".equals(autograph)? "[" + smsSignature + "]" : "");
//
//            LOG.info("Send Keeper Sms : " + telephone);
//            HttpClientSession session = null;
//            SmsResponse response = null;
//            try
//            {
//                SmsRequest request = getSmsRequest(Integer.valueOf(taskId), telephone, accessNumber, message, KEEPER_TASK_TYPE);
//                session = httpClientTemplate.create("UTF-8");
//
//                String json = JsonUtil.objectToString(request);
//                LOG.info("Send Keeper Sms json : "+json);
//                String responseMessage = session.post(systemConfigBean.getYunSmsServiceURL(), json);
//                LOG.info("Keeper task Sms Request : " + request.getPhone() + ", Response : " + responseMessage);
//                response = JsonUtil.stringToObject(responseMessage, SmsResponse.class);
//            }
//            catch (Exception e)
//            {
//                LOG.error("http post failed!", e);
//                response = new SmsResponse();
//                response.setStatus(3);
//                response.setMessage(e.getMessage());
//            }
//            finally
//            {
//                IOUtils.closeQuietly(session);
//            }
//
//            // 将发送成功的保存到list，便后后面讲短信维系记录保存到记录表
//            if (response.getStatus() == 0)
//            {
//                successDetailIdList.add(Integer.valueOf(detailId));
//            }

//            detailIdList.add(Integer.valueOf(detailId));
//            count++;
//            LOG.info("Send Keeper Sms : " + telephone + " success");
//        }
//        result.put("count",count);
//        result.put("detailIds", StringUtils.join(detailIdList,","));
//        result.put("successDetailIds",StringUtils.join(detailIdList,","));
        return result;
    }

    /**
     * 掌柜任务单个短信发送
     * @param phone
     * @param smsMessage
     * @param taskId
     * @param accessNumber
     */
//    public SmsResponse sendSms(String phone, String smsMessage, Integer taskId, String accessNumber)
//    {
//        HttpClientSession session = null;
//        SmsResponse response = null;
//        try
//        {
//            SmsRequest request = getSmsRequest(taskId,phone,accessNumber,smsMessage, KEEPER_TASK_TYPE);
//            session = httpClientTemplate.create("UTF-8");
//            String json = JsonUtil.objectToString(request);
//            String responseMessage = session.post(systemConfigBean.getYunSmsServiceURL(), json);
//            LOG.info("Keeper Task Sms Request : " + request.getPhone() + ", Response : " + responseMessage);
//            response = JsonUtil.stringToObject(responseMessage, SmsResponse.class);
//        }
//        catch (Exception var10)
//        {
//            LOG.error("http post failed!", var10);
//            response = new SmsResponse();
//            response.setStatus(3);
//            response.setMessage(var10.getMessage());
//        }
//        finally
//        {
//            IOUtils.closeQuietly(session);
//        }
//        return response;
//    }

    /**
     * 设置批量发送请求
     * @param taskId
     * @param telephone
     * @param accessNumber
     * @param smsContent
     * @param taskType 5：掌柜类型
     * @return
     */
//    private SmsRequest getSmsRequest(Integer taskId,String telephone,String accessNumber,String smsContent,Integer taskType)
//    {
//        SmsRequest smsRequest = new SmsRequest();
//        smsRequest.setTaskId(taskId);
//        smsRequest.setAccessNumber(accessNumber);
//        smsRequest.setPhone(telephone);
//        smsRequest.setMessage(smsContent);
//        smsRequest.setTime(System.currentTimeMillis());
//        //smsRequest.setResourceId(String.valueOf(baseId));
//        //smsRequest.setResourceQuota(null != systemConfigBean.getBaseSmsLimit() && systemConfigBean.getBaseSmsLimit() > 0 ? systemConfigBean.getBaseSmsLimit(): BASE_SMS_LIMIT);
//        smsRequest.setTaskType(taskType);
//        //smsRequest.setShopTaskType(String.valueOf(shopTaskType));
//
//        return smsRequest;
//
//
//    }
}

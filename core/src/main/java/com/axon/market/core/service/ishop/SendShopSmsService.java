package com.axon.market.core.service.ishop;

//import com.axon.icloud.common.httpclient.HttpClientSession;
//import com.axon.icloud.common.httpclient.HttpClientTemplate;
//import com.axon.icloud.scene.sms.common.SmsRequest;
//import com.axon.icloud.scene.sms.common.SmsResponse;
import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.util.AxonEncryptUtil;
import com.axon.market.common.util.JsonUtil;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenyu on 2017/3/20.
 */
@Service("sendShopSmsService")
public class SendShopSmsService
{
    private static final Logger LOG = Logger.getLogger(SendShopSmsService.class.getName());
    //同一家每天限制短信发送量为5W
   private static final Integer BASE_SMS_LIMIT = 50000;

    @Autowired
    @Qualifier("greenPlumOperateService")
    private GreenPlumOperateService greenPlumOperateService;

    @Autowired
    @Qualifier("axonEncrypt")
    private AxonEncryptUtil axonEncrypt;

//    @Autowired
//    @Qualifier("httpClientTemplate")
//    private HttpClientTemplate httpClientTemplate;

    @Autowired
    @Qualifier("systemConfigBean")
    private SystemConfigBean systemConfigBean;

    public static SendShopSmsService getInstance()
    {
        return (SendShopSmsService) SpringUtil.getSingletonBean("sendShopSmsService");
    }

    /**
     * @param tableName
     * @param baseId
     * @return
     */
    public String createSelectDataFromModelSql(String tableName,List<String> modelTableNameList, int baseId,int baseAreaId, int type)
    {
        StringBuffer sql = new StringBuffer();
        sql.append("select t.phone from ")
           .append(tableName)
           .append(" t where t.base_id = '").append(baseId).append("'")
           .append(" and t.addr_type = ").append(type)
           .append(" and not exists (select 1 from shop.shop_black_user u where u.phone = t.phone and (u.hide_area in ('")
                .append(baseAreaId).append("','99999') or u.hide_bases = '").append(baseId).append("'))");
        if (CollectionUtils.isNotEmpty(modelTableNameList))
        {
            sql.append(" and (exists (select 1 from "+ modelTableNameList.get(0)+" m where t.phone = m.phone)");
            for (int i = 1; i < modelTableNameList.size(); i++)
            {
                sql.append(" or exists (select 1 from "+ modelTableNameList.get(i)+" m where t.phone = m.phone)");
            }
            sql.append(" )");
        }

        return sql.toString();
    }

    public String createSelectDataFromModelSql(String tableName,String saleId, String saleBoidId, String aimSubId)
    {
        StringBuffer sql = new StringBuffer();
        sql.append("select t.serial_number as phone from ")
                .append(tableName)
                .append(" t where t.sale_id = '").append(saleId).append("'")
                .append(" and t.sale_boid_id = '").append(saleBoidId).append("'")
                .append(" and t.aim_sub_id = '").append(aimSubId).append("'")
                .append(" and not exists (select 1 from shop.icloud_user_success s where t.sale_id = s.sale_id and t.sale_boid_id = s.sale_boid_id and t.aim_sub_id = s.aim_sub_id )");
        return sql.toString();
    }

    /**
     * @param phoneList
     * @param smsMessage
     * @param taskId
     * @param accessNumber
     * @param taskType 任务类型 1、短信群发，2、炒店业务  3、场景短信  4、精细化智能云业务
     * @param shopTaskType 炒店任务来源： 1、常驻  2、流动拜访  3、指定用户  4、老客
     * @return
     */
    public int sendSms(List<String> phoneList, String smsMessage, Integer taskId, String accessNumber,Integer baseId,Integer limit,Integer taskType, Integer shopTaskType)
    {
        int count = 0;
//        for (String testPhone : phoneList)
//        {
//            LOG.info("Send Shop Sms : " + axonEncrypt.decrypt(testPhone));
//            HttpClientSession session = null;
//            SmsResponse response = null;
//            SmsRequest request = null;
//            try
//            {
//                 request = getSmsRequest(axonEncrypt.decrypt(testPhone), taskId, smsMessage, accessNumber, baseId, taskType, shopTaskType);
//                 session = httpClientTemplate.create("UTF-8");
//                if(limit != null && count > limit){
//                    break;
//                }
//                String json = JsonUtil.objectToString(request);
//                LOG.info("Send Shop Sms json : "+json);
//                String responseMessage = session.post(systemConfigBean.getYunSmsServiceURL(), json);
//                LOG.info("shop task Sms Request : " + request.getPhone() + ", Response : " + responseMessage);
//                response = JsonUtil.stringToObject(responseMessage, SmsResponse.class);
//            }
//            catch (Exception var10)
//            {
//                LOG.error("http post failed!", var10);
//                response = new SmsResponse();
//                response.setStatus(3);
//                response.setMessage(var10.getMessage());
//            }
//            finally
//            {
//                IOUtils.closeQuietly(session);
//            }
//
//            if (response.getStatus() == 0)
//            {
//                LOG.info("Send Shop Sms : " + axonEncrypt.decrypt(testPhone) + " success");
//                count++;
//            }
//        }
        return count;
    }

    /**
     * @param sql
     * @param fetchSize
     * taskType   任务类型： 1、短信群发，2、炒店业务  3、场景短信  4、精细化智能云业务
     * shopTaskType  shopTaskType业务类型  1：常驻  2：流动拜访   3：指定用户
     * @return
     */
    public int sendSms(String sql, final int fetchSize, final int marketUserCountLimit, final String smsMessage, final Integer taskId, final String accessNumber, final  Integer baseId, final Integer taskType, final Integer shopTaskType)
    {
        final Integer count[] = {new Integer(0)};
        final List<String> phoneList = new ArrayList<String>();

        greenPlumOperateService.query(sql, new RowCallbackHandler()
        {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException
            {
                if (count[0] >= marketUserCountLimit)
                {
                    return;
                }

                String phone = resultSet.getString("phone");

                phoneList.add(axonEncrypt.decrypt(phone));

                if (phoneList.size() >= fetchSize)
                {
                    if (phoneList.size() > 0)
                    {
                        if (count[0] > marketUserCountLimit)
                        {
                            int more = count[0] - marketUserCountLimit;
                            count[0] += sendSms(phoneList.subList(0, phoneList.size() - more), smsMessage, taskId, accessNumber,baseId,null,taskType,shopTaskType);
                        }
                        else
                        {
                            count[0] += sendSms(phoneList, smsMessage, taskId, accessNumber,baseId,null,taskType, shopTaskType);
                        }
                        phoneList.clear();
                    }
                }
            }
        }, fetchSize);

        if (phoneList.size() > 0)
        {
            if (count[0] > marketUserCountLimit)
            {
                int more = count[0] - marketUserCountLimit;
                count[0] += sendSms(phoneList.subList(0, phoneList.size() - more), smsMessage, taskId, accessNumber,baseId,null,taskType,shopTaskType);
            }
            else
            {
                count[0] += sendSms(phoneList, smsMessage, taskId, accessNumber,baseId,null,taskType,shopTaskType);
            }

            phoneList.clear();
        }

        return count[0];
    }


    /**
     * 单个短信发送
     * @param phone 号码，未加密
     * @param smsMessage 短信内容
     * @param taskId 任务ID
     * @param accessNumber 接入号
     * @param taskType 任务类型
     */
    public void sendSms(String phone, String smsMessage, Integer taskId, String accessNumber,Integer taskType)
    {
//        HttpClientSession session = null;
//        SmsResponse response = null;
//        SmsRequest request = null;
//        try {
//            request = getSmsRequest(phone, taskId, smsMessage, accessNumber, null, taskType, null);
//            session = httpClientTemplate.create("UTF-8");
//            String json = JsonUtil.objectToString(request);
//            String responseMessage = session.post(systemConfigBean.getYunSmsServiceURL(), json);
//            LOG.info("shop task Sms Request : " + request.getPhone() + ", Response : " + responseMessage);
//            response = JsonUtil.stringToObject(responseMessage, SmsResponse.class);
//        } catch (Exception var10) {
//            LOG.error("http post failed!", var10);
//            response = new SmsResponse();
//            response.setStatus(3);
//            response.setMessage(var10.getMessage());
//        } finally {
//            IOUtils.closeQuietly(session);
//        }

    }

    /**
     *
     * @param phone  号码，明文
     * @param taskId  任务ID
     * @param message  任务信息
     * @param accessNumber  接入号
     * @param baseId  资源炒店ID
     *  BASE_SMS_LIMIT  资源ID的每天配额
     * @param  taskType  任务类型： 1、短信群发，2、炒店业务  3、场景短信  4、精细化智能云业务
     * @param shopTaskType  业务类型  1：常驻  2：流动拜访   3：指定用户
     * @return
     */
//    public SmsRequest getSmsRequest(String phone, int taskId, String message, String accessNumber,Integer baseId,Integer taskType,Integer shopTaskType)
//    {
//        SmsRequest request = new SmsRequest();
//        request.setPhone(String.valueOf(phone));
//        request.setTaskId(taskId);
//        request.setAccessNumber(accessNumber);
//        request.setMessage(message);
//        request.setTime(System.currentTimeMillis());
//        request.setResourceId(String.valueOf(baseId));
//        request.setResourceQuota(null != systemConfigBean.getBaseSmsLimit() && systemConfigBean.getBaseSmsLimit() > 0 ? systemConfigBean.getBaseSmsLimit(): BASE_SMS_LIMIT);
//        request.setTaskType(taskType);
//        request.setShopTaskType(String.valueOf(shopTaskType));
//        return request;
//    }
}

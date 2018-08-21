package com.axon.market.core.shoptask.impl;

//import com.axon.icloud.common.httpclient.HttpClientSession;
//import com.axon.icloud.common.httpclient.HttpClientTemplate;
//import com.axon.icloud.scene.sms.common.SmsRequest;
//import com.axon.icloud.scene.sms.common.SmsResponse;
import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.domain.ishop.ShopTaskDomain;
import com.axon.market.common.util.AxonEncryptUtil;
import com.axon.market.common.util.JsonUtil;
import com.axon.market.common.util.RedisUtil;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import com.axon.market.core.service.greenplum.OperateFileDataToGreenPlum;
import com.axon.market.core.service.ishop.SendShopSmsService;
import com.axon.market.core.service.ishop.ShopTaskService;
import com.axon.market.core.shoptask.TaskExecutor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by chenyu on 2017/3/6.
 */
@Component("recommendationTaskExecutor")
public class RecommendationTaskExecutor implements TaskExecutor
{
    private static final Logger LOG = Logger.getLogger(RecommendationTaskExecutor.class.getName());
    private static final  String SHOP_AREA_WHITE_PHONE_LIST  = "shopWhitePhoneList";

    @Autowired
    @Qualifier("greenPlumOperateService")
    private GreenPlumOperateService greenPlumOperateService;

    @Autowired
    @Qualifier("shopTaskService")
    private ShopTaskService shopTaskService;

    @Autowired
    @Qualifier("axonEncrypt")
    private AxonEncryptUtil axonEncrypt;

    @Autowired
    @Qualifier("systemConfigBean")
    private SystemConfigBean systemConfigBean;

    @Autowired
    @Qualifier("operateFileDataToGreenPlum")
    private OperateFileDataToGreenPlum operateFileDataToGreenPlum;

//    @Autowired
//    @Qualifier("httpClientTemplate")
//    private HttpClientTemplate httpClientTemplate;

    @Autowired
    @Qualifier("sendShopSmsService")
    private SendShopSmsService sendShopSmsService;

    @Override
    public String execute(ShopTaskDomain domain)
    {
        long currentMills = System.currentTimeMillis();
        String smsMessage = domain.getMarketContentText();
        String smsUrl = domain.getMarketUrl();
        if (StringUtils.isNotEmpty(smsUrl))
        {
            smsMessage += smsUrl;
        }
        //短信签名
        if(StringUtils.isNotEmpty(domain.getMessageAutograph()))
        {
            smsMessage += domain.getMessageAutograph();
        }

        if ((currentMills > getTimeMills("0900") && currentMills < getTimeMills("1030")) || (currentMills > getTimeMills("1130") && currentMills < getTimeMills("1500")) || (currentMills > getTimeMills("1600") && currentMills < getTimeMills("1700")))
        {
            //流动拜访
            int taskId = domain.getSmsTaskId();
            String sql = generateQueryUserSql(domain, 1);
            LOG.info("Recommendation Shop Flow Task Sql : " + sql);
            int count = sendSms(sql, taskId, smsMessage, domain.getAccessNumber(), 1, domain.getBaseId(), domain.getBaseAreaId(),2,2);
            if (count == 0)
            {
                return null;
            }
            return count + "-" + taskId;
        }
        else if ((currentMills > getTimeMills("1030") && currentMills < getTimeMills("1130")))
        {
            //早忙
            int taskId = domain.getSmsTaskId();
            String sql = generateQueryUserSql(domain, 2);
            LOG.info("Recommendation Shop Perl Task Sql : " + sql);
            int count = sendSms(sql, taskId, smsMessage, domain.getAccessNumber(), 2, domain.getBaseId(), domain.getBaseAreaId(),2,1);
            if (count == 0)
            {
                return null;
            }
            return count + "-" + taskId;
        }
        else if ((currentMills > getTimeMills("1500") && currentMills < getTimeMills("1600")))
        {
            // 午忙
            int taskId = domain.getSmsTaskId();
            String sql = generateQueryUserSql(domain, 3);
            LOG.info("Recommendation Shop Perl Task Sql : " + sql);
            int count = sendSms(sql, taskId, smsMessage, domain.getAccessNumber(), 2, domain.getBaseId(), domain.getBaseAreaId(),2,1);
            if (count == 0)
            {
                return null;
            }
            return count + "-" + taskId;
        }
        else if ((currentMills > getTimeMills("1700") && currentMills < getTimeMills("1800")))
        {
            // 晚忙
            int taskId = domain.getSmsTaskId();
            String sql = generateQueryUserSql(domain, 4);
            LOG.info("Recommendation Shop Perl Task Sql : " + sql);
            int count = sendSms(sql, taskId, smsMessage, domain.getAccessNumber(), 2, domain.getBaseId(), domain.getBaseAreaId(),2,1);
            if (count == 0)
            {
                return null;
            }
            return count + "-" + taskId;
        }
        return null;
    }

    private String generateQueryUserSql(ShopTaskDomain domain, int type)
    {
        Integer baseAreaId = domain.getBaseAreaId();
        Integer baseId = domain.getBaseId();
        String fullTableName = "model.model_" + baseAreaId;
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct phone from");
        if (type == 1)
        {
            sql.append(" ( select serial_number as phone from shop.shop_user s join shop.shop_target t on s.serial_number = t.phone where t.bsid = ").append(baseId)
               .append(" and s.sale_id = ").append("'").append(domain.getSaleId()).append("'")
               .append(" and s.sale_boid_id = ").append("'").append(domain.getSaleBoidId()).append("'")
               .append(" and s.aim_sub_id = ").append("'").append(domain.getAimSubId()).append("'")
               .append(" and not EXISTS (select 1 from shop.shop_recommendation_execute e where e.phone = s.serial_number)")
                    .append(" and not EXISTS  (select 1 from shop.shop_black_user u where u.phone = s.serial_number and (u.hide_area in ('")
                    .append(baseAreaId).append("','99999') or u.hide_bases = '").append(baseId).append("'))) a0");
        }
        else if (type == 2)
        {
            sql.append(" ( select serial_number as phone from shop.shop_user s join ").append(fullTableName).append(" w  on s.serial_number = w.phone where s.aim_sub_id = ").append("'").append(domain.getAimSubId()).append("'")
               .append(" and s.sale_id = ").append("'").append(domain.getSaleId()).append("'")
               .append(" and s.sale_boid_id = ").append("'").append(domain.getSaleBoidId()).append("'")
               .append(" and w.base_id = ").append("'").append(baseId).append("' and w.addr_type =200")
                    .append(" and not EXISTS (select 1 from shop.shop_recommendation_execute e where e.phone = s.serial_number)")
                    .append(" and not EXISTS (select 1 from shop.shop_black_user u where u.phone = s.serial_number and (u.hide_area in ('")
                    .append(baseAreaId).append("','99999') or u.hide_bases ='").append(baseId).append("'))) a0");
        }
        else if (type == 3)
        {
            sql.append(" ( select serial_number as phone from shop.shop_user s join ").append(fullTableName).append(" w  on s.serial_number = w.phone where s.aim_sub_id = ").append("'").append(domain.getAimSubId()).append("'")
                    .append(" and s.sale_id = ").append("'").append(domain.getSaleId()).append("'")
                    .append(" and s.sale_boid_id = ").append("'").append(domain.getSaleBoidId()).append("'")
                    .append(" and w.base_id = ").append("'").append(baseId).append("' and w.addr_type =300")
                    .append(" and not EXISTS (select 1 from shop.shop_recommendation_execute e where e.phone = s.serial_number)")
                    .append(" and not EXISTS (select 1 from shop.shop_black_user u where u.phone = s.serial_number and (u.hide_area in ('")
                    .append(baseAreaId).append("','99999') or u.hide_bases ='").append(baseId).append("'))) a0");
        }
        else if (type == 4)
        {
            sql.append(" ( select serial_number as phone from shop.shop_user s join ").append(fullTableName).append(" w  on s.serial_number = w.phone where s.aim_sub_id = ").append("'").append(domain.getAimSubId()).append("'")
                    .append(" and s.sale_id = ").append("'").append(domain.getSaleId()).append("'")
                    .append(" and s.sale_boid_id = ").append("'").append(domain.getSaleBoidId()).append("'")
                    .append(" and w.base_id = ").append("'").append(baseId).append("' and w.addr_type =100")
                    .append(" and not EXISTS (select 1 from shop.shop_recommendation_execute e where e.phone = s.serial_number)")
                    .append(" and not EXISTS (select 1 from shop.shop_black_user u where u.phone = s.serial_number and (u.hide_area in ('")
                    .append(baseAreaId).append("','99999') or u.hide_bases ='").append(baseId).append("'))) a0");
        }
        return sql.toString();
    }

    private int sendSms(String sql, final int taskId, final String smsMessage, final String accessNumber, final int type,final int baseId,final int baseAreaId,final Integer taskType, final Integer shopTaskType)
    {
        final int count[] = {0};
        final List<String> list = new ArrayList<String>();
        final String time = TimeUtil.formatDateToYMDHMS(new Date());
        //获取号码地区号段白名单
        final String phoneSegment = RedisUtil.getInstance().hget(SHOP_AREA_WHITE_PHONE_LIST,String.valueOf(baseAreaId));
        greenPlumOperateService.query(sql, new RowCallbackHandler()
        {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException
            {
                String phone = resultSet.getString("phone");
                String decryptPhone = axonEncrypt.decrypt(phone);
                //在白名单中才执行
                String subDecryptPhone = decryptPhone.substring(0,9);
                if(phoneSegment.indexOf(subDecryptPhone) > 0 ){
//                    SmsRequest request = sendShopSmsService.getSmsRequest(decryptPhone, taskId, smsMessage, accessNumber,baseId,taskType, shopTaskType);
//
//                    HttpClientSession session = httpClientTemplate.create("UTF-8");
//                    SmsResponse response;
                    try
                    {
//                        String json = JsonUtil.objectToString(request);
//                        LOG.info("Send Recommendation Sms : "+json);
//                        String responseMessage = session.post(systemConfigBean.getYunSmsServiceURL(), json);
//                        LOG.info("Request : " + request.getPhone() + ", Response : " + responseMessage);
//                        response = JsonUtil.stringToObject(responseMessage, SmsResponse.class);
                    }
                    catch (Exception e)
                    {
                        LOG.error("Http Post Failed ! ", e);
//                        response = new SmsResponse();
//                        response.setStatus(3);
//                        response.setMessage(e.getMessage());
                    }
                    finally
                    {
//                        IOUtils.closeQuietly(session);
                    }

//                    if (response.getStatus() == 0)
//                    {
//                        LOG.info("Send Recommendation Sms : " + decryptPhone + " success");
//                        list.add(phone + "|1|" + type + "|" + taskId + "|" + smsMessage + "|" + time);
//                        count[0]++;
//                    }
//                    else
//                    {
//                        LOG.info("Send Recommendation Sms : " + decryptPhone + " error -> message : " + response.getMessage());
//                        list.add(phone + "|2|" + type + "|" + taskId + "|" + smsMessage + "|" + time);
//                    }
                }
            }
        }, 10000);

        if (CollectionUtils.isNotEmpty(list))
        {
            // 创建目录，多线程唯一
            File directory = new File(systemConfigBean.getMonitorPath() + File.separator + UUID.randomUUID().toString());
            if (!directory.exists())
            {
                directory.mkdir();
            }
            String fileName = directory.getPath() + File.separator + "@shop.shop_recommendation_execute";
            File file = new File(fileName);
            try
            {
                FileUtils.writeLines(file, "UTF-8", list, true);
                File greenPlumFile = new File(file.getParent() + File.separator + fileName.substring(fileName.indexOf('@') + 1) + "@" + UUID.randomUUID().toString());
                LineIterator iterator = FileUtils.lineIterator(file);
                operateFileDataToGreenPlum.dataRefresh(iterator, greenPlumFile, null);
            }
            catch (Exception e)
            {
                LOG.error("Generate Recommendation Execute File Error. ", e);
            }
            finally
            {
                try
                {
                    if (file != null)
                    {
                        FileUtils.deleteQuietly(file);
                    }
                    if (directory != null)
                    {
                        FileUtils.deleteDirectory(directory);
                    }
                }
                catch (Exception e)
                {
                    LOG.error("Delete Directory Error. ", e);
                }
            }
        }
        return count[0];
    }

    /**
     * @param time
     * @return
     */
    private long getTimeMills(String time)
    {
        String timeString = TimeUtil.formatDateToYMDDevide(new Date());
        long result = 0L;
        try
        {
            switch (time)
            {
                case "0900":
                {
                    result = TimeUtil.formatDate(timeString + " 09:00:00").getTime();
                    break;
                }
                case "1030":
                {
                    result = TimeUtil.formatDate(timeString + " 10:30:00").getTime();
                    break;
                }
                case "1130":
                {
                    result = TimeUtil.formatDate(timeString + " 11:30:00").getTime();
                    break;
                }
                case "1500":
                {
                    result = TimeUtil.formatDate(timeString + " 15:00:00").getTime();
                    break;
                }
                case "1600":
                {
                    result = TimeUtil.formatDate(timeString + " 16:00:00").getTime();
                    break;
                }
                case "1700":
                {
                    result = TimeUtil.formatDate(timeString + " 17:00:00").getTime();
                    break;
                }
                case "1800":
                {
                    result = TimeUtil.formatDate(timeString + " 18:00:00").getTime();
                    break;
                }
            }
        }
        catch (ParseException e)
        {
            LOG.error("Parse Time Error. ", e);
        }
        return result;
    }
}

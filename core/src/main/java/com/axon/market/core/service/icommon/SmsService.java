package com.axon.market.core.service.icommon;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.SmsConfigBean;
import com.axon.market.common.domain.ischeduling.MarketJobDomain;
import com.axon.market.common.util.HttpUtil;
import com.axon.market.common.util.JsonUtil;
import com.axon.market.dao.mapper.icommon.ISmsMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenyu on 2016/11/4.
 */
@Component("smsService")
public class SmsService
{
    private static final Logger LOG = Logger.getLogger(SmsService.class.getName());

    @Autowired
    @Qualifier("smsConfigBean")
    private SmsConfigBean smsConfigBean;

    @Autowired
    @Qualifier("smsDao")
    private ISmsMapper smsDao;

    private HttpUtil httpUtil = HttpUtil.getInstance();

    /**
     * @param marketJobDomain
     * @return
     */
    public ServiceResult pauseMarket(MarketJobDomain marketJobDomain)
    {
        ServiceResult result = new ServiceResult();

        String taskId = String.valueOf(marketJobDomain.getLastTaskId());
        String spNum = marketJobDomain.getAccessNumber();

        try
        {
            int status = smsDao.queryTaskStatus(taskId, spNum);
            if (status == 6)
            {
                result.setRetValue(-1);
                result.setDesc("短信下发任务已经暂停");
            }
            else if (status != 1 && status != 4 && status != 8 && status != 9)
            {
                smsDao.updateTaskStatus(taskId, spNum, "6", null);
                notifyTaskServerService(taskId, spNum, "6");
            }
        }
        catch (Exception e)
        {
            LOG.error("", e);
        }

        return result;
    }

    /**
     * @param marketJobDomain
     * @return
     */
    public ServiceResult resumeMarket(MarketJobDomain marketJobDomain)
    {
        ServiceResult result = new ServiceResult();

        String taskId = String.valueOf(marketJobDomain.getLastTaskId());
        String spNum = marketJobDomain.getAccessNumber();

        try
        {
            int status = smsDao.queryTaskStatus(taskId, spNum);
            if (status == 6)
            {
                smsDao.updateTaskStatus(taskId, spNum, "7", String.valueOf(status));
                notifyTaskServerService(taskId, spNum, "7");
            }
        }
        catch (Exception e)
        {
            LOG.error("", e);
        }

        return result;
    }

    /**
     * @param taskId
     * @return
     */
    public boolean isSendingSmsByTaskId(Integer taskId)
    {
        try
        {
            if (taskId != null)
            {
                int status = smsDao.queryTaskStatus(String.valueOf(taskId),null);
                if (status == 1 || status == 4 || status == 8 || status == 9)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /**
     * @param taskId
     * @param spNum
     * @param status
     * @param oldStatus
     * @return
     */
    private String createUpdateTaskStatusSql(String taskId, String spNum, String status, String oldStatus)
    {
        StringBuffer sql = new StringBuffer();
        sql.append("update p_task set if_execute = ").append(status);
        sql.append(" where id = ").append(taskId);
        sql.append(" and sp_num = ").append(spNum);
        if (StringUtils.isNotEmpty(oldStatus))
        {
            sql.append(" and if_execute = ").append(oldStatus);
        }
        return sql.toString();
    }

    /**
     * @param taskId
     * @param spNum
     * @param action
     */
    private void notifyTaskServerService(String taskId, String spNum, String action)
    {
        try
        {
            Map<String, String> request = new HashMap<String, String>();
            request.put("taskid", taskId);
            request.put("spnum", spNum);
            request.put("action", action);
            String result = httpUtil.sendHttpPost(smsConfigBean.getTaskUrl(), JsonUtil.objectToString(request));
            LOG.info("Notify Update Task result : " + result);
        }
        catch (JsonProcessingException e)
        {
            LOG.error("", e);
        }
    }
}

package com.axon.market.core.service.iscene;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.SmsConfigBean;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.HttpUtil;
import com.axon.market.common.util.JsonUtil;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.dao.mapper.iscene.ISmsSendConfigMapper;
import com.axon.market.common.domain.iscene.SmsSendConfigDomain;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.axon.market.common.constant.ichannel.MarketConstants.SmsSendConfigOperateEnum.DELETE;

/**
 * Created by Administrator on 2016/10/24.
 */
@Component("smsSendConfigService")
public class SmsSendConfigService
{
    private static final Logger LOG = Logger.getLogger(SmsSendConfigService.class.getName());

    @Autowired
    @Qualifier("smsSendConfigDao")
    private ISmsSendConfigMapper smsSendConfigDao;

    @Autowired
    @Qualifier("smsConfigBean")
    private SmsConfigBean smsConfigBean;

    private HttpUtil httpUtil = HttpUtil.getInstance();

    /**
     * @return
     */
    public List<Map<String, Object>> getSPNum()
    {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> spList = smsSendConfigDao.getAccessNumber();

        Map<String, List<Map<String, Object>>> hostSpMap = new HashMap<String, List<Map<String, Object>>>();
        for (Map<String, Object> map : spList)
        {
            String host = String.valueOf(map.get("ip"));
            if (hostSpMap.containsKey(host))
            {
                hostSpMap.get(host).add(map);
            }
            else
            {
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                list.add(map);
                hostSpMap.put(host, list);
            }
        }

        for (Map.Entry<String, List<Map<String, Object>>> entry : hostSpMap.entrySet())
        {
            List<Map<String, Object>> list = entry.getValue();
            int count = list.size();
            for (int i = 0; i < count; i++)
            {
                Map<String, Object> map = list.get(i);
                map.put("sleeptime", (Long.parseLong(String.valueOf(map.get("sleeptime"))) * count));
                map.put("msginterval", (Long.parseLong(String.valueOf(map.get("msginterval"))) * count));
                map.put("sn3start", (smsConfigBean.getMaxOperateNumber() / count) * i);
                map.put("sn3end", ((smsConfigBean.getMaxOperateNumber() / count) * (i + 1)) - 1);
                result.add(map);
            }
        }
        return result;
    }

    /**
     * @param accessNumber
     * @return
     */
    public SmsSendConfigDomain querySmsSendConfigByAccessNumber(String accessNumber)
    {
        return smsSendConfigDao.querySmsSendConfigByAccessNumber(accessNumber);
    }

    private void notifyAccessNumberServerService(SmsSendConfigDomain smsSendConfigDomain, String type, List<Map<String, Object>> list)
    {
        switch (type)
        {
            case "create":
            {
                notifyCreateAccessNumber(smsSendConfigDomain, list);
                break;
            }
            case "update":
            {
                notifyUpdateAccessNumber(smsSendConfigDomain, list);
                break;
            }
            case "delete":
            {
                notifyDeleteAccessNumber(smsSendConfigDomain, list);
                break;
            }
        }
    }

    private void notifyCreateAccessNumber(SmsSendConfigDomain smsSendConfigDomain, List<Map<String, Object>> list)
    {
        int count = list.size();
        for (int i = 0; i < count; i++)
        {
            Map<String, Object> map = list.get(i);
            map.put("sleeptime", (Long.parseLong(String.valueOf(map.get("sleeptime"))) * count));
            map.put("msginterval", (Long.parseLong(String.valueOf(map.get("msginterval"))) * count));
            map.put("sn3start", (smsConfigBean.getMaxOperateNumber() / count) * i);
            map.put("sn3end", ((smsConfigBean.getMaxOperateNumber() / count) * (i + 1)) - 1);
            if (smsSendConfigDomain.getAccessNumber().equals(map.get("spnum")))
            {
                notifyAccessNumberServerService(map, "1");
            }
            else
            {
                notifyAccessNumberServerService(map, "2");
            }
        }
    }

    private void notifyUpdateAccessNumber(SmsSendConfigDomain smsSendConfigDomain, List<Map<String, Object>> list)
    {
        int count = list.size();
        for (int i = 0; i < count; i++)
        {
            Map<String, Object> map = list.get(i);
            if (smsSendConfigDomain.getAccessNumber().equals(map.get("spnum")))
            {
                map.put("sleeptime", (Long.parseLong(String.valueOf(map.get("sleeptime"))) * count));
                map.put("msginterval", (Long.parseLong(String.valueOf(map.get("msginterval"))) * count));
                map.put("sn3start", (smsConfigBean.getMaxOperateNumber() / count) * i);
                map.put("sn3end", ((smsConfigBean.getMaxOperateNumber() / count) * (i + 1)) - 1);
                notifyAccessNumberServerService(map, "2");
            }
        }
    }

    private void notifyDeleteAccessNumber(SmsSendConfigDomain smsSendConfigDomain, List<Map<String, Object>> list)
    {
        int count = list.size();
        for (int i = count - 1; i > 0; i--)
        {
            Map<String, Object> map = list.get(i);
            if (smsSendConfigDomain.getAccessNumber().equals(map.get("spnum")))
            {
                map.put("sleeptime", (Long.parseLong(String.valueOf(map.get("sleeptime"))) * count));
                map.put("msginterval", (Long.parseLong(String.valueOf(map.get("msginterval"))) * count));
                map.put("sn3start", (smsConfigBean.getMaxOperateNumber() / count) * i);
                map.put("sn3end", ((smsConfigBean.getMaxOperateNumber() / count) * (i + 1)) - 1);
                notifyAccessNumberServerService(map, "3");
                list.remove(i);
                break;
            }
        }
        count = list.size();
        for (int i = 0; i < count; i++)
        {
            Map<String, Object> map = list.get(i);
            map.put("sleeptime", (Long.parseLong(String.valueOf(map.get("sleeptime"))) * count));
            map.put("msginterval", (Long.parseLong(String.valueOf(map.get("msginterval"))) * count));
            map.put("sn3start", (smsConfigBean.getMaxOperateNumber() / count) * i);
            map.put("sn3end", ((smsConfigBean.getMaxOperateNumber() / count) * (i + 1)) - 1);
            notifyAccessNumberServerService(map, "2");
        }
    }

    /**
     * @param map
     * @param type
     */
    private void notifyAccessNumberServerService(Map<String, Object> map, String type)
    {
        try
        {
            Map<String, String> request = new HashMap<String, String>();
            request.put("config", JsonUtil.objectToString(map));
            request.put("action", type);
            String result = httpUtil.sendHttpPost(smsConfigBean.getSpNumUrl(), JsonUtil.objectToString(request));
            LOG.info("Notify Access Number result : " + result);
        }
        catch (JsonProcessingException e)
        {
            LOG.error("", e);
        }
    }

    /**
     * @return
     */
    public List<Map<String, String>> queryAllEffectiveAccessNumbers()
    {
        return smsSendConfigDao.queryAllEffectiveAccessNumbers();
    }

    /**
     * 查询 短信发送配置
     *
     * @param accessNumber 接入号
     * @param offset       从第几页开始
     * @param limit        获取几条数据
     * @return Table 列表统一返回对象
     */
    public Table querySmsSendConfigList(String accessNumber, Integer offset, Integer limit)
    {
        try
        {
            List<SmsSendConfigDomain> list = smsSendConfigDao.querySmsSendConfigList(accessNumber, offset, limit);
            int count = smsSendConfigDao.queryAllSmsSendConfigCount(accessNumber);
            return new Table(list, count);
        }
        catch (Exception ex)
        {
            LOG.error("querySmsSendConfigList error:" + ex.getMessage());
            return new Table();
        }
    }

    /**
     * 新增或修改 短信发送配置
     *
     * @param model 实体
     * @return Operation 增删改统一返回对象
     */
    public Operation addOrEditSmsSendConfig(SmsSendConfigDomain model, UserDomain userDomain)
    {
        if (model.getId() == null)
        {
            model.setCreateUser(userDomain.getId());
            model.setCreateTime(TimeUtil.formatDate(new Date()));
            model.setUpdateUser(userDomain.getId());
            model.setUpdateTime(TimeUtil.formatDate(new Date()));
            return addSmsSendConfig(model);
        }
        else
        {
            model.setUpdateUser(userDomain.getId());
            model.setUpdateTime(TimeUtil.formatDate(new Date()));
            return updateSmsSendConfig(model);
        }

    }

    /**
     * 删除 短信发送配置
     *
     * @param id 主键
     * @return Operation 增删改统一返回对象
     */
    public Operation deleteSmsSendConfig(Integer id)
    {
        try
        {
            SmsSendConfigDomain sendConfigDomain = smsSendConfigDao.querySmsSendConfigById(id);

            if (sendConfigDomain == null)
            {
                return new Operation(false, "未找到此数据");
            }

            List<Map<String, Object>> list = smsSendConfigDao.getAccessNumberByHost(sendConfigDomain.getIp());

            Boolean result = smsSendConfigDao.deleteSmsSendConfig(id) == 1;
            String msg = result ? "删除成功" : "删除失败";

            notifyAccessNumberServerService(sendConfigDomain, DELETE.getValue(), list);

            return new Operation(result, msg);
        }
        catch (Exception ex)
        {
            LOG.error("deleteSmsSendConfig error:" + ex.getMessage());
            return new Operation();
        }
    }

    /**
     * 新增 短信发送配置
     *
     * @param model 实体
     * @return Operation 增删改统一返回对象
     */
    private Operation addSmsSendConfig(SmsSendConfigDomain model)
    {
        try
        {
            // 查询是否有重复的接入号
            SmsSendConfigDomain domain = smsSendConfigDao.querySmsSendConfigByAccessNumber(model.getAccessNumber());
            if (domain != null)
            {
                return new Operation(false, "已存在相同的接入号，请修改");
            }

            Boolean result = smsSendConfigDao.insertSmsSendConfig(model) == 1;
            String message = result ? "新增成功" : "新增失败";
            return new Operation(result, message);
        }
        catch (Exception ex)
        {
            LOG.error("addSmsSendConfig error:" + ex.getMessage());
            return new Operation();
        }
    }

    /**
     * 修改 短信发送配置
     *
     * @param model 实体
     * @return Operation 增删改统一返回对象
     */
    private Operation updateSmsSendConfig(SmsSendConfigDomain model)
    {
        try
        {
            Boolean result = smsSendConfigDao.updateSmsSendConfig(model) == 1;
            String message = result ? "修改成功" : "修改失败";
            return new Operation(result, message);
        }
        catch (Exception ex)
        {
            LOG.error("updateSmsSendConfig error:" + ex.getMessage());
            return new Operation();
        }
    }
}

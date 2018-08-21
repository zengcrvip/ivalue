package com.axon.market.core.service.iscene;


import com.axon.market.common.bean.InterfaceBean;
import com.axon.market.common.domain.iscene.GlobalSettingDomain;
import com.axon.market.common.bean.GroupJson;
import com.axon.market.common.util.HttpUtil;
import com.axon.market.dao.mapper.iscene.IGlobalSettingMapper;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/1.
 */

@Service("globalSettingService")
public class GlobalSettingService
{
    Gson gs = new Gson();

    @Autowired
    @Qualifier("interfaceBean")
    private InterfaceBean interfaceBean;//接口类

    @Autowired(required = true)
    @Qualifier("globalSettingDao")
    private IGlobalSettingMapper globalSettingDao;

    private static final Logger LOG = Logger.getLogger(GlobalSettingService.class.getName());

    /**
     * 封装的调取全局设置接口的方法
     *
     * @param url
     * @param id
     * @param number
     * @return
     */
    private boolean sendMessages(String url, Integer id, Integer number)
    {
        Boolean result = false;
        Map map = new HashMap();
        Map testMap = new HashMap();
        testMap.put("type", globalSettingDao.queryType(id));
        testMap.put("value", number);
        map.put("message", gs.toJson(testMap));
        try
        {
            GroupJson groupJson = gs.fromJson(HttpUtil.getInstance().sendHttpPost(url, map), GroupJson.class);
            Integer i = groupJson.getResultCode();
            if (i == 0)
            {
                result = true;
            }
            else
            {
                LOG.error("SendMessages resultCode:" + i);
            }

        }
        catch (Exception e)
        {
            LOG.error("调用接口异常：", e);
            return false;
        }
        return result;
    }

    /**
     * 查询
     *
     * @return
     */
    public List<GlobalSettingDomain> queryGlobalSettings()
    {
        return globalSettingDao.queryGlobalSettings();
    }

    /**
     * 更新
     *
     * @param id
     * @param number
     * @return
     */
    public boolean updateGlobalSettings(Integer id, Integer number)
    {
        boolean result = false;
        try
        {
            result = globalSettingDao.updateGlobalSetting(id, number) > 0 ? sendMessages(interfaceBean.getGlobalSettingUrl(), id, number) : false;
        }
        catch (Exception e)
        {
            LOG.error("Update GlobalSetting failed ", e);
        }
        return result;
    }

}

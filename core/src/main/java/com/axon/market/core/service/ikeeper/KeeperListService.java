package com.axon.market.core.service.ikeeper;


import com.axon.market.common.bean.ServiceResult;

import com.axon.market.dao.mapper.ikeeper.IKeeperListMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by zengcr on 2017/4/21.
 */
@Service("keeperListService")
public class KeeperListService
{
    private static final Logger LOG = Logger.getLogger(KeeperListService.class.getName());

    @Qualifier("keeperListDao")
    @Autowired
    private IKeeperListMapper iKeeperListMapper;

    public ServiceResult fetchActivityInfo(Map<String,Object> params)
    {
        LOG.info("keeper fetchActivityInfo bigin");
        ServiceResult result = new ServiceResult();
        boolean flag = false;
        ArrayList<Map<String,Object>>  resourceList = null;
        try
        {
             resourceList = (ArrayList<Map<String,Object>>)params.get("activityInfo");
            if(iKeeperListMapper.fetchActivityInfo(resourceList) > 0){
                flag = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOG.error(" keeper fetchActivityInfo error", e);
            flag = false;
        }
        if(!flag){
            result.setRetValue(-1);
            result.setDesc("掌柜活动清单入库失败，请重试！");
        }
        LOG.info("keeper fetchActivityInfo num:"+resourceList.size());
        return result;
    }
}

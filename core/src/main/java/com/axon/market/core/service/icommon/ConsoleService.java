package com.axon.market.core.service.icommon;

import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.core.service.ishop.ShopTaskService;
import com.axon.market.dao.mapper.icommon.IConsoleMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by yuanfei on 2017/2/22.
 */
@Component("consoleService")
public class ConsoleService
{
    @Autowired
    @Qualifier("shopTaskService")
    private ShopTaskService shopTaskService;

    @Autowired
    @Qualifier("consoleDao")
    private IConsoleMapper consoleDao;

    /**
     * 查询数目
     * @param userDomain
     * @param type
     * @return
     */
    public int queryMyShopTaskCount(UserDomain userDomain,Integer type)
    {
        String businessHallIds = StringUtils.isNotEmpty(userDomain.getBusinessHallIds()) ? userDomain.getBusinessHallIds() : null;
        return consoleDao.queryMyShopTaskCount(businessHallIds, userDomain.getAreaCode(), type);
    }

    /**
     * 查询炒店的待执行信息和模型标签营销任务炒店的审批任务
     * @return
     */
    public List<Map<String,Object>> queryMyShopTaskByPage(UserDomain userDomain,Integer type,long offset, long maxRecord)
    {
        String businessHallIds = StringUtils.isNotEmpty(userDomain.getBusinessHallIds()) ? userDomain.getBusinessHallIds() : null;
        return consoleDao.queryMyShopTaskByPage(businessHallIds, userDomain.getAreaCode(), type, offset, maxRecord);
    }

    /**
     * @see IConsoleMapper#queryShopTaskTypeCount(String, Integer)
     * @return
     */
    public Map<String,Object> queryShopTaskTypeCount(UserDomain userDomain)
    {
        String businessHallIds = StringUtils.isNotEmpty(userDomain.getBusinessHallIds()) ? userDomain.getBusinessHallIds() : null;
        return consoleDao.queryShopTaskTypeCount(businessHallIds,userDomain.getAreaCode());
    }
}

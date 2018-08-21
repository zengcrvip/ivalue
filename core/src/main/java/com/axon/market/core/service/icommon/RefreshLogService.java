package com.axon.market.core.service.icommon;

import com.axon.market.common.domain.icommon.RefreshLogDomain;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.dao.mapper.icommon.IRefreshLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by Chris on 2016/11/29.
 */
@Component("refreshLogService")
public class RefreshLogService
{
    @Autowired
    @Qualifier("refreshLogDao")
    private IRefreshLogMapper refreshLogDao;

    public static RefreshLogService getInstance()
    {
        return (RefreshLogService) SpringUtil.getSingletonBean("refreshLogService");
    }

    /**
     * @param refreshLogDomain
     */
    public void insertRefreshLog(RefreshLogDomain refreshLogDomain)
    {
        refreshLogDao.insertRefreshLog(refreshLogDomain);
    }
}

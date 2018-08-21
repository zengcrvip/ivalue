package com.axon.market.core.service.ischeduling;


import com.axon.market.common.domain.ischeduling.MarketHistoryDomain;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.dao.mapper.ischeduling.IMarketHistoryMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by yangyang on 2016/3/4.
 */
@Component("marketHistoryService")
public class MarketHistoryService
{
    private static final Logger LOG = Logger.getLogger(MarketHistoryService.class.getName());

    @Autowired
    @Qualifier("marketHistoryDao")
    private IMarketHistoryMapper marketHistoryDao;

    /**
     * @return
     */
    public static MarketHistoryService getInstance()
    {
        return (MarketHistoryService) SpringUtil.getSingletonBean("marketHistoryService");
    }

    /**
     * @param historyDomain
     */
    public void insertMarketHistory(MarketHistoryDomain historyDomain)
    {
        int flag = marketHistoryDao.insertMarketHistory(historyDomain);
        if (flag != 1)
        {
            LOG.error("营销历史数据库新增操作异常");
        }
    }

    /**
     * @param jobId
     * @return
     */
    public List<MarketHistoryDomain> queryMarketHistoryByJobId(Integer jobId)
    {
        return marketHistoryDao.queryMarketHistoryByJobId(jobId);
    }

    /**
     * @param jobId
     * @return
     */
    public int queryAllMarketHistoryCounts(int jobId)
    {
        return marketHistoryDao.queryMarketHistoryCountsByJobId(jobId);
    }

    /**
     * @param jobId
     * @param offset
     * @param maxRecord
     * @return
     */
    public List<MarketHistoryDomain> queryAllMarketHistoryByPage(int jobId, long offset, long maxRecord)
    {
        return marketHistoryDao.queryMarketHistoryByPage(jobId, offset, maxRecord);
    }

}

package com.axon.market.core.service.ischeduling;

import com.axon.market.common.domain.ischeduling.MarketJobAuditHistoryDomain;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.dao.mapper.ischeduling.IMarketJobAuditHistoryMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by chenyu on 2016/5/25.
 */
@Component("marketJobAuditHistoryService")
public class MarketJobAuditHistoryService
{
    private static final Logger LOG = Logger.getLogger(MarketJobAuditHistoryService.class.getName());

    @Autowired
    @Qualifier("marketJobAuditHistoryDao")
    private IMarketJobAuditHistoryMapper marketJobAuditHistoryDao;

    /**
     * @return
     */
    public static MarketJobAuditHistoryService getInstance()
    {
        return (MarketJobAuditHistoryService) SpringUtil.getSingletonBean("marketJobAuditHistoryDao");
    }

    /**
     * @param marketJobAuditHistory
     * @return
     */
    public int insertMarketJobAuditHistory(MarketJobAuditHistoryDomain marketJobAuditHistory)
    {
        return marketJobAuditHistoryDao.insertMarketJobAuditHistory(marketJobAuditHistory);
    }

    /**
     * @param marketJobId
     * @return
     */
    public int deleteMarketJobAuditHistory(int marketJobId)
    {
        return marketJobAuditHistoryDao.deleteMarketJobAuditHistory(marketJobId);
    }


    /**
     * @param marketJobId
     * @return
     */
    public List<MarketJobAuditHistoryDomain> queryMarketJobAuditProgress(int marketJobId)
    {
        return marketJobAuditHistoryDao.queryMarketJobAuditProgress(marketJobId);
    }

    /**
     * @param marketJobId
     * @return
     */
    public List<MarketJobAuditHistoryDomain> queryMarketJobAuditHistoryDomain(int marketJobId)
    {
        return marketJobAuditHistoryDao.queryMarketJobAuditHistoryDomain(marketJobId);
    }

    /**
     * @param userId
     * @return
     */
    public List<Map<String, Object>> queryAllMarketJobAuditUser(int userId)
    {
        return marketJobAuditHistoryDao.queryAllMarketJobAuditUser(userId);
    }
}

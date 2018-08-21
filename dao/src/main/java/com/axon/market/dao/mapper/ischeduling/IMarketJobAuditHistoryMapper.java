package com.axon.market.dao.mapper.ischeduling;

import com.axon.market.common.domain.ischeduling.MarketJobAuditHistoryDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by chenyu on 2016/5/25.
 */
@Component("marketJobAuditHistoryDao")
public interface IMarketJobAuditHistoryMapper extends IMyBatisMapper
{
    /**
     * @param marketJobAuditHistory
     * @return
     */
    int insertMarketJobAuditHistory(@Param(value = "info") MarketJobAuditHistoryDomain marketJobAuditHistory);

    /**
     * @param marketJobId
     * @return
     */
    int deleteMarketJobAuditHistory(@Param(value = "marketJobId") int marketJobId);

    /**
     * @param marketJobId
     * @return
     */
    List<MarketJobAuditHistoryDomain> queryMarketJobAuditProgress(@Param(value = "marketJobId") int marketJobId);

    /**
     * @param marketJobId
     * @return
     */
    List<MarketJobAuditHistoryDomain> queryMarketJobAuditHistoryDomain(@Param(value = "marketJobId") int marketJobId);

    /**
     * @param userId
     * @return
     */
    List<Map<String, Object>> queryAllMarketJobAuditUser(@Param(value = "userId") int userId);
}

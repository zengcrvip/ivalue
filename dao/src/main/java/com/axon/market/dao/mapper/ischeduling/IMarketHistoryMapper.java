package com.axon.market.dao.mapper.ischeduling;

import com.axon.market.common.domain.ischeduling.MarketHistoryDomain;
import com.axon.market.dao.base.IMyBatisMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by yangyang on 2016/3/4.
 */
@Component("marketHistoryDao")
public interface IMarketHistoryMapper extends IMyBatisMapper
{
    /**
     *
     * @param jobId
     * @return
     */
    List<MarketHistoryDomain> queryMarketHistoryByJobId(@Param(value = "jobId") Integer jobId);

    /**
     * @param jobId
     * @return
     */
    int queryMarketHistoryCountsByJobId(@Param(value = "jobId") Integer jobId);

    /**
     * @param jobId
     * @return
     */
    List<MarketHistoryDomain> queryMarketHistoryByPage(@Param(value = "jobId") Integer jobId, @Param(value = "offset") long offset, @Param(value = "maxRecord") long maxRecord);

    /**
     *
     * @param infos
     * @return
     */
    int insertMarketHistory(MarketHistoryDomain infos);

}

package com.axon.market.dao.mapper.icommon;

import com.axon.market.common.domain.icommon.RefreshLogDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * Created by Chris on 2016/11/29.
 */
@Component("refreshLogDao")
public interface IRefreshLogMapper extends IMyBatisMapper
{
    /**
     * @param refreshLogDomain
     * @return
     */
    Integer insertRefreshLog(@Param(value = "info") RefreshLogDomain refreshLogDomain);
}

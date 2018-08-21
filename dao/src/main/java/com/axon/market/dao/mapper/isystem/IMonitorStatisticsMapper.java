package com.axon.market.dao.mapper.isystem;

import com.axon.market.common.domain.isystem.MonitorStatisticsDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xuan on 2017/4/19.
 */
@Component("monitorStatisticsDao")
public interface IMonitorStatisticsMapper  extends IMyBatisMapper
{
    /**
     * @return
     */
    Integer queryMonitorStatisticsCounts(@Param(value = "serverIp") String serverIp,@Param(value = "status") Integer status);

    /**
     * @param limit
     * @param offset
     * @return
     */
    List<MonitorStatisticsDomain> queryMonitorStatistics(@Param(value = "offset") Integer offset, @Param(value = "limit") Integer limit,@Param(value = "serverIp") String serverIp,@Param(value = "status") Integer status);

}

package com.axon.market.core.service.isystem;

import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.isystem.MonitorStatisticsDomain;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.dao.mapper.isystem.IMonitorStatisticsMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by xuan on 2017/4/19.
 */
@Component("monitorStatisticsService")
public class MonitorStatisticsService
{
    private static final Logger LOG = Logger.getLogger(MonitorStatisticsService.class.getName());

    @Autowired
    @Qualifier("monitorStatisticsDao")
    private IMonitorStatisticsMapper monitorStatisticsDao;

    /**
     * 列表查询
     * @param param
     * @return
     */
    public Table queryMonitorStatistics(Map<String, String> param)
    {
        try
        {
            Integer start = Integer.parseInt(param.get("start"));
            Integer length = Integer.parseInt(param.get("length"));
            String serverIp = SearchConditionUtil.optimizeCondition(param.get("serverIp"));
            Integer status = Integer.parseInt(param.get("status"));
            Integer count = monitorStatisticsDao.queryMonitorStatisticsCounts(serverIp,status);
            List<MonitorStatisticsDomain> list = monitorStatisticsDao.queryMonitorStatistics(start, length, serverIp,status);
            return new Table(list, count);
        }
        catch (Exception e)
        {
            LOG.error("Query MonitorConfig Error. ", e);
            return new Table();
        }
    }
}

package com.axon.market.dao.mapper.ishop;

import com.axon.market.dao.base.IMyBatisMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 炒店任务KPI统计
 * Created by zengcr on 2017/3/24.
 */
@Component("shopTaskKPIDao")
public interface IShopTaskKPIMapper extends IMyBatisMapper
{
    List<Map<String, Object>> queryShopTaskKPI(Map<String, Object> parasMap);

    List<Map<String,Object>> queryBusinessHallKPI(Map<String,Object> param);

    Map<String,Object> queryBusinessHallCount(Map<String,Object> param);
}

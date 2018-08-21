package com.axon.market.core.service.ishop;

import com.axon.market.dao.mapper.ishop.IShopTaskKPIMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 炒店KPI指标分析统计
 * Created by zengcr on 2017/3/24.
 */
@Component("shopTaskKPIService")
public class ShopTaskKPIService
{
    private static final Logger LOG = Logger.getLogger(ShopTaskKPIService.class.getName());

    @Autowired
    @Qualifier("shopTaskKPIDao")
    private IShopTaskKPIMapper iShopTaskKPIMapper;

    /**
     * 统计炒店KPI指标
     * @param parasMap
     * @return
     */
    public List<Map<String,Object>> queryShopTaskKPI(Map<String,Object> parasMap)
    {
        return iShopTaskKPIMapper.queryShopTaskKPI(parasMap);
    }

    public List<Map<String, Object>> queryBusinessHallKPI(Map<String, Object> param)
    {
        return iShopTaskKPIMapper.queryBusinessHallKPI(param);
    }


    public Map<String, Object> queryBusinessHallCount(Map<String, Object> param)
    {
        return iShopTaskKPIMapper.queryBusinessHallCount(param);
    }

}

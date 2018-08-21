package com.axon.market.core.service.istatistics;

import com.axon.market.common.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import com.axon.market.common.domain.iStatistics.ProductOrderStatisticsDomain;
import com.axon.market.dao.mapper.istatistics.IProductOrderStatisticsMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chris on 2017/7/24.
 */
@Service("productOrderStatisticsService")
public class ProductOrderStatisticsService
{
    @Autowired
    @Qualifier("IProductOrderStatisticsDao")
    private IProductOrderStatisticsMapper ProductOrderStatisticsDao;

    public int queryProductOrderStatisticsCounts(String startTime, String endTime, String city)
    {

        return ProductOrderStatisticsDao.queryProductOrderStatisticsCounts(startTime,endTime,city);
    }



    public List<Map<String,Object>> queryProductOrderStatisticsByPage(String startTime, String endTime, String city, Integer offset, Integer limit)
    {

        return ProductOrderStatisticsDao.queryProductOrderStatisticsByPage(startTime, endTime, city, limit, offset);
    }

    public String queryMarketAreas(String areaIds)
    {
        List<ProductOrderStatisticsDomain> marketAreas = ProductOrderStatisticsDao.queryMarketAreas(areaIds);
        try
        {
            return JsonUtil.objectToString(marketAreas);
        }
        catch (JsonProcessingException e)
        {
        }
        return null;
    }



}

package com.axon.market.dao.mapper.istatistics;

import com.axon.market.common.domain.iStatistics.ProductOrderStatisticsDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by Chris on 2017/7/24.
 */
@Component("IProductOrderStatisticsDao")
public interface IProductOrderStatisticsMapper extends IMyBatisMapper
{
//    List<Map<String,Object>> queryMaintainWorkBulletinOne(@Param(value = "yearMonth") String yearMonth);

    int queryProductOrderStatisticsCounts (@Param(value = "startTime")String startTime,@Param(value = "endTime")String endTime,@Param(value = "city")String city);

    List<Map<String,Object>> queryProductOrderStatisticsByPage(@Param(value = "startTime")String startTime,@Param(value = "endTime")String endTime,@Param(value = "city")String city,@Param("limit") int limit, @Param("offset") int offset);

    /**
     * @param areaIds
     * @return
     */
    List<ProductOrderStatisticsDomain> queryMarketAreas(@Param(value = "areaIds") String areaIds);
}

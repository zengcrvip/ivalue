package com.axon.market.dao.mapper.ishop;

import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by wangtt on 2017/6/3.
 */
@Component("shopTaskMonitorDao")
public interface IShopTaskMonitorMapper extends IMyBatisMapper
{
    int queryShopTaskMonitorCount(@Param("startTime") String startTime,@Param("endTime") String endTime);

    List<Map<String,Object>>  queryShopTaskMonitor(@Param("startTime") String startTime,
                                                   @Param("endTime") String endTime,
                                                   @Param("offset") Integer offset,
                                                   @Param("limit") Integer limit);

}

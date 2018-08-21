package com.axon.market.dao.mapper.ishop;

import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by wangtt on 2017/6/15.
 */
@Component("JXHTaskStatisticsDao")
public interface IJXHTaskStatisticsMapper extends IMyBatisMapper
{
    List<Map<String,Object>> queryJXHTaskStatistics(Map<String,String> param);

    int queryJXHTaskCount(Map<String,String> param);

}

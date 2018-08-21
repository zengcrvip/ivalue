package com.axon.market.dao.mapper.istatistics;

import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by Chris on 2017/7/25.
 */
@Component("MarketAnalysisDao")
public interface IMarketAnalysisMapper extends IMyBatisMapper
{

    int querySmsAnalysisCount(@Param(value = "startTime") Long startTime, @Param(value = "endTime") Long endTime, @Param(value = "taskName") String taskName);


    List<Map<String, String>> queryAllSmsAnalysisByPage(@Param(value = "offset") Long offset, @Param(value = "maxRecord") Long maxRecord, @Param(value = "startTime") Long startTime, @Param(value = "endTime") Long endTime, @Param(value = "taskName") String taskName);


    int queryAllMarketUserDetailsCounts(@Param(value="taskId") String taskId);


     List<Map<String, String>> queryMarketUserDetailsByPage(@Param(value = "offset") Long offset, @Param(value = "maxRecord") Long maxRecord, @Param(value="taskId") String taskId);

}


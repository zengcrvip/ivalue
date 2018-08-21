package com.axon.market.dao.mapper.istatistics;

import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by wangtt on 2017/4/26.
 */
@Component("analysisFlowDao")
public interface IAnalysisFlowMapper extends IMyBatisMapper
{
    List<Map<String,String>> queryAnalysisFlowRate(@Param("yearMonth") String yearMonth);

    List<Map<String,String>> queryAnalysisFlowGrow(@Param("dateRange") String dateRange);

    List<Map<String,String>> queryAnalysisFlow(@Param("yearMonth") String yearMonth);

    List<Map<String,String>> queryProportionOfCityUser(@Param("yearMonth") String yearMonth);

    List<Map<String,String>> queryProportionOfPackage(@Param("yearMonth") String yearMonth);

    List<Map<String,Object>> queryBehaviorPreferences(@Param("yearMonth") String yearMonth);

    List<Map<String,Object>> queryStockUserDaily(@Param("tableName") String tableName,@Param("dailyMonths") String dailyMonths);

    List<Map<String,Object>> queryBaseMonthlyReservedDataResult(@Param("yearMonth") String yearMonth,@Param("cityName") String cityName);

    List<Map<String,Object>> queryBaseMonthlyReservedDataProcess(@Param("yearMonth") String yearMonth,@Param("cityName") String cityName);

    List<Map<String,Object>> downloadBaseMonthlyReserved(@Param("yearMonth") String yearMonth,@Param("cityName") String cityName);

    List<Map<String,Object>> downloadMonthlyReserved(@Param("yearMonth") String yearMonth);

    List<Map<String,String>> queryStockUserIncomeData();
}
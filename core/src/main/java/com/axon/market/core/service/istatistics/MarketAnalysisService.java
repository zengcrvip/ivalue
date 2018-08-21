package com.axon.market.core.service.istatistics;

import com.axon.market.common.util.AxonEncryptUtil;
import com.axon.market.dao.mapper.istatistics.IMarketAnalysisMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Chris on 2017/7/25.
 */
@Service("MarketAnalysisService")
public class MarketAnalysisService
{
    @Autowired
    @Qualifier("MarketAnalysisDao")
    private IMarketAnalysisMapper MarketAnalysisDao;

    private  static final Logger LOG = Logger.getLogger(MarketAnalysisService.class.getName());

    public int queryAllMarketAnalysisCounts(Map<String, Object> option) throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startExeTime = (String) option.get("startExeTime");
        String endExeTime = (String) option.get("endExeTime");
        String taskName = (String) option.get("taskName");
        Long startTime=null;
        Long endTime=null;

        if (StringUtils.isNotEmpty(startExeTime))
        {
            startTime = sdf.parse(startExeTime).getTime() / 1000;
        }
        if (StringUtils.isNotEmpty(endExeTime))
        {
            endTime = sdf.parse(endExeTime).getTime() / 1000;
        }
        try
        {
            return MarketAnalysisDao.querySmsAnalysisCount(startTime,endTime,taskName);
        }
        catch (Exception e)
        {
            LOG.error("", e);
        }
        return 0;
    }

    //region 老方法注释
//    public List<Map<String, String>> queryAllMarketAnalysisByPage(long offset, long maxRecord, String sortColumn, Map<String, Object> option) throws ParseException
//    {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String startExeTime = (String) option.get("startExeTime");
//        String endExeTime = (String) option.get("endExeTime");
//        String taskName = (String) option.get("taskName");
//        Long startTime=null;
//        Long endTime=null;
//
//        if (StringUtils.isNotEmpty(startExeTime))
//        {
//            startTime = sdf.parse(startExeTime).getTime() / 1000;
//        }
//        if (StringUtils.isNotEmpty(endExeTime))
//        {
//            endTime = sdf.parse(endExeTime).getTime() / 1000;
//        }
//        try
//        {
//            return MarketAnalysisDao.queryAllSmsAnalysisByPage(offset, maxRecord, sortColumn, startTime, endTime, taskName);
//
//        }
//        catch (Exception e)
//        {
//            LOG.error("", e);
//        }
//        return Collections.emptyList();
//    }
//endregion

    /**
     * 分页获取营销任务
     * @param offset
     * @param maxRecord
     * @param option
     * @return
     * @throws ParseException
     */
    public List<Map<String,String>> queryAllMarketAnalysisByPage(long offset, long maxRecord, Map<String, Object> option) throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startExeTime = (String) option.get("startExeTime");
        String endExeTime = (String) option.get("endExeTime");
        String taskName = (String) option.get("taskName");
        Long startTime=null;
        Long endTime=null;

        if (StringUtils.isNotEmpty(startExeTime))
        {
            startTime = sdf.parse(startExeTime).getTime() / 1000;
        }
        if (StringUtils.isNotEmpty(endExeTime))
        {
            endTime = sdf.parse(endExeTime).getTime() / 1000;
        }
        try
        {
            return MarketAnalysisDao.queryAllSmsAnalysisByPage(offset, maxRecord, startTime, endTime, taskName);
        }
        catch (Exception e)
        {
            LOG.error("", e);
        }
        return Collections.emptyList();
    }

    /**
     * 获取用户清单中用户总数
     * @param taskId
     * @return
     */
    public int queryAllMarketUserDetailsCounts(String taskId)
    {
        return MarketAnalysisDao.queryAllMarketUserDetailsCounts(taskId);
    }

    /**
     * 分页获取营销任务用户清单
     * @param offset
     * @param maxRecord
     * @param taskId
     * @return
     */
    public List<Map<String, String>> queryMarketUserDetailsByPage(long offset, long maxRecord, String taskId)
    {
        List<Map<String, String>> results = MarketAnalysisDao.queryMarketUserDetailsByPage(offset, maxRecord, taskId);
        if(CollectionUtils.isNotEmpty(results))
        {
            for(Map<String, String> result : results)
            {
                result.put("phone", AxonEncryptUtil.getInstance().decrypt(String.valueOf(result.get("phone"))));
            }
            return results;
        }
        return Collections.emptyList();
    }
}

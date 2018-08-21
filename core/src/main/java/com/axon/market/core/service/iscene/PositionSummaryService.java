package com.axon.market.core.service.iscene;

import com.axon.market.dao.mapper.ishop.IShopTaskSummaryMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 地市任务汇总统计service
 * Created by zengcr on 2016/12/13.
 */
@Component("positionSummaryService")
public class PositionSummaryService
{
    private static final Logger LOG = Logger.getLogger(PositionSummaryService.class.getName());

    @Autowired
    @Qualifier("shopTaskSummaryDao")
    private IShopTaskSummaryMapper shopTaskSummaryDao;

    /**
     * 查询地市任务汇总统计
     *
     * @param paras，时间，日报或月报标识
     * @return
     */
    public List<Map<String, Object>> queryPositionSummary(Map<String, Object> paras)
    {
        try
        {
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            if ("month".equals((String) paras.get("flag")))
            {
                //月报
                result = shopTaskSummaryDao.queryPositionSummaryByMonth(paras);
            }
            else if ("day".equals((String) paras.get("flag")))
            {
                //日报
                result = shopTaskSummaryDao.queryPositionSummaryByDay(paras);
            }
            return result;
        }
        catch (Exception e)
        {
            LOG.error("queryPositionSummary Error. ", e);
            return null;
        }
    }
}

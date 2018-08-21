package com.axon.market.core.service.iscene;

import com.axon.market.dao.mapper.ishop.IShopTaskReportMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * 位置场景统计Service
 * Created by zengcr on 2016/12/12.
 */
@Component("positionReportService")
public class PositionReportService
{
    private static final Logger LOG = Logger.getLogger(PositionReportService.class.getName());

    @Autowired
    @Qualifier("shopTaskReportDao")
    private IShopTaskReportMapper shopTaskReportDao;

    /**
     * 位置场景统计分页总数
     *
     * @param paras
     * @return
     */
    public int queryPositionReportTotal(Map<String, Object> paras)
    {
        try
        {
            return shopTaskReportDao.queryPositionReportTotal(paras);
        }
        catch (Exception e)
        {
            LOG.error("queryPositionReportTotal Error. ", e);
            return 0;
        }
    }

    /**
     * 位置场景统计分页列表
     *
     * @param paras
     * @return
     */
    public List<Map<String, Object>> queryPositionSceneByPage(Map<String, Object> paras)
    {
        try
        {
            return shopTaskReportDao.queryPositionReortByPage(paras);
        }
        catch (Exception e)
        {
            LOG.error("queryPositionSceneByPage Error. ", e);
            return null;
        }
    }

    /**
     * 查询任务统计,execl导出统计
     *
     * @param paras
     * @return
     */
    public List<Map<String, Object>> queryPositionReport(Map<String, Object> paras)
    {
        try
        {
            return shopTaskReportDao.queryPositionReport(paras);
        }
        catch (Exception e)
        {
            LOG.error("queryPositionReport Error. ", e);
            return null;
        }
    }
}

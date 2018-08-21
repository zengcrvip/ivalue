package com.axon.market.core.task;

import com.axon.market.common.domain.ischeduling.MarketingPoolTaskDomain;
import com.axon.market.common.timer.RunJob;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import com.axon.market.core.service.ischeduling.MarketingTaskPoolService;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by yuanfei on 2017/6/8.
 */
public class RefreshMarketingPoolTasksCountTask extends RunJob
{
    private static final Logger LOG = Logger.getLogger(RefreshMarketingPoolTasksCountTask.class.getName());

    private MarketingTaskPoolService marketingTaskPoolService = MarketingTaskPoolService.getInstance();

    private GreenPlumOperateService greenPlumOperateService = GreenPlumOperateService.getInstance();

    @Override
    public void runBody()
    {
        LOG.info("Refresh MarketingTasksCount Of Pool Start");
        try
        {
            List<MarketingPoolTaskDomain> marketingTasksPoolList = marketingTaskPoolService.queryTodayNormalMarketingPoolTasks();
            for (MarketingPoolTaskDomain marketingPoolTask : marketingTasksPoolList)
            {
                // 分批营销 0：否；1：是
                String sql = "";
                if (marketingPoolTask.getIsBoidSale() != null && marketingPoolTask.getIsBoidSale() == 0)
                {
                    String modelIds = marketingPoolTask.getMarketSegmentIds();
                    String[] modelIdArray = modelIds.split(",");
                    // 生成客户群数据sql(踢重)
                    for (int i=0; i<modelIdArray.length; i++)
                    {
                        sql += (i==0 ? "" : " union") + " select * from " + greenPlumOperateService.getModelDataTableName(modelIdArray[i]);
                    }
                    sql = "select count(0) from (" + sql + ") as modelTable";
                }
                else if (marketingPoolTask.getIsBoidSale() != null && marketingPoolTask.getIsBoidSale() == 1)
                {
                    String taskTableName = "task.task_" + marketingPoolTask.getId();
                    sql = "select count(0) from " + taskTableName + "where send_date = " + marketingPoolTask.getDate();
                }
                else
                {
                    continue;
                }

                int totalCount = greenPlumOperateService.queryRecordCount(sql);
                marketingTaskPoolService.updateMarketingPoolTaskTargetCount(marketingPoolTask.getId(), totalCount);
            }

        }
        catch (Exception e)
        {
            LOG.error("Refresh MarketingTasksCount Of Pool Fail", e);
        }

        LOG.info("Refresh MarketingTasksCount Of Pool End");
    }
}

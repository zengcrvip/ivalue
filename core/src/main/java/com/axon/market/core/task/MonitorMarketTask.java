package com.axon.market.core.task;

import com.axon.market.common.constant.ischeduling.ShopTaskStatusEnum;
import com.axon.market.common.domain.ischeduling.MarketingPoolTaskDomain;
import com.axon.market.common.domain.ischeduling.MarketingTasksDomain;
import com.axon.market.common.timer.RunJob;
import com.axon.market.common.util.MarketTaskThreadPoolUtil;
import com.axon.market.common.util.MarketTimeUtils;
import com.axon.market.common.util.ThreadPoolUtil;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.service.ischeduling.MarketingTasksService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 实时监控已执行的营销任务
 * Created by zengcr on 2017/6/12.
 */
public class MonitorMarketTask extends RunJob
{
    private static final Logger LOG = Logger.getLogger(MonitorMarketTask.class.getName());

    private MarketingTasksService marketingTasksService = MarketingTasksService.getInstance();


    @Override
    public void runBody()
    {
        LOG.info("market task is begin...........................................");
        // 查询审批通过待处理的单子 + 处理失败的单子重复处理5次以内的单子
        List<MarketingPoolTaskDomain> list = marketingTasksService.queryAllWaitingExecuteMarketTask(TimeUtil.formatDateToYMD(new Date()));
        LOG.info("marketJob task 待执行的营销任务任务数：" + list.size());

        try
        {
            for (MarketingPoolTaskDomain tasksDomain : list)
            {
                //判断是否在执行范围内
                try
                {
                    long currentTime = System.currentTimeMillis();
                    String startTimeStr = tasksDomain.getStartTime() + " " +  tasksDomain.getBeginTime() +":00";
                    String endTimeStr = tasksDomain.getStopTime() + " " + tasksDomain.getEndTime() +":00";
                    long startTime = MarketTimeUtils.formatDate(startTimeStr).getTime();
                    long endTime = MarketTimeUtils.formatDate(endTimeStr).getTime();

                    if (currentTime < startTime || currentTime > endTime)
                    {
                        LOG.info("任务不在有效期内，暂无法执行 " + tasksDomain.getId());
                        continue;
                    }
                }
                catch (ParseException e)
                {
                    LOG.error("", e);
                }

                //记录营销记录
                Integer taskStatus = tasksDomain.getStatus();
                //执行前先修改任务状态
                marketingTasksService.updateShopTaskExecuteBySystemId(tasksDomain.getId(), ShopTaskStatusEnum.TASK_MARKET_SEND.getValue());
                String marketType = tasksDomain.getMarketType();

                if (marketType != null && !"".equals(marketType))
                {
                    MarketTaskThreadPoolUtil.submit(new MarketTaskDispatch(tasksDomain, marketType));
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

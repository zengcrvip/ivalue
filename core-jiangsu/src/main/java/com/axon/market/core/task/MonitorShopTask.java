package com.axon.market.core.task;

import com.axon.market.common.constant.ischeduling.ShopTaskStatusEnum;
import com.axon.market.common.domain.ishop.ShopTaskDomain;
import com.axon.market.common.timer.RunJob;
import com.axon.market.common.util.MarketTimeUtils;
import com.axon.market.common.util.ThreadPoolUtil;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.service.ishop.ShopTaskService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 实时监控待处理的炒店任务
 * Created by zengcr on 2017/3/7.
 */
public class MonitorShopTask extends RunJob
{
    private static final Logger LOG = Logger.getLogger(MonitorShopTask.class.getName());

    private ShopTaskService shopTaskService = ShopTaskService.getInstance();

    private static final Integer PER_TASK = 1;  //常驻任务类型

    private static final Integer FLOW_TASK = 2; //流动任务类型

    private static final Integer PER_AND_FLOW_TASK = 3; //常驻+流动任务类型

    @Override
    public void runBody()
    {
        //早上6点之前或晚上18点之后不再执行
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if(hour < 6 || hour > 18)
        {
            return;
        }

        LOG.info("shop task is begin...........................................");
        // 查询审批通过待处理的单子 + 处理失败的单子重复处理5次以内的单子
        List<ShopTaskDomain> list = shopTaskService.queryAllWaitingExecuteShopTask(TimeUtil.formatDateToYMD(new Date()));
        LOG.info("shop task 待执行的炒店任务数：" + list.size());

        try
        {
            for (ShopTaskDomain shopTaskDomain : list)
            {
                //判断是否在执行范围内
                try
                {
                    long currentTime = System.currentTimeMillis();
                    long startTime = MarketTimeUtils.formatDate(shopTaskDomain.getStartTime() + " 00:00:00").getTime();
                    long endTime = MarketTimeUtils.formatDate(shopTaskDomain.getStopTime() + " 23:59:00").getTime();

                    if (currentTime < startTime || currentTime > endTime)
                    {
                        LOG.error("任务不在有效期内，暂无法执行 " + shopTaskDomain.getId());
                    }
                }
                catch (ParseException e)
                {
                    LOG.error("", e);
                }

                //记录营销记录
                Integer taskStatus = shopTaskDomain.getStatus();
                //执行前先修改任务状态
                shopTaskService.updateShopTaskExecuteBySystemId(shopTaskDomain.getId(), shopTaskDomain.getBaseId(), ShopTaskStatusEnum.TASK_MARKET_SEND.getValue());
                Integer marketUserType = shopTaskDomain.getMarketUser();
                if (marketUserType == PER_AND_FLOW_TASK)
                {
                    //两种场景都营销失败的单子 + 审批通过待处理的单子 + 暂停的单子
                    if (taskStatus == ShopTaskStatusEnum.TASK_MARKET_ALL_FAIL.getValue() || taskStatus == 30 || taskStatus == ShopTaskStatusEnum.TASK_PAUSE.getValue())
                    {
                        ThreadPoolUtil.submit(new ShopTaskExecutor(shopTaskDomain, PER_TASK));
                        ThreadPoolUtil.submit(new ShopTaskExecutor(shopTaskDomain, FLOW_TASK));
                    }
                    else if (taskStatus == ShopTaskStatusEnum.TASK_MARKET_ALL_PER_SUCCESS.getValue() || taskStatus == ShopTaskStatusEnum.TASK_MARKET_PER_FAIL.getValue())
                    {
                        //常驻用户营销失败
                        ThreadPoolUtil.submit(new ShopTaskExecutor(shopTaskDomain, PER_TASK));
                    }
                    else if (taskStatus == ShopTaskStatusEnum.TASK_MARKET_ALL_FLOW_SUCCESS.getValue() || taskStatus == ShopTaskStatusEnum.TASK_MARKET_FLOW_FAIL.getValue())
                    {
                        //流动用户营销失败
                        ThreadPoolUtil.submit(new ShopTaskExecutor(shopTaskDomain, FLOW_TASK));
                    }
                }
                else
                {
                    ThreadPoolUtil.submit(new ShopTaskExecutor(shopTaskDomain, marketUserType));
                }

                // 单独执行指定用户
                String appointUsers = shopTaskDomain.getAppointUsers();
                if (StringUtils.isNotEmpty(appointUsers))
                {
                    ThreadPoolUtil.submit(new ShopTaskExecutor(shopTaskDomain, 5));
                }

            }
        }
        catch (Exception e)
        {
            LOG.error("shop task push ThreadPool error",e);
        }
    }
}

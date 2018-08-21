package com.axon.market.core.task;

import com.axon.market.common.constant.ischeduling.ShopTaskStatusEnum;
import com.axon.market.common.domain.ischeduling.MarketingPoolTaskDomain;
import com.axon.market.core.marketingtask.MarketTaskExecutor;
import com.axon.market.core.marketingtask.MarketTaskFactory;
import com.axon.market.core.service.ischeduling.MarketingTasksService;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * 营销任务执行线程
 * Created by zengcr on 2017/6/12.
 */
public class MarketTaskDispatch implements Runnable
{
    private static final Logger LOG = Logger.getLogger(MarketTaskDispatch.class.getName());

    private MarketingTasksService marketingTasksService =  MarketingTasksService.getInstance();

    private MarketingPoolTaskDomain tasksDomain;

    private String marketType;

    public MarketTaskDispatch(MarketingPoolTaskDomain tasksDomain, String marketType)
    {
        this.tasksDomain = tasksDomain;
        this.marketType = marketType;
    }

    public String getMarketType()
    {
        return marketType;
    }

    @Override
    public void run()
    {
        boolean isSendSuccess = true;
        String lastTaskId = null;
        Integer taskStatus = null;
        Integer marketNum  = null;
        MarketTaskFactory factoty = new MarketTaskFactory();
        try
        {
            LOG.info("shop task " + tasksDomain.getId() + "---" + tasksDomain.getName() + " 执行中，执行的任务类型：" + marketType);
            //执行营销
            MarketTaskExecutor taskExecutor = factoty.createMarketTask(marketType);
            String results = taskExecutor.execute(tasksDomain);

            if (results == null)
            {
                //返回为空，营销失败
                isSendSuccess = false;
            }
            else if ("WAIT".equals(results))
            {
                //精细化周期任务，用户数据未接收到时，返回null，重复处理
                // 返回结果为WAIT时，则对于精细化周期任务，表明用户数据未接收到时，需要重复处理，对于批量短信push，表明分批任务表还未创建，需要重复执行
                marketingTasksService.updateShopTaskExecuteBySystemId(tasksDomain.getId(), ShopTaskStatusEnum.TASK_MARKET_FOR_DEAL.getValue());
                return;
            }
            else
            {
                String[] result = results.split("-");
                marketNum = Integer.parseInt(result[0]); //获取发送的营销用户数
                lastTaskId = result[1];                  //获取对应的任务ID
            }
        }
        catch (Exception e)
        {
            isSendSuccess = false;
            LOG.error("Manual shop task error : " + tasksDomain.getId(), e);
        }

        //营销失败修改状态
        if(!isSendSuccess)
        {
            taskStatus = ShopTaskStatusEnum.TASK_MARKET_FAIL.getValue();
        }
        else
        {
            taskStatus = ShopTaskStatusEnum.TASK_MARKET_FINISHED.getValue();
        }

        //修改任务的执行次数+状态+执行用户数+对应的任务ID
        Map<String, Object> paras = new HashMap<String, Object>();
        paras.put("taskId", tasksDomain.getId());
        paras.put("lastTaskId", lastTaskId);
        paras.put("taskStatus", taskStatus);
        paras.put("marketNum", marketNum);
        marketingTasksService.updateMarketTaskExecuteById(paras);
    }
}

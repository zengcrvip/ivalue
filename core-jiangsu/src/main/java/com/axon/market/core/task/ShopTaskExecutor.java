package com.axon.market.core.task;

import com.axon.market.common.domain.ishop.ShopTaskDomain;
import com.axon.market.core.service.ishop.ShopTaskService;
import com.axon.market.core.shoptask.ShopTaskFactory;
import com.axon.market.core.shoptask.TaskExecutor;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * 炒店任务执行线程
 * Created by zengcr on 2017/3/11.
 */
public class ShopTaskExecutor implements Runnable
{
    private static final Logger LOG = Logger.getLogger(ShopTaskExecutor.class.getName());

    private ShopTaskService shopTaskService = ShopTaskService.getInstance();

    private ShopTaskDomain shopTaskDomain;

    private Integer taskType;

    public ShopTaskExecutor(ShopTaskDomain shopTaskDomain, Integer taskType)
    {
        this.shopTaskDomain = shopTaskDomain;
        this.taskType = taskType;
    }

    public Integer getTaskType()
    {
        return taskType;
    }

    @Override
    public void run()
    {
        boolean isSendSuccess = true;
        Integer pTaskId = null;
        Integer smsTaskId = null;
        String taskStatus = null;
        ShopTaskFactory factoty = new ShopTaskFactory();
        try
        {
            LOG.info("shop task " + shopTaskDomain.getId() + "---" + shopTaskDomain.getTaskName() + " 执行中，执行的任务类型：" + taskType);
            //执行营销
            TaskExecutor taskExecutor = factoty.createShopTask(taskType);
            String results = taskExecutor.execute(shopTaskDomain);

            if (taskType == 5 || taskType == 6 || taskType == 7)
            {
                return;
            }

            if (results == null)
            {
                isSendSuccess = false;
            }
            else
            {
                String[] result = results.split("-");
                if (taskType == 1)
                {
                    //短信push营销
                    smsTaskId = Integer.parseInt(result[1]);
                }
                else if (taskType == 2)
                {
                    //实时营销
                    pTaskId = Integer.parseInt(result[1]);
                }
            }
        }
        catch (Exception e)
        {
            isSendSuccess = false;
            LOG.error("Manual shop task error : " + shopTaskDomain.getId(), e);
        }

        //个性化推荐处理失败，返回
        if (taskType == 4)
        {
            shopTaskService.updateShopTaskExecuteBySystemId(shopTaskDomain.getId(), shopTaskDomain.getBaseId(), 30);
            return;
        }

        if (!isSendSuccess)
        {
            taskStatus = "fail";
        }
        else
        {
            taskStatus = "success";
        }

        Integer flag = null;
        if (taskType == 1)
        {
            if ("success".equals(taskStatus))
            {
                flag = 1;
            }
            else
            {
                flag = 2;
            }
        }
        else
        {
            if ("success".equals(taskStatus))
            {
                flag = 3;
            }
            else
            {
                flag = 4;
            }
        }

        Map<String, Object> paras = new HashMap<String, Object>();
        paras.put("taskId", shopTaskDomain.getId());
        paras.put("flag", flag);
        paras.put("pTaskId", pTaskId);
        paras.put("smsTaskId", smsTaskId);
        paras.put("baseId", shopTaskDomain.getBaseId());
        shopTaskService.updateShopTaskExecuteById(paras);
    }
}

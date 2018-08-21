package com.axon.market.core.service.ischeduling;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.constant.ischeduling.ShopTaskStatusEnum;
import com.axon.market.common.domain.ischeduling.MarketingPoolTaskDomain;
import com.axon.market.common.domain.ischeduling.MarketingTasksDomain;
import com.axon.market.common.util.MarketTimeUtils;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import com.axon.market.dao.mapper.ischeduling.IMarketingTaskPoolMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by yuanfei on 2017/6/8.
 */
@Service("marketingTaskPoolService")
public class MarketingTaskPoolService
{
    private static final Logger LOG = Logger.getLogger(MarketingTaskPoolService.class.getName());

    @Autowired
    @Qualifier("marketingTaskPoolDao")
    private IMarketingTaskPoolMapper marketingTaskPoolDao;

    @Autowired
    @Qualifier("marketingTasksService")
    private MarketingTasksService marketingTasksService;

    @Autowired
    @Qualifier("greenPlumOperateService")
    private GreenPlumOperateService greenPlumOperateService;

    @Autowired
    @Qualifier("marketHistoryService")
    private MarketHistoryService marketHistoryService;

    public static MarketingTaskPoolService getInstance()
    {
        return (MarketingTaskPoolService) SpringUtil.getSingletonBean("marketingTaskPoolService");
    }

    /**
     * @see IMarketingTaskPoolMapper#queryMarketingPoolTasksCount(Map)
     * @param condition
     * @return
     */
    public int queryMarketingPoolTasksCount(Map<String,Object> condition)
    {
        return  marketingTaskPoolDao.queryMarketingPoolTasksCount(condition);
    }

    /**
     * @see IMarketingTaskPoolMapper#queryMarketingPoolTasksByPage(long, long, Map)
     * @param offset
     * @param maxRecord
     * @param condition
     * @return
     */
    public List<MarketingPoolTaskDomain> queryMarketingPoolTasksByPage(long offset, long maxRecord, Map<String,Object> condition)
    {
        return marketingTaskPoolDao.queryMarketingPoolTasksByPage(offset, maxRecord, condition);
    }

    /**
     * 获取当天任务池非精细化的任务
     * @return
     */
    public List<MarketingPoolTaskDomain> queryTodayNormalMarketingPoolTasks()
    {
        return marketingTaskPoolDao.queryTodayNormalMarketingPoolTasks();
    }

    /**
     * 根据id查询当天任务池的任务
     * @param id
     * @return
     */
    public MarketingPoolTaskDomain queryMarketingPoolTask(Integer id)
    {
        return marketingTaskPoolDao.queryMarketingPoolTaskById(id);
    }

    /**
     * @see IMarketingTaskPoolMapper#batchInsertMarketingTasksToPool()
     * 定时线程调用：将当天任务信息批量插入营销任务任务池
     */
    @Transactional
    public void batchHandleMarketingTasksToPool ()
    {
        // 执行营销任务进入营销任务任务池
        int count = marketingTaskPoolDao.batchInsertMarketingTasksToPool();
        LOG.info(count + " MarketingTasks To Pool Success.");

        // 修改已经进入任务池的营销任务的下一次营销时间
        int updateCount = marketingTasksService.batchUpdateMarketingTasksNextMarketTime();
        LOG.info(updateCount + " MarketingTasks Update.");
    }


    /**
     * @see IMarketingTaskPoolMapper#insertMarketingTaskToPool(MarketingTasksDomain)
     * @param taskDomain
     * @return
     */
    public ServiceResult insertMarketingTaskToPool(MarketingTasksDomain taskDomain)
    {
        ServiceResult result = new ServiceResult();
        if (1 != marketingTaskPoolDao.insertMarketingTaskToPool(taskDomain))
        {
            result.setRetValue(-1);
            result.setDesc("数据库插入任务失败");
        }
        return result;
    }

    /**
     * @see IMarketingTaskPoolMapper#updateMarketingPoolTaskStatus(Integer, Integer)
     * @param taskId
     * @param status
     * @return
     */
    public int updateMarketingPoolTaskStatus(Integer taskId, Integer status)
    {
        return marketingTaskPoolDao.updateMarketingPoolTaskStatus(taskId, status);
    }

    /**
     *
     * @see IMarketingTaskPoolMapper#updateMarketingPoolTaskTargetCount(Integer, Integer)
     * @param taskId
     * @param targetNum
     * @return
     */
    public int updateMarketingPoolTaskTargetCount(Integer taskId, Integer targetNum)
    {
        return marketingTaskPoolDao.updateMarketingPoolTaskTargetCount(taskId, targetNum);
    }

    /**
     * 执行任务池营销任务
     * @param taskId
     * @param userName
     * @return
     */
    public ServiceResult executeMarketingTask(Integer taskId,String userName)
    {
        ServiceResult result = new ServiceResult();
        final MarketingPoolTaskDomain marketingPoolTaskDomain = marketingTaskPoolDao.queryMarketingPoolTaskById(taskId);

        try
        {
            long currentTime = System.currentTimeMillis();
            long startTime = MarketTimeUtils.formatDate(marketingPoolTaskDomain.getStartTime() + " 00:00:00").getTime();
            long endTime = MarketTimeUtils.formatDate(marketingPoolTaskDomain.getStopTime() + " 23:59:59").getTime();

            if (currentTime <= startTime || currentTime > endTime)
            {
                result.setRetValue(-1);
                result.setDesc("任务不在有效期内，暂无法执行");
                return result;
            }
        }
        catch (ParseException e)
        {
            LOG.error("", e);
            result.setRetValue(-1);
            result.setDesc("系统错误");
            return result;
        }

        if (ShopTaskStatusEnum.TASK_PAUSE.getValue().equals(marketingPoolTaskDomain.getStatus()))
        {
            result.setRetValue(-1);
            result.setDesc("该任务已暂停");
            return result;
        }

        // 修改状态为待处理
        marketingTaskPoolDao.updateMarketingPoolTaskStatus(taskId, ShopTaskStatusEnum.TASK_MARKET_FOR_DEAL.getValue());

        //如果是自动任务，修改market_jobs的状态
        if("single".equals(marketingPoolTaskDomain.getScheduleType()))
        {
            marketingTasksService.updateMarketingTaskStatus(taskId,ShopTaskStatusEnum.TASK_EXECUTE.getValue());
        }

        // 执行任务时，修改任务状态为30，等待任务执行线程扫描发送
        result.setDesc("任务执行请求发送成功！");
        return result;
    }

    /**
     * 刷新精细化周期任务每天的用户数据
     */
    public void refreshTaskUserData()
    {
        List<MarketingPoolTaskDomain> jxhTasks = marketingTaskPoolDao.queryJXHMarketingPoolTasks();
        for(MarketingPoolTaskDomain marketingPoolTaskDomain : jxhTasks)
        {
            String saleId = marketingPoolTaskDomain.getSaleId();
            String saleBoidId =  marketingPoolTaskDomain.getSaleBoidId();
            String aimSubId = marketingPoolTaskDomain.getAimSubId();
            String sql = "select count(*) from shop.icloud_user where sale_id = '" + saleId + "' and sale_boid_id = '" + saleBoidId + "' and aim_sub_id = '" + aimSubId + "'";
            int targetNum = greenPlumOperateService.queryRecordCount(sql);
            if(targetNum > 0){
                marketingTaskPoolDao.updateMarketingPoolTaskCountByBaseId(saleId,saleBoidId,aimSubId,targetNum);
            }
        }
    }




}

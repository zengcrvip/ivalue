package com.axon.market.dao.mapper.ischeduling;

import com.axon.market.common.domain.ischeduling.MarketingPoolTaskDomain;
import com.axon.market.common.domain.ischeduling.MarketingTasksDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by yuanfei on 2017/6/8.
 */
@Component("marketingTaskPoolDao")
public interface IMarketingTaskPoolMapper extends IMyBatisMapper
{
    /**
     * 查询任务池所有数目
     * @param condition
     * @return
     */
    int queryMarketingPoolTasksCount(@Param(value = "condition") Map<String,Object> condition);

    /**
     * 分页查询任务池数据
     * @param offset
     * @param maxRecord
     * @param condition
     * @return
     */
    List<MarketingPoolTaskDomain> queryMarketingPoolTasksByPage(@Param(value = "offset") long offset, @Param(value = "maxRecord") long maxRecord, @Param(value = "condition") Map<String,Object> condition);

    /**
     * 获取当天任务池非精细化任务的任务
     * @return
     */
    List<MarketingPoolTaskDomain> queryTodayNormalMarketingPoolTasks();

    /**
     * 根据taskId查询任务池任务
     * @param taskId
     * @return
     */
    MarketingPoolTaskDomain queryMarketingPoolTaskById(@Param(value = "taskId") Integer taskId);

    /**
     * 批量生成任务到任务池
     * @return
     */
    int batchInsertMarketingTasksToPool();

    /**
     * 单条任务插入到营销任务任务池
     * @param taskDomain
     * @return
     */
    int insertMarketingTaskToPool(@Param(value = "task") MarketingTasksDomain taskDomain);

    /**
     * 更新任务池的营销任务
     * @param taskId
     * @param status
     * @return
     */
    int updateMarketingPoolTaskStatus(@Param(value = "taskId") Integer taskId, @Param(value = "status") Integer status);

    /**
     * 更新当前任务池任务的目标用户数
     * @param taskId
     * @param targetNum
     * @return
     */
    int updateMarketingPoolTaskTargetCount(@Param(value = "taskId") Integer taskId, @Param(value = "targetNum") Integer targetNum);

    /**
     * 更新任务池任务信息
     * @param poolTaskDomain
     * @return
     */
    int updateMarketingPoolTaskInfo(@Param(value = "poolTask") MarketingPoolTaskDomain poolTaskDomain);

    List<MarketingPoolTaskDomain> queryJXHMarketingPoolTasks();

    /**
     * 更新当前任务池任务的目标用户数
     * @param saleId
     * @param saleBoidId
     * @param aimSubId
     * @param targetNum
     * @return
     */
    int updateMarketingPoolTaskCountByBaseId(@Param(value = "saleId") String saleId,@Param(value = "saleBoidId") String saleBoidId,@Param(value = "aimSubId") String aimSubId, @Param(value = "targetNum") Integer targetNum);

}

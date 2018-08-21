package com.axon.market.dao.mapper.ischeduling;

import com.axon.market.common.domain.ischeduling.MarketingPoolTaskDomain;
import com.axon.market.common.domain.ischeduling.MarketingTasksDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by yuanfei on 2017/6/6.
 */
@Component("marketingTasksDao")
public interface IMarketingTasksMapper extends IMyBatisMapper
{
    /**
     * 查询所有营销任务的数目
     * @param condition
     * @return
     */
    int queryTasksCount(@Param(value = "condition") Map<String,Object> condition);

    /**
     * 分页查询营销任务数据
     * @param offset
     * @param maxRecord
     * @param condition
     * @return
     */
    List<MarketingTasksDomain> queryTasksByPage(@Param(value = "offset") long offset,@Param(value = "maxRecord") long maxRecord,@Param(value = "condition") Map<String,Object> condition);

    /**
     * 新增营销任务
     * @param taskDomain
     * @return
     */
    int insertMarketingTask(MarketingTasksDomain taskDomain);

    /**
     * 修改营销任务
     * @param taskDomain
     * @return
     */
    int updateMarketingTask(@Param(value = "task") MarketingTasksDomain taskDomain);

    /**
     * 根据id删除任务
     * @param taskId
     * @return
     */
    int deleteMarketingTask(@Param(value = "taskId") Integer taskId);

    /**
     * 批量更新任务的下一次营销时间
     * @return
     */
    int batchUpdateMarketingTasksNextMarketTime();

    /**
     * 更新任务下次营销时间
     * @param taskId
     * @return
     */
    int updateMarketingTaskNextMarketTime(@Param(value = "taskId") Integer taskId);

    /**
     * 根据任务id查询任务
     * @param id
     * @return
     */
    MarketingTasksDomain queryMarketingTaskById(@Param(value = "id") Integer id);

    /**
     * 修改对应任务的状态
     * @param taskId
     * @param status
     * @return
     */
    int updateMarketingTaskStatus(@Param(value = "taskId") Integer taskId, @Param(value = "status") Integer status);

    /**
     * 查询营销任务创建人的
     * @param taskId
     * @return
     */
    String queryUserPhoneOfCreateTaskById(@Param(value = "taskId") Integer taskId);

    /**
     * 查询我审批的营销任务
     * @param auditUserId
     * @return
     */
    List<Map<String, Object>> queryAllMarketJobsAuditByUser(@Param(value = "auditUserId") int auditUserId);

    /**
     * 查询审批记录
     * @param marketJobIds
     * @return
     */
    List<Map<String, Object>> queryMarketingTasksAuditInfo(@Param(value = "marketJobIds") String marketJobIds);

    /**
     * 查询当天待执行的任务
     * @param date
     * @return
     */
    List<MarketingPoolTaskDomain> queryAllWaitingExecuteMarketTask(@Param(value = "date") String date);

    /**
     * 根据id和name校验重名问题
     * @param id
     * @param name
     * @return
     */
    int checkMarketingTaskName(@Param(value = "id") Integer id,@Param(value = "name") String name);

    /**
     * 根据任务ID，修改任务状态
     * @param taskId
     * @param status
     * @return
     */
    int updateMarketTaskExecuteBySystemId(@Param(value = "taskId") Integer taskId,@Param("status") Integer status);

    /**
     * 根据任务ID修改任务的状态及执行次数
     * @param paras
     * @return
     */
    int updateMarketTaskExecuteById(Map<String,Object> paras);

    /**
     *
     * @return
     */
    List<Map<String,Object>> queryMarketingUserDistribution();

    /**
     * 根据任务ID清除该任务的审批记录
     * @param taskId
     * @return
     */
    int deleteMarketingTaskAuditHis(@Param(value = "taskId") Integer taskId);

}

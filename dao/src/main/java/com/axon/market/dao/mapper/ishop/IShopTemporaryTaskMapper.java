package com.axon.market.dao.mapper.ishop;

import com.axon.market.common.domain.iscene.PositionBaseDomain;
import com.axon.market.common.domain.ishop.ShopTaskAuditHistoryDomain;
import com.axon.market.common.domain.ishop.ShopTaskDomain;
import com.axon.market.common.domain.ishop.ShopTaskHistoryDomain;
import com.axon.market.common.domain.ishop.ShopTemporaryTaskDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 位置场景站点配置管理DAO
 * Created by zengcr on 2016/12/2.
 */
@Component("shopTemporaryTaskDao")
public interface IShopTemporaryTaskMapper extends IMyBatisMapper
{
    /**
     * 查询炒店临时任务总数
     * shopTaskId 炒店任务ID,shopTaskName 炒店任务名称,taskType 炒店任务类型，beginTime 开始时间，endTime 结束时间
     *
     * @return
     */
    int queryShopTempTaskTotal(Map<String, Object> paras);
    /**
     * 分页查询炒店临时任务
     * offset 起始标记位,limit 限制大小,shopTaskId 炒店任务ID,shopTaskName 炒店任务名称,taskType 炒店任务类型，beginTime 开始时间，endTime 结束时间
     *
     * @return
     */
    List<ShopTemporaryTaskDomain> queryShopTempTaskByPage(Map<String, Object> paras);
    /**
     * 查询单个炒店临时任务
     * offset 起始标记位,limit 限制大小,shopTaskId 炒店任务ID,shopTaskName 炒店任务名称,taskType 炒店任务类型，beginTime 开始时间，endTime 结束时间
     *
     * @return
     */
    ShopTemporaryTaskDomain queryShopTempTaskById(@Param(value = "taskId") Integer taskId);

    /**
     * 创建待执行的任务
     *
     * @param taskDomain
     * @return
     */
    int insertShopTempTaskPool(ShopTaskDomain taskDomain);

    /**
     * 创建炒店任务关联的炒店信息，根据炒炒店地区查询
     *
     * @param taskDomain
     * @return
     */
    int insertShopTempTaskToBaseByArea(ShopTaskDomain taskDomain);
    /**
     * 更新营销任务的下次营销时间
     */
    int updateShopTempTaskNextTime(Map<String, Object> paras);
    /**
     * 更新营销任务的下次营销时间为开始时间
     */
    int updateShopTempTaskNextTimeAsStart(Map<String, Object> paras);
    /**
     * 根据创建人和任务名称查询任务数
     *
     * @param paras
     * @return
     */
    int queryShopTempTaskNumByName(Map<String, Object> paras);
    /**
     * 创建炒店任务关联的炒店信息，根据炒店ID查询
     *
     * @param taskDomain
     * @return
     */
    int insertShopTempTaskToBaseByIds(ShopTaskDomain taskDomain);
    /**
     * 创建炒店任务
     *
     * @param taskDomain
     * @return
     */
    int insertShopTempTask(ShopTemporaryTaskDomain taskDomain);

    /**
     * 创建炒店任务关联的炒店信息，根据炒店ID查询
     *
     * @param taskDomain
     * @return
     */
    int insertShopTaskToBaseByIds(ShopTaskDomain taskDomain);
    /**
     * 更新炒店任务
     *
     * @param taskDomain
     * @return
     */
    int updateShopTempTask(ShopTemporaryTaskDomain taskDomain);
    /**
     * 根据ID删除炒店临时任务
     * @param shopTaskId
     * @return
     */
    int deleteShopTempTaskById(@Param(value = "shopTaskId") Integer shopTaskId);
}

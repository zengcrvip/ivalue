package com.axon.market.dao.mapper.ishop;

import com.axon.market.common.domain.iscene.PositionBaseDomain;
import com.axon.market.common.domain.iservice.ShopTaskApiDomain;
import com.axon.market.common.domain.ishop.ShopTaskAuditHistoryDomain;
import com.axon.market.common.domain.ishop.ShopTaskChannelDomain;
import com.axon.market.common.domain.ishop.ShopTaskDomain;
import com.axon.market.common.domain.ishop.ShopTaskHistoryDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 炒店任务DAO
 * Created by zengcr on 2017/2/14.
 */
@Component("shopTaskDao")
public interface IShopTaskMapper extends IMyBatisMapper
{
    /**
     * 查询炒店任务总数
     * shopTaskId 炒店任务ID,shopTaskName 炒店任务名称,taskStatus 炒店任务状态
     *
     * @return
     */
    int queryShopTaskTotal(Map<String, Object> paras);

    /**
     * 分页查询炒店任务
     * offset 起始标记位,limit 限制大小,shopTaskId 炒店任务ID,shopTaskName 炒店任务名称,taskStatus 炒店任务状态
     *
     * @return
     */
    List<ShopTaskDomain> queryShopTaskByPage(Map<String, Object> paras);

    /**
     * 根据个性化任务名称，波次编码，客户群编码，营业厅类型查询任务是否存在
     *
     * @param name
     * @param boidId
     * @param segment
     * @param departTypeCode
     * @return
     */
    ShopTaskDomain queryRecommendationShopTaskByName(@Param(value = "name") String name, @Param(value = "boidId") String boidId, @Param(value = "segment") String segment, @Param(value = "departTypeCode") String departTypeCode);

    /**
     * 根据活动编码和波次编码查询相同的活动不同的波次的炒店任务
     *
     * @param saleId
     * @param boidId
     * @return
     */
    List<ShopTaskDomain> queryShopTaskBySaleIdAndBoid(@Param(value = "saleId") String saleId, @Param(value = "boidId") String boidId);

    /**
     * 查询待执行的任务数
     * shopTaskId 炒店任务ID,shopTaskName 炒店任务名称
     *
     * @return
     */
    int queryShopTaskExecuteAll(Map<String, Object> parasMap);

    /**
     * 分页查询炒店任务
     * offset 起始标记位,limit 限制大小,shopTaskId 炒店任务ID,shopTaskName 炒店任务名称
     *
     * @return
     */
    List<ShopTaskDomain> queryShopTaskExecute(Map<String, Object> parasMap);

    /**
     * 查询待执行的任务数
     * shopTaskId 炒店任务ID,shopTaskName 炒店任务名称
     *
     * @return
     */
    int queryShopTaskExecuteAllForClerk(Map<String, Object> parasMap);

    /**
     * 分页查询炒店任务
     * offset 起始标记位,limit 限制大小,shopTaskId 炒店任务ID,shopTaskName 炒店任务名称
     *
     * @return
     */
    List<ShopTaskDomain> queryShopTaskExecuteForClerk(Map<String, Object> parasMap);


    /**
     * 创建炒店任务
     *
     * @param taskDomain
     * @return
     */
    int insertShopTask(ShopTaskDomain taskDomain);

    /**
     * 创建炒店任务关联的炒店信息，根据炒店ID查询
     *
     * @param taskDomain
     * @return
     */
    int insertShopTaskToBaseByIds(ShopTaskDomain taskDomain);

    /**
     * 创建炒店任务关联的炒店信息，根据炒炒店地区查询
     *
     * @param taskDomain
     * @return
     */
    int insertShopTaskToBaseByArea(ShopTaskDomain taskDomain);

    /**
     * 删除炒店任务关联的炒店信息
     *
     * @param taskDomain
     * @return
     */
    int deleteShopTaskToBase(ShopTaskDomain taskDomain);


    /**
     * 删除炒店任务的历史审批记录
     * @param taskDomain
     * @return
     */
    int deleteShopTaskAuditHistory(ShopTaskDomain taskDomain);

    /**
     * 创建待执行的任务
     *
     * @param taskDomain
     * @return
     */
    int insertShopTaskPool(ShopTaskDomain taskDomain);

    /**
     * 根据ID查询炒店任务
     *
     * @param taskId
     * @return
     */
    ShopTaskDomain queryShopTaskById(@Param(value = "taskId") Integer taskId);

    String queryShopPhone(Map<String, Object> paras);

    String queryShopMsgDesc(Map<String, Object> paras);

    /**
     * 根据ID查询炒店任务
     *
     * @param taskId
     * @return
     */
    ShopTaskDomain queryShopTaskPoolById(@Param(value = "taskId") Integer taskId);

    /**
     * 根据ID查询炒店待执行的任务
     *
     * @param taskId
     * @return
     */
    ShopTaskDomain queryShopTaskExecuteById(@Param(value = "taskId") Integer taskId);

    /**
     * 获取任务要执行的门店
     *
     * @param taskId
     * @return
     */
    List<Map<String, Object>> getExecuteBaseByTaskId(@Param(value = "taskId") String taskId, @Param(value = "userId") Integer userId);

    /**
     * 更新营销任务
     *
     * @param taskDomain
     * @return
     */
    int updateShopTask(ShopTaskDomain taskDomain);

    /**
     * 更新待审批池中的营销任务
     *
     * @param taskDomain
     * @return
     */
    int updateShopTaskPool(ShopTaskDomain taskDomain);

    /**
     * 在待办任务池中生成待处理的任务
     *
     * @param paras
     * @return
     */
    int insertShopTaskForExecute(Map<String, String> paras);

    /**
     * 查询待办任务池中该任务是否存在
     * @param paras
     * @return
     */
    int queryShopTaskForExecute(Map<String, String> paras);

    /**
     * 更新营销任务
     *
     * @param taskId
     * @return
     */
    int updateShopTaskById(@Param(value = "taskId") Integer taskId, @Param(value = "status") Integer status);

    /**
     * 查询炒店营销号码
     *
     * @return
     */
    List<Map<String, String>> querFixedAccessNum(Map<String, Object> parasMap);

    /**
     * 查询炒店业务类型
     *
     * @return
     */
    List<Map<String, String>> queryShopBusinessType();

    /**
     * 更新炒店任务池中的任务
     *
     * @return
     */
    int updateShopTaskExecuteById(Map<String, Object> paras);

    /**
     * 更新炒店任务池中的任务
     *
     * @param taskId
     * @param status
     * @return
     */
    int updateShopTaskPoolBySystemId(@Param(value = "taskId") Integer taskId, @Param(value = "status") Integer status);

    /**
     * 查询我审批的炒店
     *
     * @param paras
     * @return
     */
    List<Map<String, Object>> queryAllShopTaskAuditByUser(Map<String, String> paras);

    /**
     * 查询审批记录
     *
     * @param shopTaskIds
     * @return
     */
    List<Map<String, Object>> queryShopTaskAuditInfo(@Param(value = "shopTaskIds") String shopTaskIds);

    /**
     * 插入炒店审批记录
     *
     * @param shopTaskAuditHistoryDomain
     * @return
     */
    int insertShopTaskAuditHistory(ShopTaskAuditHistoryDomain shopTaskAuditHistoryDomain);

    /**
     * @param paras
     * @return
     */
    List<ShopTaskAuditHistoryDomain> queryShopTaskAuditHistoryDomain(Map<String, Object> paras);

    /**
     * 查询炒店任务审核失败的最新一条数据
     *
     * @param paras
     * @return
     */
    Map<String, Object> queryShopTaskAuditReject(Map<String, Object> paras);

    /**
     * 查询炒店任务创建人的手机号码
     *
     * @param taskId
     * @return
     */
    String queryUserPhoneOfCreateShopTask(@Param(value = "taskId") Integer taskId);

    /**
     * @param historyDomain
     */
    int insertMarketHistory(ShopTaskHistoryDomain historyDomain);

    /**
     * 插入导入EXECL文件行数据
     *
     * @param paras
     * @return
     */
    int insertRow(Map<String, Object> paras);

    /**
     * 插入导入EXECL文件本身相关数据（文件名，大小，时间等）
     *
     * @param paras
     * @return
     */
    int insertFile(Map<String, Object> paras);


    /**
     * 查询炒店任务导入号码数据总数
     *
     * @param fileId 文件ID
     * @return
     */
    int queryShopTaskPhoneImportTotal(@Param(value = "fileId") Long fileId);

    /**
     * 查询炒店任务导入号码数据
     *
     * @param offset 每次查询数量
     * @param limit  起始标记位
     * @param fileId 文件ＩＤ
     * @return
     */
    List<Map<String, Object>> queryShopTaskPhoneImport(@Param(value = "offset") int offset, @Param(value = "limit") int limit, @Param(value = "fileId") Long fileId);

    /**
     * @param fileId
     * @return
     */
    List<Map<String, Object>> queryShopTaskPhoneByFileId(@Param(value = "fileId") String fileId);

    /**
     * 临时保存导入指定用户数据
     *
     * @return
     */
    int saveAppointUsersImport(@Param(value = "fileId") Long fileId);

    /**
     * 临时保存导入免打扰用户数据
     *
     * @return
     */
    int saveBlackUsersImport(@Param(value = "fileId") Long fileId);

    /**
     * 根据用户查询上传的历史文件
     *
     * @param paras
     * @return
     */
    List<Map<String, Object>> queryHistoryFileById(Map<String, Object> paras);

    /**
     * 查询指定用户清单
     *
     * @param fileId
     * @return
     */
    List<Map<String, Object>> queryAppointPhoneList(@Param(value = "fileId") Long fileId);

    /**
     * @param fileId
     * @return
     */
    List<String> queryAppointPhoneListByFileId(@Param(value = "fileId") Long fileId);

    /**
     * 查询免打扰用户清单
     *
     * @param fileId
     * @return
     */
    List<Map<String, Object>> queryBlackPhoneList(@Param(value = "fileId") Long fileId);

    /**
     * 自动生成每天的炒店待办任务
     *
     * @return
     */
    int generateShopTaskPool();

    /**
     * 自动生成每天的炒店执行历史记录
     *
     * @return
     */
    int generateShopTaskExecuteHis();

    /**
     * 任务池里待执行的任务
     *
     * @param date
     * @return
     */
    List<ShopTaskDomain> queryAllWaitingExecuteShopTask(@Param(value = "date") String date);

    /**
     * 每天午忙和晚忙需要执行的任务
     *
     * @param date
     * @return
     */
    List<ShopTaskDomain> queryExtralExecuteShopTask(@Param(value = "date") String date);

    List<PositionBaseDomain> queryUpdateShops(@Param(value = "dateTime") String dateTime);

    /**
     * @return
     */
    int updateShopTaskStatus(@Param(value = "taskId") int taskId, @Param(value = "baseId") int baseId, @Param(value = "status") int status);

    /**
     * 修改待办任务状态
     *
     * @param taskId
     * @param status
     * @return
     */
    int updateShopTaskExecuteBySystemId(@Param(value = "taskId") Integer taskId, @Param(value = "baseId") Integer baseId, @Param(value = "status") Integer status);

    //根据任务和炒店ID查询执行的流动拜访任务ID
    List<Map<String, Object>> getPTaskIdByTaskIdAndBaseId(@Param(value = "taskId") Integer taskId, @Param(value = "baseId") Integer baseId);

    //根据任务ID查询执行的流动拜访任务ID
    List<Map<String, Object>> getPTaskIdByTaskId(@Param(value = "taskId") Integer taskId);

    /**
     * 更新营销任务的下次营销时间
     */
    int updateShopTaskNextTime(Map<String, Object> paras);

    /**
     * 更新营销任务的下次营销时间
     */
    int updateShopTaskNextTimeSysTem(Map<String, Object> paras);

    /**
     * 更新营销任务的下次营销时间为开始时间
     */
    int updateShopTaskNextTimeAsStart(Map<String, Object> paras);

    /**
     * 根据创建人和任务名称查询任务数
     *
     * @param paras
     * @return
     */
    int queryShopTaskNumByName(Map<String, Object> paras);

    int updateRecommendationTaskStatus(@Param(value = "time") String time);

    /**
     * 根据主键获取炒店已经执行的常驻用户类型任务数
     *
     * @param domain
     * @return
     */
    int getPerTaskExecuteNumById(ShopTaskDomain domain);

    /**
     * 根据炒店ID插入当前生效的省级任务及地市级任务
     *
     * @param baseId
     * @return
     */
    int addShopAddEffectiveTask(@Param(value = "baseId") Integer baseId, @Param(value = "areaCode") Integer areaCode);

    /**
     * 查询表conf_segment
     *
     * @return
     */
    List<Map<String, Object>> queryConfSegment();

    /**
     * 根据活动文件名称查看活动入库数目
     *
     * @param fileName
     * @return
     */
    List<String> queryShopTaskSaleIdsBySaleFileName(@Param(value = "fileName") String fileName);

    /**
     * 导出炒店任务
     *
     * @param paras(shopTaskId 炒店任务ID,shopTaskName 炒店任务名称,taskStatus 炒店任务状态)
     * @return List<Map<String, Object>>
     */
    List<Map<String, Object>> exportShopTask(Map<String, Object> paras);

    /**
     * 导出炒店任务审批
     *
     * @param
     * @return List<Map<String, Object>>
     */
    List<Map<String, Object>> exportAuditShopTask(Map<String, Object> paras);

    /**
     * 根据ID删除位置场景的对应任务
     * @param baseId 基站站点
     * @return
     */
    int deletePositionBaseTaskById(@Param(value = "baseId") Integer baseId);

    /**
     * 新增shop_task_channel
     * @param dataList
     * @return
     */
    int insertShopTaskChannel(@Param(value = "dataList") List<ShopTaskChannelDomain> dataList);

    /**
     * 查询shop_task_channel待处理的数据
     * @return
     */
    List<ShopTaskChannelDomain> queryShopTaskChannel();

    /**
     * 将渠道更新到炒店
     * @param channelDomain
     * @return
     */
    int pushChannelToshopBase(ShopTaskChannelDomain channelDomain);

    /**
     * 修改shop_task_channel状态
     * @param channelDomain
     * @return
     */
    int updateShopTaskChannelStatus(ShopTaskChannelDomain channelDomain);

    /**
     * 根据taskid查询临时促销点id
     *
     * @param taskId
     * @return
     */
    String queryBaseIdByTaskId(@Param(value = "taskId") Integer taskId);

    /**
     * 根据日期查询当天上线的任务
     * @param date
     * @return
     */
    List<ShopTaskApiDomain> queryShopTasksByDate(@Param(value = "date") String date);

    /**
     * 根据日期和任务ID查询当天上线的该任务详情
     * @param date
     * @return
     */
    ShopTaskApiDomain queryShopTasksByDateAndId(@Param(value = "date") String date , @Param(value = "taskId") String taskId);

    /**
     * 查询七天未执行任务base_id及其相关管理员telephone字段
     */
    List<Map<String,Object>> queryExecuteShopTask(@Param(value = "startTime") String startTime,@Param(value = "endTime") String endTime);

    /**
     * 更改营业厅单独管理员的状态
     */
    int updateUserStatus(@Param(value = "baseId") String baseId);

    /**
     * 更改营业厅的状态
     */
    int updateShopStatus(@Param(value = "baseId") String baseId);


}

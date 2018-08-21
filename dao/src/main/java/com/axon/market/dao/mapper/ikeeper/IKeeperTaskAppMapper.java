package com.axon.market.dao.mapper.ikeeper;

import com.axon.market.common.domain.ikeeper.*;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by yuanfei on 2017/8/15.
 */
@Component("keeperTaskAppDao")
public interface IKeeperTaskAppMapper extends IMyBatisMapper
{

    /**
     * 按照状态，任务类型，分类查询任务分组信息
     * @param userId
     * @return
     */
    List<Map<String,Object>> queryMyKeeperTaskInstGroupDetail(@Param(value = "userId") Integer userId);

    /**
     * 根据状态和类型查询任务实例详情信息
     * @param state
     * @param typeId
     * @return
     */
    List<TaskInstDetailShowDomain> queryTaskInstDetailsByStateAndType(@Param(value = "state") Integer state, @Param(value = "typeId") Integer typeId,@Param(value = "userId")  Integer userId);

    /**
     * 根据类型查询任务实例状态分组的数目信息
     * @param typeId
     * @param userId
     * @return
     */
    List<Map<String,Object>> queryTaskInstGroupCountByType(@Param(value = "typeId") Integer typeId, @Param(value = "userId")  Integer userId);

    /**
     * 根据福利产品的id查询包含的所有产品信息
     * @param welfareId
     * @return
     */
    Map<String,Object> queryWelfareProductsByWelfareId(@Param(value = "welfareId") Integer welfareId);

    /**
     * 根据任务实例详情ID修改状态
     * @param detailId
     * @return
     */
    int updateMaintainOperationStatus(@Param(value = "detailId") Integer detailId,@Param(value = "operateType") String operateType, @Param(value = "callLimit") Integer callLimit, @Param(value = "status") Integer status);

    /**
     * 查询有效的任务，场景关怀(type_id =3)的需要校验exp_time，其他的不校验
     * @param detailId
     */
    TaskInstDetailDomain queryEffectiveTaskDetailById(@Param(value = "detailId") Integer detailId);

    /**
     * 查询实例表中是否有该用户的任务实例
     * @param interfaceUserId
     * @param taskId
     * @return
     */
    TaskInstDomain queryTaskInstByUserAndTask(@Param(value = "interfaceUserId") Integer interfaceUserId, @Param(value = "taskId") Integer taskId);

    /**
     *
     * @param taskInstDomain
     * @return
     */
    int insertTaskInst(TaskInstDomain taskInstDomain);

    /**
     * 根据实例详情id查询详情实例信息
     * @return
     */
    TaskInstDetailDomain queryTaskInstDetailByDetailId(@Param(value = "detailId") Integer detailId);

    /**
     * 根据详情查询到对应的任务信息
     * @param detailId
     * @return
     */
    TaskInstDomain queryTaskInstByDetailId(@Param(value = "detailId") Integer detailId);

    /**
     * 转发任务给同组织业务的人
     * @param detailId
     * @return
     */
    int forwardingTask(@Param(value = "detailId") Integer detailId, @Param(value = "taskInstId") Integer taskInstId);

    /**
     * 批量短信发送获取当前状态，类型对应的所有任务的短信模板
     * @param userId
     * @param typeId
     * @param state
     * @return
     */
    List<Map<String,Object>> queryBatchSmsTaskTemplates(@Param(value = "userId") Integer userId,@Param(value = "typeId") Integer typeId,@Param(value = "state") Integer state);

    /**
     * 批量短信发送 获取当前状态，类型对应的所有任务详情
     * @param userId
     * @param typeId
     * @param state
     * @return
     */
    List<Map<String,Object>> queryBatchSmsTaskDetails(@Param(value = "userId") Integer userId,@Param(value = "typeId") Integer typeId,@Param(value = "state") Integer state,@Param(value = "selectedTaskIds") String selectedTaskIds);

    /**
     * 根据任务实例详情id查询任务的外呼限制次数规则
     * @param detailId
     * @return
     */
    TaskChannelDomain queryTaskChannelByDetailId(@Param(value = "detailId") Integer detailId, @Param(value = "channelType") Integer channelType);

    /**
     * 调话+接口后将结果入库
     * @param taskInstResultDomain
     * @return
     */
    int insertTaskInstResult(TaskInstResultDomain taskInstResultDomain);

    /**
     * 将再次确认的话+通话结果保存入库
     * @param taskInstResultDomain
     * @return
     */
    int confirmTaskInstResult(TaskInstResultDomain taskInstResultDomain);

    /**
     * 批量更新任务实例短信发送状态
     * @param detailIds
     * @return
     */
    int batchUpdateTaskInstDetailSmsState(@Param(value = "detailIds") String detailIds);

    /**
     * 将某个任务中某个用户的维系操作信息保存进记录表，用于任务统计和任务剔重
     * @param detailIds
     * @param type
     * @return
     */
    int insertMaintainedCustomerUnderTaskRecord(@Param(value = "detailIds") String detailIds, @Param(value = "type") Integer type);

    /**
     *
     * @return
     */
    List<TaskInstRecordDomain> queryMaintainedCustomerUnderTaskRecords();

    /**
     * 查询H5页面首页实时任务提醒
     * @param token
     * @return
     */
    List<Map<String,Object>> queryRealTimeReminder(@Param(value = "token") String token);

}

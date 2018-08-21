package com.axon.market.dao.mapper.ikeeper;

import com.axon.market.common.domain.ikeeper.TaskShowDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by wangtt on 2017/8/20.
 */
@Component("keeperTaskDao")
public interface IKeeperTaskMapper extends IMyBatisMapper
{

    /**
     * 查询掌柜业务类型
     *
     * @return
     */
    List<Map<String, Object>> queryKeeperTaskType();


    /**
     *  查询掌柜福利类型
     *
     * @return
     */
    List<Map<String, Object>> queryKeeperWelfareType();


    /**
     * 查询掌柜策略规则
     *
     * @param typeId
     * @return
     */
    List<Map<String, Object>> queryKeeperRuleByTypeId(@Param("typeId") Integer typeId);


    /**
     * 批量提交任务
     *
     * @param param
     * @return
     */
    int batchImportRowData(List<Map<String, Object>> param);

    /**
     * 插入文件信息
     *
     * @param paras
     * @return
     */
    int insertFile(Map<String, Object> paras);

    /**
     * 保存导入掌柜任务客群
     *
     * @param fileId
     * @return
     */
    int saveKeeperTaskCustomer(@Param(value = "fileId") Long fileId);


    /**
     * 创建掌柜任务主表内容
     *
     * @param paraMap
     * @return
     */
    int createKeeperTask(@Param("keeperTask") Map<String, Object> paraMap);

    /**
     * 更新掌柜任务信息
     *
     * @param paraMap
     * @return
     */
    int updateKeeperTask(@Param("keeperTask") Map<String, Object> paraMap);

    /**
     * 创建掌柜任务话+渠道信息
     *
     * @param paraMap
     * @return
     */
    int createKeeperPhoneChannel(@Param("phoneChannel") Map<String, Object> paraMap);

    /**
     * 创建掌柜任务短信渠道信息
     *
     * @param paraMap
     * @return
     */
    int createKeeperSmsChannel(@Param("smsChannel") Map<String, Object> paraMap);


    /**
     * 创建任务审批信息
     *
     * @param paraMap
     * @return
     */
    int createKeeperAudit(@Param("param") Map<String, Object> paraMap);


    /**
     * 创建任务生效规则
     *
     * @param paraMap
     * @return
     */
    int createKeeperRemindRule(@Param("param") Map<String, Object> paraMap);


    /**
     * 创建任务失效规则
     *
     * @param paraMap
     * @return
     */
    int createKeeperFailureRule(@Param("param") Map<String, Object> paraMap);


//    /**
//     * 创建任务福利信息
//     * @param
//     * @return
//     */
//    int createkeeperWelfare(@Param("taskId") String taskId,@Param("welfareId") String welfareId);


    /**
     * 分页查询任务
     *
     * @param param
     * @return
     */
    List<Map<String, Object>> queryKeeperTaskByPage(Map<String, Object> param);

    /**
     * 查询分页数
     *
     * @param param
     * @return
     */
    int queryKeeperTaskByCount(Map<String, Object> param);

    /**
     * 根据id查询任务详情
     *
     * @param id
     * @return
     */
    TaskShowDomain queryTaskById(@Param("taskId") Integer id);

    /**
     * 删除任务id
     *
     * @param taskId
     * @return
     */
    int deleteKeeperTask(@Param("taskId") Integer taskId);

    /**
     * 根据福利id查询任务的个数
     *
     * @param welfareId
     * @return
     */
    int queryTaskCountByWelfareId(@Param("welfareId") String welfareId);

    /**
     * 查询任务名称是否存在
     *
     * @param taskName
     * @return
     */
    int queryTaskNameIsExist(@Param("taskName") String taskName);

    /**
     * 删除旧的渠道信息
     *
     * @param taskId
     * @return
     */
    int deleteOldTaskChannel(@Param("taskId") Integer taskId);


    /**
     * 删除旧的任务审核信息
     *
     * @param taskId
     * @return
     */
    int deleteOldTaskAudit(@Param("taskId") Integer taskId);

    /**
     * 删除任务规则信息
     *
     * @param taskId
     * @return
     */
    int deleteOldTaskRule(@Param("taskId") Integer taskId);

    /**
     * 查询需要我审核的任务信息
     *
     * @param userId
     * @return
     */
    List<Map<String, Object>> queryNeedMeAuditKeeperTask(@Param("taskName") String taskName,@Param("userId") Integer userId, @Param("limit") Integer limit, @Param("offset") Integer offset);

    /**
     * 分页查询需要我审核的任务信息
     *
     * @param userId
     * @return
     */
    int queryNeedMeAuditKeeperTaskCount(@Param("taskName") String taskName,@Param("userId") Integer userId);

    /**
     * 审核掌柜任务
     *
     * @param paramMap
     * @return
     */
    int auditKeeperTask(Map<String, Object> paramMap);


    /**
     * 更新任务状态
     *
     * @param state
     * @return
     */
    int updateKeeperTaskState(@Param("state") Integer state, @Param("taskId") Integer taskId);


    /**
     * 查询任务审核失败原因
     *
     * @param taskId
     * @return
     */
    String queryAuditFailureReason(@Param("taskId") Integer taskId);


    /**
     * 终止任务
     *
     * @param taskId
     * @return
     */
    int terminateKeeperTask(@Param("taskId") Integer taskId);

    /**
     * 终止任务实例
     * @param taskId
     * @return
     */
    int terminateKeeperTaskInst(@Param("taskId") Integer taskId);

    /**
     * 任务过期
     * @param date
     * @return
     */
    int expireKeeperTask(@Param("date") String date);

}

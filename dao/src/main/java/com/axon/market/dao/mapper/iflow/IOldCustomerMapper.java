package com.axon.market.dao.mapper.iflow;

import com.axon.market.common.domain.iflow.OldCustomerDomain;
import com.axon.market.common.domain.iservice.OldCustomerResultDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wangtt on 2017/7/24.
 */
@Component("oldCustomerDao")
public interface IOldCustomerMapper extends IMyBatisMapper
{
    /**
     * 插入导入EXECL文件行数据
     *
     * @param paras
     * @return
     */
    int insertRow(Map<String, Object> paras);

    /**
     * 导入指定用户数据
     * @param param
     * @return
     */
    int batchImportRowData(List<Map<String,Object>> param);

    /**
     * 插入导入EXECL文件本身相关数据（文件名，大小，时间等）
     *
     * @param paras
     * @return
     */
    int insertFile(Map<String, Object> paras);

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
     * 临时保存指定营业厅
     * @param fileId
     * @return
     */
    int saveBaseInfoImport(@Param("fileId") Long fileId);

    /**
     * 新增老用户营销任务
     * @param oldCustomerDomain
     * @return
     */
    int insertOlderCustomer(@Param("oldCustomerDomain") OldCustomerDomain oldCustomerDomain);


    /**
     * 生成任务绑定到剔重过后的指定营业厅上
     * @param fileId
     * @param baseType
     * @return
     */
    int insertTaskToBase(@Param("fileId") String fileId,@Param("baseType") String baseType,@Param("taskId") Integer taskId,@Param("marketAreaCode") String marketAreaCode);

    /**
     * 查询营业厅类型
     * @return
     */
    List<Map<String,Object>> queryLocationType();


    /**
     * 查询老用户优惠活动
     * @param map
     * @return
     */
    List<Map<String,Object>> queryOldCustomerByPage(Map<String,String> map);




    /**
     * 查询已上线老用户优惠活动
     * @param map
     * @return
     */
    List<Map<String,Object>> oldCustomerCheckOut(Map<String,String> map);


    /**
     * 查询总活动数
     * @param map
     * @return
     */
    int  queryOldCustomerByCount(Map<String,String> map);

    /**
     * 查询总活动数
     * @param map
     * @return
     */
    int  oldCustomerCheckOutCounts(Map<String,String> map);

    /**
     * 预览老用户优惠活动
     * @param taskId
     * @return
     */
    OldCustomerDomain previewOldCustomer(@Param("taskId") Integer taskId);

    /**
     * 根据任务ID查出导入营业厅数量
     *
     * @param taskId
     * @return
     */
    int queryAppointBaseInfoById(@Param("taskId") Integer taskId);

    /**
     * 根据地区编码查询地区名称
     * @param areaCodes
     * @return
     */
    List<String> queryMarketAreaDesc(@Param("areaCodes") String areaCodes);


    /**
     * 更新老用户优惠活动信息
     * @param oldCustomerDomain
     * @return
     */
    int updateOldCustomer(@Param("oldCustomerDomain") OldCustomerDomain oldCustomerDomain,@Param("userId") Integer userId);


    /**
     * 根据任务id查询
     * 1.营业厅目标表fileId
     * 2.营业厅类型
     * @param taskId
     * @return
     */
    String queryOldCustomerBaseType(@Param("taskId") Integer taskId);


    /**
     * 删除优惠任务绑定的第三方营业厅Id
     * @param taskId
     * @return
     */
    int deleteOldCustomerBaseInfo(@Param("taskId") Integer taskId);


    /**
     * 查询所有需要审核的任务
     * @param paraMap
     * @return
     */
    List<Map<String,Object>> queryAllNeedAuditTask(Map<String,Object> paraMap);


    /**
     * 查询任务审批历史
     * @param taskIds
     * @return
     */
    List<Map<String,Object>> queryAuditHistoryInfo(@Param("taskIds") String taskIds);


    /**
     * 审核操作记录进审批历史表
     * @param paraMap
     * @return
     */
    int insertIntoAuditHistory(Map<String,Object> paraMap);


    /**
     * 更新审批任务状态
     * @param status
     * @param taskId
     * @return
     */
    int updateOldCustomerById(@Param("status") Integer status,@Param("taskId") Integer taskId);

    /**
     * 查询该任务所有审批记录
     * @param taskId
     * @return
     */
    List<Map<String,Object>> queryOldCustomerAuditHistory(@Param("taskId") Integer taskId);

    /**
     * 操作老用户活动
     * @param taskId
     * @param userId
     * @return
     */
    int handleOldCustomer(@Param("taskId") Integer taskId,@Param("updateUser") Integer userId,@Param("status") Integer status);


    /**
     * 查询审批失败原因
     * @param taskId
     * @return
     */
    List<String> getOldCustomerTaskAuditReason(@Param("taskId") int taskId);


    /**
     * 查询某个地区所有已经上线的任务
     *
     * @return
     */
    List<OldCustomerDomain> queryAllOnlineTask();


    /**
     * 检查用户是否在任务黑名单中
     * @param fileId
     * @param userPhone
     * @return
     */
    int checkBlackUsers(@Param("fileId") String fileId,@Param("userPhone") String userPhone);

    /**
     * 检查用户是否在指定名单中
     * @param fileId
     * @param userPhone
     * @return
     */
    int checkAppointUsers(@Param("fileId") String fileId,@Param("userPhone") String userPhone);


    /**
     * 查询所有地区编码
     * @return
     */
    List<Integer> queryAllAreaCode();

    /**
     * 查询任务
     * @param taskId
     * @return
     */
    Map<String,Object> queryOldCustomerResult(@Param("taskId") Integer taskId);

    /**
     * 终止老用户优惠活动任务
     * @param taskId
     * @param status
     * @param userId
     * @return
     */
    int terminateOldCustomerTask(@Param("taskId") Integer taskId,@Param("status") Integer status,@Param("userId") Integer userId);

    /**
     * 任务到期自动失效
     * @param date
     * @return
     */
    int expireOldCustomer(@Param("date") String date);

    /**
     * 查询所有上线营业厅的营业厅编码
     * @return
     */
    List<String> queryBaseInfoCodeList();


    /**
     * 编辑后删除审批历史
     * @param taskId
     * @return
     */
    int delAuditHistory(@Param("taskId") Integer taskId);

    /**
     * 查询老用户专享审批人
     * @param userId
     * @return
     */
    Map<String,Object> queryAuditStr(@Param("userId") Integer userId);

    /**
     * 查询当前任务的status
     * @param taskId
     * @return
     */
    int queryTaskIsDel(@Param("taskId") Integer taskId);
}

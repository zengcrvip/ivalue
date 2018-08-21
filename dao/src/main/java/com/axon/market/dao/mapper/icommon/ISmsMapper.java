package com.axon.market.dao.mapper.icommon;

import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * Created by yuanfei on 2017/1/20.
 */
@Component("smsDao")
public interface ISmsMapper extends IMyBatisMapper
{
    /**
     * 查询当前任务短信发送状态
     * @param taskId
     * @param spNum
     * @return
     */
    int queryTaskStatus(@Param(value = "taskId") String taskId,@Param(value = "spNum") String spNum);

    /**
     * 更新短信发送状态
     * @param taskId
     * @param spNum
     * @param status
     * @param oldStatus
     * @return
     */
    int updateTaskStatus(@Param(value = "taskId") String taskId,@Param(value = "spNum") String spNum,@Param(value = "status") String status,@Param(value = "oldStatus") String oldStatus);
}

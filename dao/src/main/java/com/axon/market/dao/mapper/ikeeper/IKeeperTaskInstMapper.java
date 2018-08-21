package com.axon.market.dao.mapper.ikeeper;

import com.axon.market.common.domain.ikeeper.TaskCustPhoneDomain;
import com.axon.market.common.domain.ikeeper.TaskInstDomain;
import com.axon.market.common.domain.ikeeper.TaskUserIdsDomain;
import com.axon.market.common.domain.ikeeper.UserMaintainDetailDomain;
import com.axon.market.common.domain.ishopKeeper.TaskCustDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 *  掌柜任务实例
 * Created by zengcr on 2017/8/20.
 */
@Component("keeperTaskInstDao")
public interface IKeeperTaskInstMapper extends IMyBatisMapper{
    /**
     * 查询有效任务 :
     1、按掌柜的维系任务维度 keeperType
     2、任务实例中任务与末梢人员映射的条件：任务的业务归属包含末梢人员所属的业务归属；末梢人员的维系能力包含任务的任务策略类型
     3、必须是审批通过且未到期的任务
     * @param keeperType  掌柜维系类型 1:生日维系  2:2转4维系   3：场景关怀维系   4:优惠到期
     * @return
     */
    List<TaskInstDomain> queryValidTask(@Param("keeperType") Integer keeperType);

    /**
     *  生成掌柜任务实例
     * @param date 日期
     * @param keeperType 掌柜维系类型 1:生日维系  2:2转4维系   3：场景关怀维系   4:优惠到期
     */
    int insertTaskInstByType(@Param("date") String date,@Param("keeperType") Integer keeperType);

    /**
     * 生成掌柜任务实例
     * @param taskInstDomain
     * @return
     */
    int insertTaskInst(TaskInstDomain taskInstDomain);

    /**
     * 查询掌柜末梢用户维系的客户号码
     * @return
     */
    List<UserMaintainDetailDomain> queryUserMaintains();

    /**
     * 根据任务查询任务对应的白名单
     * @return
     */
    List<TaskCustPhoneDomain> queryTaskCustPhones();

    /**
     * 根据任务查询任务对应的执行用户
     * @return
     */
    List<TaskUserIdsDomain> queryTaskUsersBySceneCare(@Param("date") String date,@Param("keeperType") Integer keeperType);

    /**
     * 根据任务ID和执行用户ID查询当天的执行任务实例
     * @param taskId
     * @param userId
     * @return
     */
    TaskInstDomain getTaskInstInfoByByTaskIdAndUserId(@Param("taskId") Integer taskId, @Param("userId") Integer userId);


}

package com.axon.market.core.service.ikeeper.inst;

import com.axon.market.common.domain.ikeeper.TaskInstDetailDomain;
import com.axon.market.common.domain.ikeeper.TaskInstDomain;
import com.axon.market.common.util.SpringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 2转4维系任务任务处理，每天凌晨处理
 * Created by zengcr on 2017/8/22.
 */
@Service("twoFourSceneTaskService")
public class TwoFourSceneTaskService extends TaskExecuteService{

    private static final Logger LOG = Logger.getLogger(TwoFourSceneTaskService.class);

    public static TwoFourSceneTaskService getInstance()
    {
        return (TwoFourSceneTaskService) SpringUtil.getSingletonBean("twoFourSceneTaskService");
    }

    public void run() {
        LOG.info("start twoFourSceneTaskService ...");

        //扫描所有待执行的优惠到期任务
        List<TaskInstDomain> TaskInstDomainList = iKeeperTaskInstMapper.queryValidTask(TWO_FOR_FOUR_TYPE);
        if(CollectionUtils.isEmpty(TaskInstDomainList))
        {
            LOG.info("No two four Task need to be executed today");
            return;
        }
        for(TaskInstDomain taskInstDomain : TaskInstDomainList)
        {
            //1、新增任务实例 keeper_task_inst
            taskInstDomain.setExecDate(getDate(0));
            taskInstDomain.setState(EFFECT);
            iKeeperTaskInstMapper.insertTaskInst(taskInstDomain);

            //2、根据GP查询的换机率匹配top10的用户
            Set<String> userPhoneSet = iTaskCustDataDao.queryTopTenCustomerPhoneOfTwoTransferFour(taskInstDomain.getExecUserId());

            if(CollectionUtils.isEmpty(userPhoneSet))
            {
                LOG.info("TwoFourSceneTaskService Query User Cust Phone is empty by userId  : " +  taskInstDomain.getExecUserId());
                continue;
            }

            //3、找到任务对应的导入白名单
            Set<String> taskPhoneSet = getTaskPhoneSet(taskInstDomain.getTaskId());

            //4、如果任务对应的白名单不为空，再取交集
            if(CollectionUtils.isNotEmpty(taskPhoneSet)){
                userPhoneSet.retainAll(taskPhoneSet);
            }

            //5、拼对象，新增keeper_task_inst_detail
            if(CollectionUtils.isNotEmpty(userPhoneSet)){
                batchInsertTaskInstDetail(taskInstDomain.getTaskInstId(), taskInstDomain.getTypeId(),taskInstDomain.getChannelTypes(), userPhoneSet);
            }
        }

        LOG.info("end twoFourSceneTaskService ...");
    }

}

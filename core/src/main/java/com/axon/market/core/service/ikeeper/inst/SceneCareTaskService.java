package com.axon.market.core.service.ikeeper.inst;

import com.axon.market.common.domain.ikeeper.TaskInstDetailDomain;
import com.axon.market.common.domain.ikeeper.TaskInstDomain;
import com.axon.market.common.domain.ikeeper.TaskUserIdsDomain;
import com.axon.market.common.util.SpringUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 场景关怀维系任务任务处理，每天凌晨处理
 * Created by zengcr on 2017/8/22.
 */
@Service("sceneCareTaskService")
public class SceneCareTaskService extends  TaskExecuteService{
    private static final Logger LOG = Logger.getLogger(SceneCareTaskService.class);
    private static final String GPS_OVER_FLOW_SCENE = "L003";      //流量超套80%场景
    private static final String BALANCE_LIMIT_SCENE = "L204";      //余额不足10元场景
    private static final String FOUR_NET_SCENE = "R003";            //4G首登网场景
    //保存场景关怀维系规则对应的任务
    private static final Map<String,Set<Integer>> ruleForTasksMap = new HashMap<String,Set<Integer>>();
    //保存场景关怀维系任务对应的执行用户
    private static final Map<Integer,Set<Integer>> taskForUserIdsMap = new HashMap<Integer,Set<Integer>>();

    public static SceneCareTaskService getInstance()
    {
        return (SceneCareTaskService) SpringUtil.getSingletonBean("sceneCareTaskService");
    }

    public  Map<String, Set<Integer>> getRuleForTasksMap() {
        return ruleForTasksMap;
    }

    public  Map<Integer, Set<Integer>> getTaskForUserIdsMap() {
        return taskForUserIdsMap;
    }

    /**
     * 任务执行入口
     */
    public void run() {
        LOG.info("SceneCareTaskService run");
        List<TaskInstDomain> TaskInstDomainList = iKeeperTaskInstMapper.queryValidTask(SCENE_TYPE);
        if(null == TaskInstDomainList || TaskInstDomainList.size() == 0) {
            LOG.info("No Scene care Task need to be executed today");
            return;
        }
        Set<Integer> gpsOverSet = new HashSet<Integer>();
        Set<Integer> balanceLimitSet = new HashSet<Integer>();
        Set<Integer> fourNetSet = new HashSet<Integer>();

        for(TaskInstDomain taskInstDomain : TaskInstDomainList){
            //1、新增任务实例 keeper_task_inst
            taskInstDomain.setExecDate(getDate(0));
            taskInstDomain.setState(EFFECT);
            iKeeperTaskInstMapper.insertTaskInst(taskInstDomain);
            int ruleId = taskInstDomain.getRuleId();   //规则ID
            int taskId = taskInstDomain.getTaskId();   //任务ID
            //流量超套
            if(GPS_OVER_SCENE_ID == ruleId){
                gpsOverSet.add(taskId);
                continue;
            }
            //花费不足10元
            if(BALANCE_LIMIT_SCENE_ID == ruleId){
                balanceLimitSet.add(taskId);
                continue;
            }
            //4G首登网
            if(FOUR_NET_SCENE_ID == ruleId){
                fourNetSet.add(taskId);
                continue;
            }
        }
        ruleForTasksMap.put(GPS_OVER_FLOW_SCENE,gpsOverSet);
        ruleForTasksMap.put(BALANCE_LIMIT_SCENE,balanceLimitSet);
        ruleForTasksMap.put(FOUR_NET_SCENE,fourNetSet);

        //根据场景类型查出当天待执行的任务的执行用户
        List<TaskUserIdsDomain> taskUserIds = getTaskUserIdsBySceneCare(SCENE_TYPE);
        for(TaskUserIdsDomain taskUserIdsDomain : taskUserIds){
            taskForUserIdsMap.put(taskUserIdsDomain.getTaskId(),taskUserIdsDomain.getUserSet());
        }
    }

    /**
     * 根据任务ID，执行用户ID，目标客户号码新增任务实例详情
     * @param taskId
     * @param userId
     * @param custPhone
     */
    public void insertTaskInstDetailByTaskIdAndUserId(Integer taskId,Integer userId,String custPhone){
        TaskInstDomain taskInstDomain = getTaskInstInfoByByTaskIdAndUserId(taskId, userId);
        TaskInstDetailDomain taskInstDetailDomain = new TaskInstDetailDomain();
        taskInstDetailDomain.setTaskInstId(taskInstDomain.getTaskInstId());
        taskInstDetailDomain.setTypeId(SCENE_TYPE);
        taskInstDetailDomain.setCustomerName(userInfoMap.get(custPhone));
        taskInstDetailDomain.setTelephone(custPhone);
//        taskInstDetailDomain.setCallTimes(taskInstDomain.getPhoneLimit());
        taskInstDetailDomain.setCallTimes(0);
        taskInstDetailDomain.setExpTime(getInstDetailExpTime(taskInstDomain.getRuleId()));
        taskInstDetailDomain.setState(EFFECT);
        iKeeperTaskInstDetailMapper.insertTaskInstDetail(taskInstDetailDomain);
    }

}

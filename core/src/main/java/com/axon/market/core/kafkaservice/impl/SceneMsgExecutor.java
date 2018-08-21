package com.axon.market.core.kafkaservice.impl;

import com.axon.market.common.domain.kafkaservice.LogInfo;
import com.axon.market.common.domain.kafkaservice.SceneTask;
import com.axon.market.common.util.AxonEncryptUtil;
import com.axon.market.common.util.JsonUtil;
import com.axon.market.common.util.RedisUtil;
import com.axon.market.core.service.ikeeper.inst.SceneCareTaskService;
import com.axon.market.core.service.ishop.SendShopSmsService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zengcr on 2017/7/26.
 * 场景监控实时信息执行器
 */
@Service("sceneMsgExecutor")
public class SceneMsgExecutor {
    private static final Logger LOG = Logger.getLogger(SceneMsgExecutor.class.getName());
    private static final  String MARKET_SCENE_TASK_LIST  = "marketSceneId:[phoneList:taskId]";
    private static final String APP_SCENE = "R002";                  //APP场景
    private static final String GPS_SCENE  =  "L001";                //流量突发场景
    private static final String GPS_OVER_FLOW_SCENE = "L003";      //流量超套80%场景
    private static final String BALANCE_LIMIT_SCENE = "L204";      //余额不足10元场景
    private static final String FOUR_NET_SCENE = "R003";            //4G首登网场景

    @Autowired
    @Qualifier("sendShopSmsService")
    private SendShopSmsService sendShopSmsService;

    @Autowired
    @Qualifier("sceneCareTaskService")
    private SceneCareTaskService sceneCareTaskService;

    @Autowired
    @Qualifier("axonEncrypt")
    private AxonEncryptUtil axonEncrypt;

    /**
     * 执行场景
     * @param logInfo
     */
    public void execute( LogInfo logInfo){
        String sceneType = logInfo.getSceneType();
        if(StringUtils.isNotEmpty(sceneType))
        {
            //如果APP场景或者是流量突发场景
            if(APP_SCENE.equals(sceneType) || GPS_SCENE.equals(sceneType)){
                exectotScene(logInfo);
            }

            //流量超套80%场景,余额不足10元场景, 4G首登网场景
            if(GPS_OVER_FLOW_SCENE.equals(sceneType) || BALANCE_LIMIT_SCENE.equals(sceneType) || FOUR_NET_SCENE.equals(sceneType)){
                executorForKeeper(logInfo);
            }
        }
    }

    /**
     * 执行场景规则执行场景
     * logInfo 消息体
     */
    public void exectotScene( LogInfo logInfo)
    {
        String sceneId = logInfo.getSceneId();
        List<SceneTask> sceneTaskList = new ArrayList<SceneTask>();
        //取出场景ID对应的任务组
        final String sceneTasks = RedisUtil.getInstance().hget(MARKET_SCENE_TASK_LIST, sceneId);
        try {
            if(sceneTasks != null && !"".equals(sceneTasks)){
                sceneTaskList = JsonUtil.stringToObject(sceneTasks, new TypeReference<List<SceneTask>>() {
                });
            }
        } catch (IOException e) {
            LOG.error("sceneTaskList json转list对象 转换失败",e);
        }

        String phone = logInfo.getPhone();
        String logPhone = AxonEncryptUtil.getInstance().encrypt(phone); //号码加密
        //TODO 存在相同的号码永远被同一个任务消费的问题，要用权重去判断
        if(sceneTaskList != null && sceneTaskList.size() > 0) {
            for(SceneTask sceneTask:sceneTaskList){
                List<String>  phoneList = sceneTask.getPhoneList();
                if(phoneList.contains(logPhone)){
                    sendShopSmsService.sendSms(phone,sceneTask.getContentSms(),sceneTask.getTaskId(),sceneTask.getAccessNumber(),3);
                    break;
                }
            }
        }
    }

    /**
     * 执行掌柜的3个场景
     * @param logInfo
     */
    public void executorForKeeper(LogInfo logInfo)
    {
        //根据场景类型获取当前该场景有多少匹配的任务
        String sceneType = logInfo.getSceneType();
        LOG.info("executorForKeeper :" +sceneType );
        Map<String, Set<Integer>>  ruleForTasksMap =  sceneCareTaskService.getRuleForTasksMap();
        Set<Integer> taskSet = ruleForTasksMap.get(sceneType);

        String phone = axonEncrypt.encrypt( logInfo.getPhone());  //号码加密
        if(CollectionUtils.isNotEmpty(taskSet)){
            for(Integer taskId : taskSet){
                //找出任务对应的白名单
                Set<String> taskPhoneSet = sceneCareTaskService.getTaskPhoneSet(taskId);
                //如果白名单不为空且不包含当前号码，返回
                if(CollectionUtils.isNotEmpty(taskPhoneSet)){
                    if(!taskPhoneSet.contains(phone)){
                        continue;
                    }
                }
                //先找出任务对应的执行人
               Map<Integer,Set<Integer>>  taskForUserIdMap = sceneCareTaskService.getTaskForUserIdsMap();
                Set<Integer> userIdSet = taskForUserIdMap.get(taskId);
                if(CollectionUtils.isNotEmpty(userIdSet)){
                    for(Integer userId : userIdSet){
                        //找出执行人对应的维系客户
                        Set<String> userPhoneSet = sceneCareTaskService.getUserPhoneSet(userId);
                        //维系客户中是否包含当前用户
                        if(userPhoneSet.contains(phone)){
                            sceneCareTaskService.insertTaskInstDetailByTaskIdAndUserId(taskId,userId,phone);
                            break;
                        }
                    }
                }
            }
        }
    }
}

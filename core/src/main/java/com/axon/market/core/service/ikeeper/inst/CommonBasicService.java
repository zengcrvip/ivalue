package com.axon.market.core.service.ikeeper.inst;

import com.axon.market.common.domain.ikeeper.TaskCustPhoneDomain;
import com.axon.market.common.domain.ikeeper.TaskInstRecordDomain;
import com.axon.market.common.domain.ikeeper.UserMaintainDetailDomain;
import com.axon.market.common.util.JsonUtil;
import com.axon.market.common.util.RedisUtil;
import com.axon.market.core.service.ikeeper.KeeperTaskAppService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 4种维系任务公共数据加载
 * Created by zengcr on 2017/8/21.
 */
@Service("commonBasicService")
public class CommonBasicService extends  TaskExecuteService{
    private static final Logger LOG = Logger.getLogger(CommonBasicService.class);

    @Autowired
    @Qualifier("keeperTaskAppService")
    private KeeperTaskAppService keeperTaskAppService;

    @PostConstruct
    private void load(){
        if(systemPersonConfig.isKeeperSwitch()){
            LOG.info("commonBasicService load");
//            loadTaskCustPhoneMap();
//            loadUserCustInfoMap();
        }
    }

    /**
     * 加载任务对应的白名单号码
     */
    @Scheduled(cron = "0 10 0 * * ?")
    private void loadTaskCustPhoneMap(){
        if(!systemPersonConfig.isKeeperSwitch()){
            return;
        }
        Map<String, String> taskCustPhoneMap = new HashMap<String,String>();
        List<TaskCustPhoneDomain> taskCustPhoneList = iKeeperTaskInstMapper.queryTaskCustPhones();
        if(CollectionUtils.isNotEmpty(taskCustPhoneList)){
            for(TaskCustPhoneDomain taskCustPhoneDomain : taskCustPhoneList){
                try {
                    taskCustPhoneMap.put(taskCustPhoneDomain.getTaskId(),JsonUtil.objectToString(taskCustPhoneDomain.getPhoneSet()));
                } catch (JsonProcessingException e) {
                    LOG.error("taskCustPhone Set转JSON 转换失败", e);
                }
            }
        }
        //放入redis缓存
        if(taskCustPhoneMap.size() > 0){
            RedisUtil.getInstance().hmset(KEEPER_TASK_CUST_LIST, taskCustPhoneMap);
        }
    }

    /**
     * 加载末梢用户对应的客户
     */
    @Scheduled(cron = "0 10 0 * * ?")
    private void loadUserCustInfoMap(){
        if(!systemPersonConfig.isKeeperSwitch()){
            return;
        }

        Map<String, String> userCustPhoneMap = new HashMap<String,String>();
        List<UserMaintainDetailDomain> userMaintainList  = iKeeperTaskInstMapper.queryUserMaintains();
        if(CollectionUtils.isNotEmpty(userMaintainList)){
            for(UserMaintainDetailDomain userMaintainDetailDomain : userMaintainList){
                //1、组装用户对应的客户号码，放redis缓存
                try {
                    userCustPhoneMap.put(userMaintainDetailDomain.getUserId(), JsonUtil.objectToString(userMaintainDetailDomain.getPhoneSet()));
                } catch (JsonProcessingException e) {
                    LOG.error("userCustPhone Set转JSON 转换失败",e);
                }

                //2、组装用户对应的客户信息，放本机缓存
                Set<Map<String,String>> userInfoSet = userMaintainDetailDomain.getUserSet();
                for(Map<String,String> map : userInfoSet){
                    userInfoMap.put(map.get("phone"),map.get("userName"));
                }
            }
        }
        //放入redis缓存
        if(userCustPhoneMap.size() > 0){
            RedisUtil.getInstance().hmset(KEEPER_USER_CUST_SET, userCustPhoneMap);
        }

//        RedisUtil.getInstance().hmset(KEEPER_USER_CUST_INFO_MAP, userInfoMap);
    }


    /**
     * 加载已经维系过的用户
     */
    @Scheduled(cron = "0 0 23 * * ?")
    private void loadMaintainedCust(){
        List<TaskInstRecordDomain> taskInstRecordDomains = keeperTaskAppService.queryMaintainedCustomerUnderTaskRecords();
        Map<String,String> phoneMap = new HashMap<String,String>();
        for(TaskInstRecordDomain domain : taskInstRecordDomains){
            String key = domain.getPhone() + "-" + domain.getTypeId();
            phoneMap.put(key,"1");
        }
        RedisUtil.getInstance().hmset(KEEPER_MAINTAINED_CUST_SET,phoneMap);
    }

}

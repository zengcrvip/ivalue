package com.axon.market.core.service.ikeeper.inst;

import com.axon.market.common.domain.ikeeper.TaskInstDomain;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.service.ikeeper.KeeperWelfareService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 生日维系任务处理，每天凌晨处理
 * Created by zengcr on 2017/8/20.
 */
@Service("birthdayTaskService")
public class BirthdayTaskService extends  TaskExecuteService{

    private static final Logger LOG = Logger.getLogger(BirthdayTaskService.class);

    @Autowired
    @Qualifier("keeperWelfareService")
    private KeeperWelfareService keeperWelfareService;

    public static BirthdayTaskService getInstance()
    {
        return (BirthdayTaskService) SpringUtil.getSingletonBean("birthdayTaskService");
    }

    @PostConstruct
    public void load(){
        LOG.info("birthdayTaskService load");
//        loadBirthdayCustPhoneSet();
    }

    /**
     * 加载当天生日祝福维系任务目标客户，每天加载一次，在每天当前任务之前处理
     */
    @Scheduled(cron = "0 10 0 * * ?")
    protected void loadBirthdayCustPhoneSet(){
        if(!systemPersonConfig.isKeeperSwitch()){
            return;
        }
        birthdayCustPhoneSet.clear();
        birthdayCustPhoneSet = iTaskCustDataDao.queryCustPhoneNumberByBirthDate(getDate(-1));
    }


    /**
     * 早上6点到晚上18点之间每隔10分钟加载一次，在福利订购表里读取已定够成功的任务实例
     */
    @Scheduled(cron = "0 */10 6-18 * * ?")
    protected  void addBirthdayPhoneToDetail(){
        if(!systemPersonConfig.isKeeperSwitch()){
            return;
        }
        List<Map<String,Object>> welfareRecodes = keeperWelfareService.queryRecordsByState(getDate(0));
        for(Map<String,Object> map : welfareRecodes){
           Set<String> phoneList = keeperWelfareService.queryCustPhoneListByRecordId(String.valueOf(map.get("recordId")));
            batchInsertTaskInstDetail(Integer.parseInt(""+map.get("taskInstId")),Integer.parseInt(""+map.get("typeId")),""+map.get("channelTypes"), phoneList);
            keeperWelfareService.updateRecordById(String.valueOf(map.get("recordId")));
        }
    }



    public void run() {
        LOG.info("birthdayTaskService run");
        //扫描所有待执行的生日维系任务
        List<TaskInstDomain> TaskInstDomainList = iKeeperTaskInstMapper.queryValidTask(BIRTHDAY_TYPE);
        if(null == TaskInstDomainList || TaskInstDomainList.size() == 0) {
            LOG.info("No birthday Task need to be executed today");
            return;
        }
        for(TaskInstDomain taskInstDomain : TaskInstDomainList){
            //1、新增任务实例
            taskInstDomain.setExecDate(getDate(0));
            taskInstDomain.setState(EFFECT);
            iKeeperTaskInstMapper.insertTaskInst(taskInstDomain);

            //2、找到用户对应的维系客户
            Integer executeUserId = taskInstDomain.getExecUserId();
            Set<String> userPhoneSet = getUserPhoneSet(executeUserId);
            if(CollectionUtils.isEmpty(userPhoneSet)){
                LOG.info("birthdayTaskService Query User Cust Phone is empty by userId  : " +  executeUserId);
                continue;
            }

            //3、找到匹配当前任务所属规则的所有白名单用户
            Set<String> taskRulePhoneSet = getTaskRulePhoneSet(taskInstDomain.getRuleId());
            if(CollectionUtils.isEmpty(taskRulePhoneSet)){
                LOG.info("birthdayTaskService Query birth phone is empty by ruleId : " + taskInstDomain.getRuleId());
                continue;
            }

            //4、找到任务对应的导入白名单
            Set<String> taskPhoneSet = getTaskPhoneSet(taskInstDomain.getTaskId());

            //5、用户维系客户集合与满足规则的客户集合取交集 （第2步和第3步）
            userPhoneSet.retainAll(taskRulePhoneSet);
            //6、如果任务对应的白名单不为空，再取交集
            if(CollectionUtils.isNotEmpty(taskPhoneSet)){
                userPhoneSet.retainAll(taskPhoneSet);
            }

            //7、新增掌柜福利订购记录
            if(CollectionUtils.isNotEmpty(userPhoneSet)){
                try {
                    keeperWelfareService.addWelfareRecordByTask(taskInstDomain.getTaskId(),taskInstDomain.getWelfareIds(),executeUserId, TimeUtil.getFixTimes(0, " 01:00:00"),TimeUtil.getFixTimes(0," 17:00:00"),userPhoneSet);
                } catch (Exception e) {
                    LOG.error("keeperWelfareService.addWelfareRecordByTask error",e);
                }
            }
        }
    }
}

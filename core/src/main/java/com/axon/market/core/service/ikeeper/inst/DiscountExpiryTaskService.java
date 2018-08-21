package com.axon.market.core.service.ikeeper.inst;

import com.axon.market.common.domain.ikeeper.TaskInstDetailDomain;
import com.axon.market.common.domain.ikeeper.TaskInstDomain;
import com.axon.market.common.util.SpringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 优惠到期维系任务处理，每天凌晨处理
 * Created by zengcr on 2017/8/21.
 */
@Service("discountExpiryTaskService")
public class DiscountExpiryTaskService extends  TaskExecuteService{
    private static final Logger LOG = Logger.getLogger(DiscountExpiryTaskService.class);


    public static DiscountExpiryTaskService getInstance()
    {
        return (DiscountExpiryTaskService) SpringUtil.getSingletonBean("discountExpiryTaskService");
    }

    @PostConstruct
    protected void load(){
        if(!systemPersonConfig.isKeeperSwitch()){
            return;
        }
        LOG.info("discountExpiryTaskService load");
//        loadDiscountExpireCustPhoneSet();
    }

    /**
     * 加载优惠到期维系任务目标客户，每天凌晨加载一次，在每天当前任务之前处理
     */
    @Scheduled(cron = "0 10 0 * * ?")
    protected void loadDiscountExpireCustPhoneSet(){
        if(!systemPersonConfig.isKeeperSwitch()){
            return;
        }
        enduranceCustPhoneSet.clear();
        crosslinkCustPhoneSet.clear();
        expiringCustPhoneSet.clear();
        enduranceCustPhoneSet = iTaskCustDataDao.queryCustPhoneNumberByDiscountType(DISCOUNT_EXPIRY_ENDURANCE);
        crosslinkCustPhoneSet = iTaskCustDataDao.queryCustPhoneNumberByDiscountType(DISCOUNT_EXPIRY_CROSSLINK);
        expiringCustPhoneSet = iTaskCustDataDao.queryCustPhoneNumberByDiscountType(DISCOUNT_EXPIRY_EXPIRING);
    }

    /**
     * 任务执行入口
     */
    public void run() {
        LOG.info("discountExpiryTaskService run");
        //扫描所有待执行的优惠到期任务
         List<TaskInstDomain> TaskInstDomainList = iKeeperTaskInstMapper.queryValidTask(DISCOUNT_EXPIRY_TYPE);
        if(null == TaskInstDomainList || TaskInstDomainList.size() == 0) {
            LOG.info("No Discount Expiry Task need to be executed today");
            return;
        }
        for(TaskInstDomain taskInstDomain : TaskInstDomainList){
            //1、新增任务实例 keeper_task_inst
            taskInstDomain.setExecDate(getDate(0));
            taskInstDomain.setState(EFFECT);
            iKeeperTaskInstMapper.insertTaskInst(taskInstDomain);

            //2、找到用户对应的维系客户
            Set<String> userPhoneSet = getUserPhoneSet(taskInstDomain.getExecUserId());
            if(CollectionUtils.isEmpty(userPhoneSet)){
                LOG.info("discountExpiryTaskService Query User Cust Phone is empty by userId  : " +  taskInstDomain.getExecUserId());
                continue;
            }

            //3、找到匹配当前任务所属规则的所有白名单用户
            Set<String> taskRulePhoneSet = getTaskRulePhoneSet(taskInstDomain.getRuleId());
            if(CollectionUtils.isEmpty(taskRulePhoneSet)){
                LOG.info("discountExpiryTaskService Query discountExpire phone is empty by ruleId : " + taskInstDomain.getRuleId());
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

            //7、拼对象，新增keeper_task_inst_detail
            if(CollectionUtils.isNotEmpty(userPhoneSet)){
                batchInsertTaskInstDetail(taskInstDomain.getTaskInstId(),taskInstDomain.getTypeId(),taskInstDomain.getChannelTypes(), userPhoneSet);
            }
        }
    }
}

package com.axon.market.core.service.ikeeper.inst;

import com.axon.market.common.bean.SystemPersonConfig;
import com.axon.market.common.cache.RedisCache;
import com.axon.market.common.cache.impl.KeeperCustLock;
import com.axon.market.common.domain.ikeeper.TaskInstDetailDomain;
import com.axon.market.common.domain.ikeeper.TaskInstDomain;
import com.axon.market.common.domain.ikeeper.TaskUserIdsDomain;
import com.axon.market.common.util.JsonUtil;
import com.axon.market.common.util.RedisUtil;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.dao.mapper.ikeeper.IKeeperTaskInstDetailMapper;
import com.axon.market.dao.mapper.ikeeper.IKeeperTaskInstMapper;
import com.axon.market.dao.mapper.ikeeper.ITaskCustDataDao;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 掌柜任务实例生成超类
 * Created by zengcr on 2017/8/21.
 */
@Service
public class TaskExecuteService {
    private static final Logger LOG = Logger.getLogger(TaskExecuteService.class);
    //掌柜维系类型 1:生日维系  2:2转4维系   3：场景关怀维系   4:优惠到期
    protected static final  Integer BIRTHDAY_TYPE  = 1;
    protected static final  Integer TWO_FOR_FOUR_TYPE  = 2;
    protected static final  Integer SCENE_TYPE  = 3;
    protected static final  Integer DISCOUNT_EXPIRY_TYPE = 4;

    //优惠到期维系类型 6：续航  7：搭桥  8：合约到期
    protected static final  Integer DISCOUNT_EXPIRY_ENDURANCE = 6;
    protected static final  Integer DISCOUNT_EXPIRY_CROSSLINK = 7;
    protected static final  Integer DISCOUNT_EXPIRY_EXPIRING = 8;

    //场景关怀类型  3：用户套内流量使用超过80%  4：余额不足10元   5：4G首登网
    protected  static  final  Integer GPS_OVER_SCENE_ID = 3;
    protected  static  final  Integer BALANCE_LIMIT_SCENE_ID = 4;
    protected  static  final  Integer FOUR_NET_SCENE_ID = 5;

    //任务实例状态 0：已失效   1：生效中
    protected  static  final Integer FAILURE = 0;
    protected  static  final Integer EFFECT  = 1;

    //任务对应的白名单号码
    protected static final  String KEEPER_TASK_CUST_LIST  = "keeperTaskId:[phoneList]";

    //末梢人员维系的用户号码
    protected static final  String KEEPER_USER_CUST_SET  = "keeperUserId:[phoneSet]";

    //末梢人员维系的用户详情
    protected static final  String KEEPER_USER_CUST_INFO_MAP  = "keeperUserId:{phone:userName}";
    protected static final  Map<String,String> userInfoMap = new HashMap<String,String>();

    //生日到期维系下的白名单用户
    protected static   Set<String> birthdayCustPhoneSet = new HashSet<String>();
    //优惠到期维系下的续航白名单用户
    protected static   Set<String> enduranceCustPhoneSet = new HashSet<String>();
    //优惠到期维系下的搭桥白名单用户
    protected static  Set<String> crosslinkCustPhoneSet = new HashSet<String>();
    //优惠到期维系下的合约到期白名单用户
    protected static  Set<String> expiringCustPhoneSet = new HashSet<String>();

    //
    protected  static final String KEEPER_MAINTAINED_CUST_SET = "{phone:1}";

    @Autowired
    @Qualifier("keeperTaskInstDao")
    protected IKeeperTaskInstMapper iKeeperTaskInstMapper;

    @Autowired
    @Qualifier("iKeeperTaskInstDetailDao")
    protected IKeeperTaskInstDetailMapper iKeeperTaskInstDetailMapper;

    @Autowired
    @Qualifier("taskCustDataDao")
    protected ITaskCustDataDao iTaskCustDataDao;

    @Autowired
    @Qualifier("redisCache")
    private RedisCache redisCache;

    protected  SystemPersonConfig systemPersonConfig = SystemPersonConfig.getInstance();

    /**
     * 获取日期
     * @param ruleValue
     * @return
     */
    protected String getDate(Integer ruleValue){
        Calendar calendar = Calendar.getInstance();
        switch (ruleValue) {
            case -1 :
//                calendar.add(Calendar.DAY_OF_MONTH, 1);
                return TimeUtil.formatDateToMD(calendar.getTime());
            case 0:
                return  TimeUtil.formatDateToYMD(calendar.getTime());
            case 1:
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                return TimeUtil.formatDateToYMD(calendar.getTime());
            default:
                return null;
        }
    }

    /**
     * 根据任务提醒规则类型获取满足改类型的所有白名单用户
     * @param discountType
     * 1:生日祝福  6：续航  7：搭桥  8：合约到期
     * @return
     */
    protected Set<String> getTaskRulePhoneSet(Integer discountType){
        Set<String> taskRulePhoneSet = new HashSet<String>();
        switch (discountType){
            case  1 :
                taskRulePhoneSet = birthdayCustPhoneSet;
                break;
            case  6 :
                taskRulePhoneSet = enduranceCustPhoneSet;
                break;
            case 7 :
                taskRulePhoneSet = crosslinkCustPhoneSet;
                break;
            case  8 :
                taskRulePhoneSet = expiringCustPhoneSet;
                break;
            default:
                taskRulePhoneSet = new HashSet<String>();
        }
        return taskRulePhoneSet;
    }

    /**
     * 根据失效规则ID查询当前规则失效时间
     * 9：无 10：半小时  11:1小时  12:2小时 1 13:3小时  14:1天   15:3天
     * @param expType
     * @return
     */
    protected String getInstDetailExpTime(Integer expType){
        Calendar calendar = Calendar.getInstance();
        switch (expType) {
            case 9 :
                return "2019-12-30 23:59:59";
            case 10 :
                calendar.add(Calendar.HOUR_OF_DAY, 1);
                return TimeUtil.formatDate(calendar.getTime());
            case 11 :
                calendar.add(Calendar.HOUR_OF_DAY, 1);
                return TimeUtil.formatDate(calendar.getTime());
            case 12 :
                calendar.add(Calendar.HOUR_OF_DAY, 2);
                return TimeUtil.formatDate(calendar.getTime());
            case 13 :
                calendar.add(Calendar.HOUR_OF_DAY, 3);
                return TimeUtil.formatDate(calendar.getTime());
            case 14 :
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                return TimeUtil.formatDate(calendar.getTime());
            case 15 :
                calendar.add(Calendar.DAY_OF_MONTH, 3);
                return TimeUtil.formatDate(calendar.getTime());
            default:
                return "2019-12-30 23:59:59";
        }
    }


    /**
     * 根据员工ID查找员工对应的维系客户
     * @param userId
     * @return
     */
    public  Set<String> getUserPhoneSet(Integer userId){
        Set<String> userPhoneSet = new HashSet<String>();
        try {
            final String userPhones  = RedisUtil.getInstance().hget(KEEPER_USER_CUST_SET, String.valueOf(userId));
            if(userPhones != null && !"".equals(userPhones)) {
                userPhoneSet = JsonUtil.stringToObject(userPhones, new TypeReference<Set<String>>() {
                });
            }
        } catch (Exception e) {
            LOG.error("Query User Cust Phone error by redis userId = " + userId,e);
        }finally {
            return userPhoneSet;
        }
    }

    /**
     * 根据任务ID查找任务导入的白名单
     * @return
     */
    public   Set<String> getTaskPhoneSet(Integer taskId){
        Set<String> taskPhoneSet = new HashSet<String>();
        try {
            final String taskPhones  = RedisUtil.getInstance().hget(KEEPER_TASK_CUST_LIST, String.valueOf(taskId));
            if(taskPhones != null && !"".equals(taskPhones)){
                taskPhoneSet = JsonUtil.stringToObject(taskPhones, new TypeReference<Set<String>>() {
                });
            }
        } catch (Exception e) {
            LOG.error("Query Task Cust Phone error by redis taskId = " + taskId,e);
        }finally {
            return taskPhoneSet;
        }
    }

    /**
     * 根据掌柜场景类型查出当天待执行的任务下对应的执行用户
     * @param keepType
     * @return
     */
    protected List<TaskUserIdsDomain> getTaskUserIdsBySceneCare(Integer keepType){
        return  iKeeperTaskInstMapper.queryTaskUsersBySceneCare(getDate(0),keepType);
    }

    /**
     * 根据任务ID和用户ID查询当天该用户执行的该任务实例信息
     * @param taskId
     * @param userId
     * @return
     */
    public TaskInstDomain getTaskInstInfoByByTaskIdAndUserId(Integer taskId,Integer userId){
         return iKeeperTaskInstMapper.getTaskInstInfoByByTaskIdAndUserId(taskId, userId);
    }

    /**
     * 批量插入任务实例详情
     * @param taskInstId 任务实例ID
     * @param typeId 类型ID
     * @param channelTypes 渠道类型，逗号隔开 1：短信  2：话+
     * @param userPhoneSet 号码清单
     */
    protected void batchInsertTaskInstDetail(Integer taskInstId, Integer typeId, String channelTypes, Set<String> userPhoneSet){
        List<TaskInstDetailDomain> instDetailList = new ArrayList<TaskInstDetailDomain>();
        for(String phone : userPhoneSet){
            //去重处理
            String key = phone + "-" + typeId;
//            KeeperCustLock lock = new KeeperCustLock(key,3);
//            if(redisCache.doAction(lock)){
//                continue;
//            }
            String isExists = RedisUtil.getInstance().hget(KEEPER_MAINTAINED_CUST_SET,key);
            if(isExists != null && "1".equals(isExists)){
                continue;
            }
            TaskInstDetailDomain taskInstDetailDomain = new TaskInstDetailDomain();
            taskInstDetailDomain.setTaskInstId(taskInstId);
            taskInstDetailDomain.setTypeId(typeId);
            taskInstDetailDomain.setCustomerName(userInfoMap.get(phone));
            taskInstDetailDomain.setTelephone(phone);
            taskInstDetailDomain.setCallTimes(0);
            taskInstDetailDomain.setState(EFFECT);
            taskInstDetailDomain.setSmsResult(channelTypes.indexOf("1") >= 0 ? 0 : 2);
            taskInstDetailDomain.setCallResult(channelTypes.indexOf("2") >= 0 ? 0 : 2);
            instDetailList.add(taskInstDetailDomain);
        }
        if(CollectionUtils.isNotEmpty(instDetailList)){
            iKeeperTaskInstDetailMapper.batchInsertTaskInstDetail(instDetailList);
        }
    }
}

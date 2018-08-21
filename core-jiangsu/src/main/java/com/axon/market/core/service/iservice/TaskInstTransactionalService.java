//package com.axon.market.core.service.iservice;
//
//import com.axon.market.common.domain.ishopKeeper.*;
//import com.axon.market.common.domain.isystem.UserDomain;
//import com.axon.market.common.excel.util.DateUtil;
//import com.axon.market.common.util.AxonEncryptUtil;
//import com.axon.market.core.service.ichannel.ContactChannelService;
//import com.axon.market.core.service.icommon.AttrService;
//import com.axon.market.core.service.ikeeper.DirectionalCustomerBaseService;
//import com.axon.market.core.service.ishopKeeper.ShopKeeperProductService;
//import com.axon.market.core.service.ishopKeeper.TaskInstDetailService;
//import com.axon.market.core.service.ishopKeeper.TaskInstService;
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.*;
//
///**
// * Created by Duzm on 2017/8/17.
// */
//@Component("taskInstTransactionalService")
//public class TaskInstTransactionalService {
//
//    private static final Logger LOG = Logger.getLogger(BirthdayTaskService.class);
//
//    //保存产品ID和产品名称之间的映射,做一个临时缓存
//    private Map<String,String> productInfo = null;
//
//
//    @Autowired
//    @Qualifier("taskInstService")
//    private  TaskInstService taskInstService;
//
//    @Autowired
//    @Qualifier("taskInstDetailService")
//    private TaskInstDetailService taskInstDetailService;
//
//    @Autowired
//    @Qualifier("shopKeeperProductService")
//    private ShopKeeperProductService shopKeeperProductService;
//
//    @Autowired
//    @Qualifier("directionalCustomerBaseService")
//    private DirectionalCustomerBaseService directionalCustomerBaseService;
//
//    @Autowired
//    @Qualifier("attrService")
//    private AttrService attrService;
//
//    @Transactional
//    public  void generateTaskInst(Set<String> custPhoneNumberList, Set<String> taskCustPhoneNumberList,UserDomain userDomain, String ruleValue,
//                                 TaskDomain taskDomain,List<TaskChannelDomain> taskChannelDomainList,String productIds) throws Exception {
//
//        //初始化
//        productInfo = new HashMap<>();
//        //查询员工维系的客户号码
//        List<Map<String,String>> userCustPhoneNumberList = directionalCustomerBaseService.queryUserCustPhoneNumberList(userDomain.getId());
//        if(null == userCustPhoneNumberList || userCustPhoneNumberList.isEmpty()){
//            LOG.debug("Saff did not maintain cust usrid =  " + userDomain.getId());
//            return;
//        }
//
//        TaskInstDomain taskInstDomain = new TaskInstDomain();
//        taskInstDomain.setEffDate(taskDomain.getEffDate());
//        taskInstDomain.setWaitExecDate(DateUtil.dateToString(new Date(), "yyyyMMdd"));
//        taskInstDomain.setProductIds(productIds);
//        this.setProductNames(taskInstDomain);
//        taskInstDomain.setUserId(userDomain.getId());
//        taskInstDomain.setExpDate(taskDomain.getExpDate());
//        taskInstDomain.setHoldingRules(ruleValue);
//        taskInstDomain.setTaskId(taskDomain.getTaskId());
//        taskInstDomain.setTypeId(taskDomain.getTypeId());
//        taskInstDomain.setTaskTypeName(attrService.queryAttrValueDisplayValue("EXP_KEEPER_TASK_TYPE",taskDomain.getTypeId().toString()));
//        taskInstDomain.setTaskName(taskDomain.getTaskName());
//        taskInstService.insertTaskInst(taskInstDomain);
//        List<TaskInstDetailDomain> taskInstDetailDomainList = new LinkedList<>();
//        TaskInstDetailDomain taskInstDetailDomain = null;
//        for (Map<String,String> userMapInfo : userCustPhoneNumberList) {
//            String custPhoneNumber = userMapInfo.get("user_phone");
//            String encryptCustPhoneNumber = AxonEncryptUtil.getInstance().encrypt(custPhoneNumber);
//            //首先号码必须在GP里面存在
//            if(custPhoneNumberList.contains(encryptCustPhoneNumber)) {
//                //任务配置了号码，则需要匹配任务对应的号码，没配置任何号码则不匹配
//                if(null != taskCustPhoneNumberList && !taskCustPhoneNumberList.isEmpty() && !taskCustPhoneNumberList.contains(custPhoneNumber)){
//                    continue;
//                }
//                taskInstDetailDomain = new TaskInstDetailDomain();
//                taskInstDetailDomain.setTelephone(custPhoneNumber);
//                taskInstDetailDomain.setTypeId(taskInstDomain.getTypeId());
//                taskInstDetailDomain.setTaskInstId(taskInstDomain.getTaskInstId());
//                setTaskInstDetailDomain(taskInstDetailDomain, taskChannelDomainList);
//                taskInstDetailDomain.setCustName(userMapInfo.get("user_name"));
//                taskInstDetailDomainList.add(taskInstDetailDomain);
//            }
//        }
//        taskInstDetailService.batchInsertTaskInstDetail(taskInstDetailDomainList);
//    }
//
//    public void setTaskInstDetailDomain(TaskInstDetailDomain taskInstDetailDomain, List<TaskChannelDomain> taskChannelDomainList){
//        for (TaskChannelDomain taskChannelDomain : taskChannelDomainList) {
//            switch (taskChannelDomain.getChannelId()) {
//                case ContactChannelService.SMS_CHANNEL_ID : taskInstDetailDomain.setSmsTimes(0);break;
//                case ContactChannelService.PHONE_PLUS_CHANNEL_ID : taskInstDetailDomain.setCallTimes(0);break;
//                case ContactChannelService.IT_CHANNEL_ID : taskInstDetailDomain.setItTimes(0);break;
//            }
//        }
//    }
//
//    public void setProductNames(TaskInstDomain taskInstDomain){
//        StringBuilder stringBuilder = new StringBuilder();
//        for (String productId : taskInstDomain.getProductIds().split(",")) {
//            String productName = this.productInfo.get(productId);
//            if (null == productName){
//                ShopKeeperProductDomain shopKeeperProductDomain = shopKeeperProductService.queryProductByProductId(Integer.parseInt(productId));
//                if(null == shopKeeperProductDomain) {
//                    LOG.error("can not find product product_id = " + productId);
//                    continue;
//                }
//                productName = shopKeeperProductDomain.getProductName();
//                productInfo.put(productId, productName);
//            }
//            stringBuilder.append(productName).append(",");
//        }
//        stringBuilder.deleteCharAt(stringBuilder.length() -1 );
//        taskInstDomain.setProductNames(stringBuilder.toString());
//    }
//}
